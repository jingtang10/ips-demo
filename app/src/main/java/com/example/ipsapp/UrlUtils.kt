package com.example.ipsapp

import android.os.Build
import android.provider.SyncStateContract.Constants
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
import com.nimbusds.jose.crypto.AESEncrypter
import com.nimbusds.jose.crypto.DirectEncrypter
import com.nimbusds.jose.jwk.OctetSequenceKey
import com.nimbusds.jose.util.Base64URL
// import io.jsonwebtoken.Jwts
// import io.jsonwebtoken.SignatureAlgorithm
// import io.jsonwebtoken.security.Keys
import java.io.ByteArrayOutputStream
import java.nio.charset.StandardCharsets
import java.security.Key
import java.security.SecureRandom
import java.security.Security
import java.util.Base64
import java.util.zip.DataFormatException
import java.util.zip.Deflater
import java.util.zip.Inflater
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.jose4j.jwe.JsonWebEncryption
import org.jose4j.keys.AesKey
import org.json.JSONObject

open class UrlUtils {

    // Extracts the part of the link after the 'shlink:/'
    fun extractUrl(scannedData: String): String {
        return scannedData.substringAfterLast("shlink:/")
    }

    // Decodes the extracted url from Base64Url to a byte array
    @RequiresApi(Build.VERSION_CODES.O)
    fun decodeUrl(extractedUrl: String): ByteArray {
        return Base64.getUrlDecoder().decode(extractedUrl.toByteArray())
    }


    // returns a string of the data in the verifiableCredential field in the returned JSON
    fun extractVerifiableCredential(jsonString: String): String {
        val jsonObject = JSONObject(jsonString)
        val verifiableCredentialArray = jsonObject.getJSONArray("verifiableCredential")

        if (verifiableCredentialArray.length() > 0) {
            // Assuming you want the first item from the array
            return verifiableCredentialArray.getString(0)
        }
        return ""
    }

    // Decodes and decompresses the payload in the JWE token
    @RequiresApi(Build.VERSION_CODES.O)
    fun decodeAndDecompressPayload(token: String): String {
        val tokenParts = token.split('.')
        val decoded = Base64.getUrlDecoder().decode(tokenParts[1])

        val inflater = Inflater(true)
        inflater.setInput(decoded)

        val initialBufferSize = 100000
        val decompressedBytes = ByteArrayOutputStream(initialBufferSize)
        val buffer = ByteArray(8192) // You can adjust the buffer size as needed

        try {
            while (!inflater.finished()) {
                val length = inflater.inflate(buffer)
                decompressedBytes.write(buffer, 0, length)
            }
            decompressedBytes.close()
            // val finalDecompressedData = decompressedBytes.toByteArray()
        } catch (e: DataFormatException) {
            e.printStackTrace()
        }

        inflater.end()

        return decompressedBytes.toByteArray().decodeToString()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun decodeShc(responseBody: String, key: String): String? {
        val decodedKey: ByteArray = Base64.getUrlDecoder().decode(key)
        val jweKey: Key = AesKey(decodedKey)

        val jwe = JsonWebEncryption()
        if (responseBody.split('.').size == 5) {
            jwe.compactSerialization = responseBody
            jwe.key = jweKey
            return jwe.plaintextString
        } else {
            return ""
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun encodeAndCompressPayload(payload: String, encryptionKey: String): String {
        val deflater = Deflater(Deflater.BEST_COMPRESSION, true)
        deflater.setInput(payload.toByteArray(Charsets.UTF_8))
        deflater.finish()

        val initialBufferSize = 100000
        val compressedBytes = ByteArrayOutputStream(initialBufferSize)
        val buffer = ByteArray(8192) // You can adjust the buffer size as needed

        while (!deflater.finished()) {
            val length = deflater.deflate(buffer)
            compressedBytes.write(buffer, 0, length)
        }
        deflater.end()

        val jweHeader = JWEHeader.Builder(JWEAlgorithm.DIR, EncryptionMethod.A256GCM).build()

        // Create the JWE payload
        val jwePayload = Payload(compressedBytes.toByteArray())

        Log.d("key sjads", encryptionKey)

        // Parse the encryption key
        val dirJWK = OctetSequenceKey.parse(encryptionKey)

        // Create the JWE object and encrypt it with the encryption key
        val jweObject = JWEObject(jweHeader, jwePayload)
        val dirEncrypter = DirectEncrypter(dirJWK)
        jweObject.encrypt(dirEncrypter)

        // Return the serialized JWE object (compact form)
        return jweObject.serialize()
    }

    fun getManifestUrl(): String {
        var responseBody = ""
        GlobalScope.launch(Dispatchers.IO) {
            val httpClient: CloseableHttpClient = HttpClients.createDefault()
            val httpPost = HttpPost("https://api.vaxx.link/api/shl")
            httpPost.addHeader("Content-Type", "application/json")

            // Recipient and passcode entered by the user on this screen
            val jsonData = "{}"
            val entity = StringEntity(jsonData)

            httpPost.entity = entity
            val response = httpClient.execute(httpPost)

            responseBody = EntityUtils.toString(response.entity, StandardCharsets.UTF_8)
            // Log.d("Response status: ", "${response.statusLine.statusCode}")
            // Log.d("Response body: ", responseBody)
            httpClient.close()
        }
        return responseBody
    }

    // Currently uses has "alg: A256KW" and "enc: A256GCM" but needs "alg: dir"
    @RequiresApi(Build.VERSION_CODES.O)
    fun encryptContent(contentJson: String, encryptionKey: String): String {
        val decodedKey = Base64URL(encryptionKey).decode()
        Security.addProvider(BouncyCastleProvider())
        val header = JWEHeader(JWEAlgorithm.A256KW, EncryptionMethod.A256GCM)
        val secretKey: SecretKey = SecretKeySpec(decodedKey, "AES")

        val jweObject = JWEObject(header, Payload(contentJson))
        jweObject.encrypt(AESEncrypter(secretKey))
        return jweObject.serialize()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun generateRandomKey(): String {
        val random = SecureRandom()
        val keyBytes = ByteArray(32)
        random.nextBytes(keyBytes)
        return Base64.getUrlEncoder().encodeToString(keyBytes)
    }
}
