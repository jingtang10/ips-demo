package com.example.ipsapp.utils

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.methods.HttpPost
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.entity.StringEntity
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.impl.client.CloseableHttpClient
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.impl.client.HttpClients
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.util.EntityUtils
import com.nimbusds.jose.EncryptionMethod
import com.nimbusds.jose.JWEAlgorithm
import com.nimbusds.jose.JWEHeader
import com.nimbusds.jose.JWEObject
import com.nimbusds.jose.Payload
import com.nimbusds.jose.crypto.DirectEncrypter
import java.nio.charset.StandardCharsets
import java.security.SecureRandom
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Base64

class GenerateShlUtils {
  fun getManifestUrl(): String {
    val httpClient: CloseableHttpClient = HttpClients.createDefault()
    val httpPost = HttpPost("https://api.vaxx.link/api/shl")
    httpPost.addHeader("Content-Type", "application/json")

    // Recipient and passcode entered by the user on this screen
    val jsonData = "{}"
    val entity = StringEntity(jsonData)

    httpPost.entity = entity

    return httpClient.execute(httpPost).use { response ->
      val responseBody = EntityUtils.toString(response.entity, StandardCharsets.UTF_8)
      responseBody
    }
  }

  @RequiresApi(Build.VERSION_CODES.O)
  fun generateRandomKey(): String {
    val random = SecureRandom()
    val keyBytes = ByteArray(32)
    random.nextBytes(keyBytes)
    println(keyBytes.toString())
    return Base64.getUrlEncoder().encodeToString(keyBytes)
  }

  @RequiresApi(Build.VERSION_CODES.O)
  fun encrypt(data: String, key: String): String {
    val header = JWEHeader(JWEAlgorithm.DIR, EncryptionMethod.A256GCM)
    val jweObj = JWEObject(header, Payload(data))
    val decodedKey = Base64.getUrlDecoder().decode(key)
    val encrypter = DirectEncrypter(decodedKey)

    jweObj.encrypt(encrypter)
    return jweObj.serialize()
  }

  @RequiresApi(Build.VERSION_CODES.O)
  fun postPayload(file: String, manifestUrl: String, key: String, managementToken: String) {

    // encode the file here (convert to JWE)
    // val encryptionKey = urlUtils.generateRandomKey()
    // Log.d("enc key", encryptionKey)

    // val contentJson = Gson().toJson(file.trim())
    val contentEncrypted = encrypt(file, key)
    Log.d("encrypted content", contentEncrypted)

    // Log.d("encryption key", key)

    val jwtHeader = "{\"zip\": \"DEF\", \"alg\": \"ES256\", \"kid\": \"${key}\"}"
    val finalContent = Base64.getUrlEncoder().withoutPadding()
      .encodeToString(jwtHeader.toByteArray()) + "." + contentEncrypted
    Log.d("final content", finalContent)

    // val encryptedContent = encryptContent(JSONObject(file), encryptionKey)


    val httpClient: CloseableHttpClient = HttpClients.createDefault()
    Log.d("manifest", manifestUrl)
    val httpPost = HttpPost("$manifestUrl/file")
    httpPost.addHeader("Content-Type", "application/fhir+json")
    // httpPost.addHeader("Content-Length", file.length.toString())
    httpPost.addHeader("Authorization", "Bearer $managementToken")

    val entity = StringEntity(contentEncrypted)

    httpPost.entity = entity
    val response = httpClient.execute(httpPost)

    val responseBody = EntityUtils.toString(response.entity, StandardCharsets.UTF_8)
    Log.d("Response status: ", "${response.statusLine.statusCode}")
    Log.d("Response body: ", responseBody)
    httpClient.close()
  }

  // converts the inputted expiry date to epoch seconds
  @RequiresApi(Build.VERSION_CODES.O)
  fun dateStringToEpochSeconds(dateString: String): Long {
    val formatter = DateTimeFormatter.ofPattern("yyyy-M-d")
    val localDate = LocalDate.parse(dateString, formatter)
    val zonedDateTime = localDate.atStartOfDay(ZoneOffset.UTC)
    return zonedDateTime.toEpochSecond()
  }

  @RequiresApi(Build.VERSION_CODES.O)
  fun base64UrlEncode(data: String): String {
    val bytes = data.toByteArray(StandardCharsets.UTF_8)
    return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
  }
}