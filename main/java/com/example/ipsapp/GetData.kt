package com.example.ipsapp

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.security.Key
import java.util.Base64
import org.jose4j.jwe.JsonWebEncryption
import org.jose4j.keys.AesKey


class GetData : AppCompatActivity() {
  @RequiresApi(Build.VERSION_CODES.O)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // set the text view value to the body of the response from the POST
    setContentView(R.layout.get_data)
    val responseBody: Array<out String>? = intent.getStringArrayExtra("responseBody")
    val key = intent.getStringExtra("key").toString()
    val textView = findViewById<TextView>(R.id.textView)

    // val jsonObject = JSONObject(responseBody)
    //
    // val filesArray = jsonObject.getJSONArray("files")
    // val embeddedList = ArrayList<String>()
    //
    // for (i in 0 until filesArray.length()) {
    //   val fileObject = filesArray.getJSONObject(i)
    //   val embeddedValue = fileObject.getString("embedded")
    //   embeddedList.add(embeddedValue)
    // }
    //
    // val embeddedArray = embeddedList.toTypedArray()

    // now need to base64Url decode the thing in here to get the fhir resource
    // go through each element of the responseBody and do this (somehow set different text views on the page to the data
    val decodedShc = responseBody?.let { decodeShc(it.get(1), key) }

    textView.text = decodedShc

    if (decodedShc != null) {
      Log.d("shc decode", decodedShc)
      val toDecode = extractVerifiableCredential(decodedShc)

      var healthData = ""

      //so this gives you the JWT string to split, decode and decompress
      if (toDecode != null) {
        Log.d("toDecode", toDecode)
        healthData = decodeAndDecompressPayload(toDecode)
      }

      // set the text view to the final outputted data
      textView.text = healthData
    }



  }

  @RequiresApi(Build.VERSION_CODES.O)
  private fun decodeShc(responseBody: String, key: String): String? {
    val decodedKey: ByteArray = Base64.getUrlDecoder().decode(key)
    val key: Key = AesKey(decodedKey)

    val jwe = JsonWebEncryption()
    jwe.compactSerialization = responseBody
    jwe.key = key

    return jwe.plaintextString
  }
}