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
import com.nimbusds.jose.JWEDecrypter
import com.nimbusds.jose.JWEHeader
import com.nimbusds.jose.JWEObject
import com.nimbusds.jose.Payload
import com.nimbusds.jose.crypto.DirectDecrypter
import com.nimbusds.jose.crypto.DirectEncrypter
import java.io.ByteArrayOutputStream
import java.nio.charset.StandardCharsets
import java.security.SecureRandom
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Base64
import java.util.zip.DataFormatException
import java.util.zip.Inflater
import org.json.JSONObject


class ReadShlUtils {

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
        if (jsonObject.has("verifiableCredential")) {
            val verifiableCredentialArray = jsonObject.getJSONArray("verifiableCredential")

            if (verifiableCredentialArray.length() > 0) {
                // Assuming you want the first item from the array
                return verifiableCredentialArray.getString(0)
            }
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
    fun decodeShc(responseBody: String, key: String): String {
        val jweObject = JWEObject.parse(responseBody)
        val decodedKey: ByteArray = Base64.getUrlDecoder().decode(key)
        val decrypter: JWEDecrypter = DirectDecrypter(decodedKey)
        jweObject.decrypt(decrypter)
        println(jweObject.payload.toString().trim())
        return jweObject.payload.toString()
    }
}
