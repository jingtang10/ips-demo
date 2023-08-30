package com.google.android.fhir.ipsapp

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ca.uhn.fhir.context.FhirContext
import ca.uhn.fhir.context.FhirVersionEnum
import com.example.ipsapp.utils.DocumentUtils
import com.google.android.fhir.library.IPSDocument


class GetData : AppCompatActivity() {

  val docUtils = DocumentUtils()
  private val parser = FhirContext.forCached(FhirVersionEnum.R4).newJsonParser()
  override fun onCreate(savedInstanceState: Bundle?) {

    super.onCreate(savedInstanceState)

    // set the text view value to the body of the response from the POST
    setContentView(R.layout.get_data)
    val textView = findViewById<TextView>(R.id.textView)
    textView.movementMethod = ScrollingMovementMethod()

    val doc = intent.getSerializableExtra("doc", IPSDocument::class.java)
    textView.setText(parser.encodeResourceToString(doc?.document))

    println(doc.toString())
    println(doc?.document.toString())
  }

}