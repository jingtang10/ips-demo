package com.example.ipsapp

import android.os.Build
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity


class GetData : AppCompatActivity() {

  val urlUtils = UrlUtils()

  // val fhirEngine = FhirApplication.fhirEngine(applicationContext)
  @RequiresApi(Build.VERSION_CODES.O)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // set the text view value to the body of the response from the POST
    setContentView(R.layout.get_data)
    val embeddedArray: Array<out String>? = intent.getStringArrayExtra("embeddedArray")
    val key = intent.getStringExtra("key").toString()
    val textView = findViewById<TextView>(R.id.textView)
    textView.movementMethod = ScrollingMovementMethod()

    var count = 0;

    // now need to base64Url decode the thing in here to get the fhir resource
    // go through each element of the responseBody and do this (somehow set different text views on the page to the data
    if (embeddedArray != null) {

      var healthData = ""
      // may be worth putting this loop in decodeShc?
      for (elem in embeddedArray) {
        val decodedShc = urlUtils.decodeShc(elem, key)
        textView.text = decodedShc

        if (decodedShc != null) {
          val toDecode = urlUtils.extractVerifiableCredential(decodedShc)
          //so this gives you the JWT string to split, decode and decompress
          healthData = healthData + "\n" + urlUtils.decodeAndDecompressPayload(toDecode) + "\n"
          Log.d("to extract vc", decodedShc)
          Log.d("extracted", toDecode)
          Log.d(count.toString(), urlUtils.decodeAndDecompressPayload(toDecode))
          count++
        }
      }

      // set the text view to the final outputted data
      textView.text = healthData
      for (i in healthData.indices step 200) {
        val endIndex = if (i + 200 > healthData.length) healthData.length else i + 200
        println(healthData.substring(i, endIndex))
      }

      // Save something to the fhir engine here. Like this:

      // val patient = Patient().apply{
      //   id = "19682646"
      //   gender = Enumerations.AdministrativeGender.MALE
      //   addName(
      //     HumanName().apply {
      //       addGiven("Lance")
      //       addGiven("Thomas")
      //       family = "East"
      //     }
      //   )
      // }
      //
      // CoroutineScope(Dispatchers.Main).launch {
      //   fhirEngine.create(patient)
      // }



    }

  }

}