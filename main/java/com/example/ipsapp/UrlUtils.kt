package com.example.ipsapp

import android.os.Build
import androidx.annotation.RequiresApi
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
    return Base64.getUrlDecoder().decode(extractedUrl.toByteArray())
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

fun extractVerifiableCredential(jsonString: String): String? {
    val jsonObject = JSONObject(jsonString)
    val verifiableCredentialArray = jsonObject.optJSONArray("verifiableCredential")

    if (verifiableCredentialArray != null && verifiableCredentialArray.length() > 0) {
        // Assuming you want the first item from the array
        return verifiableCredentialArray.getString(0)
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

@RequiresApi(Build.VERSION_CODES.O)
fun decodeShc(responseBody: String, key: String): String? {
    val decodedKey: ByteArray = Base64.getUrlDecoder().decode(key)
    val key: Key = AesKey(decodedKey)

    val jwe = JsonWebEncryption()
    jwe.compactSerialization = responseBody
    jwe.key = key

    return jwe.plaintextString
}
