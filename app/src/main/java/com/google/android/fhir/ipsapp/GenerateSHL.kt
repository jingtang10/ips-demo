package com.google.android.fhir.ipsapp

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.fhir.library.LinkGenerator
import com.google.android.fhir.library.dataClasses.SHLData


class GenerateSHL : AppCompatActivity() {

  private val linkGenerator = LinkGenerator()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.view_shl)

    val passcode: String = intent.getStringExtra("passcode").toString()
    val shlData = intent.getSerializableExtra("shlData", SHLData::class.java)
    val passcodeField = findViewById<TextView>(R.id.passcode)
    val expirationDateField = findViewById<TextView>(R.id.expirationDate)
    passcodeField.text = passcode
    expirationDateField.text = shlData?.exp

    if (shlData?.ipsDoc?.document != null) {
      val view = findViewById<ImageView>(R.id.qrCode)
      linkGenerator.generateSHL(
        this, shlData, passcode, view
      )
      // view.setImageBitmap(bitmap)
    }
  }
}