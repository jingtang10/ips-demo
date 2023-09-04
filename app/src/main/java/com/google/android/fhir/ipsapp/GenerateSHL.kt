package com.google.android.fhir.ipsapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ca.uhn.fhir.context.FhirContext
import ca.uhn.fhir.context.FhirVersionEnum
import com.example.ipsapp.utils.GenerateShlUtils
import com.google.android.fhir.library.IPSDocument


class GenerateSHL : AppCompatActivity() {

  private val generateShlUtils = GenerateShlUtils()
  private val parser = FhirContext.forCached(FhirVersionEnum.R4).newJsonParser()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.view_shl)

    val passcode: String = intent.getStringExtra("passcode").toString()
    val labelData: String = intent.getStringExtra("label").toString()
    val expirationDate: String = intent.getStringExtra("expirationDate").toString()
    val ipsDoc = intent.getSerializableExtra("ipsDoc", IPSDocument::class.java)

    println("generation ${parser.encodeResourceToString(ipsDoc?.document)}")

    val passcodeField = findViewById<TextView>(R.id.passcode)
    val expirationDateField = findViewById<TextView>(R.id.expirationDate)
    passcodeField.text = passcode
    expirationDateField.text = expirationDate

    if (ipsDoc?.document != null) {
      generateShlUtils.generatePayload(
        passcode,
        labelData,
        expirationDate,
        ipsDoc.document!!,
        findViewById(R.id.qrCode),
        this
      )
    }
  }
}