package com.example.ipsapp

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.io.ByteArrayOutputStream
import java.security.Key
import java.util.Base64
import java.util.zip.DataFormatException
import java.util.zip.Deflater
import java.util.zip.Inflater
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
        val verifiableCredentialArray = jsonObject.optJSONArray("verifiableCredential")

        if (verifiableCredentialArray != null && verifiableCredentialArray.length() > 0) {
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
            val finalDecompressedData = decompressedBytes.toByteArray()
            // Use the finalDecompressedData as needed
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
        jwe.compactSerialization = responseBody
        jwe.key = jweKey

        return jwe.plaintextString
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun encodeAndCompressPayload(payload: String): String {
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

        val encodedCompressedPayload = Base64.getUrlEncoder().encodeToString(compressedBytes.toByteArray())

        return encodedCompressedPayload
    }
}
