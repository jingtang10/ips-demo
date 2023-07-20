package com.example.ipsapp

import android.os.Build
import androidx.annotation.RequiresApi
import java.io.ByteArrayOutputStream
import java.security.Key
import java.util.Base64
import java.util.zip.Inflater
import org.jose4j.jwe.JsonWebEncryption
import org.jose4j.keys.AesKey
import org.json.JSONObject

internal fun extractUrl(scannedData: String): String {
    return scannedData.substringAfterLast("shlink:/")
}

@RequiresApi(Build.VERSION_CODES.O)
internal fun decodeUrl(extractedUrl: String): ByteArray? {
    return Base64.getDecoder().decode(extractedUrl.toByteArray())
}

fun ByteArray.zlibDecompress(): String {
    val inflater = Inflater()
    val outputStream = ByteArrayOutputStream()

    return outputStream.use {
        val buffer = ByteArray(1024)

        inflater.setInput(this)

        var count = -1
        while (count != 0) {
            count = inflater.inflate(buffer)
            outputStream.write(buffer, 0, count)
        }

        inflater.end()
        outputStream.toString("UTF-8")
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun decodeJwt(jwt: String, keyString: String): String? {
    val decodedKey: ByteArray = Base64.getUrlDecoder().decode(keyString)
    val key: Key = AesKey(decodedKey)

    val jwe = JsonWebEncryption()
    jwe.compactSerialization = jwt
    jwe.key = key

    return jwe.plaintextString
}

@RequiresApi(Build.VERSION_CODES.O)
fun decodePayload(response: String): String {
    val jsonObject = JSONObject(response)
    val url = jsonObject.get("")

    val filesArray = jsonObject.getJSONArray("files")
    val embeddedList = ArrayList<String>()

    for (i in 0 until filesArray.length()) {
        val fileObject = filesArray.getJSONObject(i)
        val embeddedValue = fileObject.getString("embedded")
        embeddedList.add(embeddedValue)
    }

    val embeddedArray = embeddedList.toTypedArray()
    val decodedPayload = Base64.getUrlDecoder().decode(embeddedArray[0].split('.')[1] + "==")
    return decodedPayload.zlibDecompress()
}

fun extractVerifiableCredential(jsonString: String): String? {
    val jsonObject = JSONObject(jsonString)
    val verifiableCredentialArray = jsonObject.optJSONArray("verifiableCredential")

    if (verifiableCredentialArray != null && verifiableCredentialArray.length() > 0) {
        // Assuming you want the first item from the array
        val verifiableCredentialString = verifiableCredentialArray.getString(0)
        return verifiableCredentialString
    }

    return null
}

@RequiresApi(Build.VERSION_CODES.O)
fun decodeAndDecompressPayload(token: String): String {
    val tokenParts = token.split('.')
    val decoded = Base64.getUrlDecoder().decode(tokenParts[1])

    val inflater = Inflater(true)
    inflater.setInput(decoded)
    val decompressedBytes = ByteArray(4096)
    val length = inflater.inflate(decompressedBytes)

    inflater.end()

    return decompressedBytes.copyOf(length).decodeToString()
}
