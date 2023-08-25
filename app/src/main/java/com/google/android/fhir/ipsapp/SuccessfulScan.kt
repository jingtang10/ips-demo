package com.google.android.fhir.ipsapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.ipsapp.utils.ReadShlUtils
import com.google.android.fhir.library.SHLData
import com.google.android.fhir.library.decode.Decoder


class SuccessfulScan : AppCompatActivity() {

  private val readShlUtils = ReadShlUtils()

  override fun onCreate (savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.successfulscan)

    val shlData = intent.getParcelableExtra("shlData", SHLData::class.java)

    val decoder = Decoder(shlData)
    val passcodeEditText = findViewById<EditText>(R.id.passcode)
    val hasPasscode = decoder.hasPasscode()

    if (!hasPasscode) {
      passcodeEditText.visibility = View.INVISIBLE
    }

    // when button is pushed, the inputted data is passed into fetchData()
    val button = findViewById<Button>(R.id.getData)
    button.setOnClickListener {
      val recipientField = findViewById<EditText>(R.id.recipient).text.toString()
      val doc = if (hasPasscode) {
        val passcodeField = passcodeEditText.text.toString()
        decoder.decodeSHLToDocument(recipientField, passcodeField)
      } else {
        decoder.decodeSHLToDocument(recipientField)
      }
      val i = Intent(this@SuccessfulScan, GetData::class.java)
      i.putExtra("doc", doc)
      startActivity(i)
    }
  }

}