package com.google.android.fhir.ipsapp

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.TableLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ca.uhn.fhir.context.FhirContext
import ca.uhn.fhir.context.FhirVersionEnum
import com.google.android.fhir.library.IPSDocument


class GetData : AppCompatActivity() {

  private val parser = FhirContext.forCached(FhirVersionEnum.R4).newJsonParser()
  override fun onCreate(savedInstanceState: Bundle?) {

    super.onCreate(savedInstanceState)

    // set the text view value to the body of the response from the POST
    setContentView(R.layout.get_data)
    val textView = findViewById<TextView>(R.id.textView)

    val patientView = findViewById<TextView>(R.id.patient)
    val documentView = findViewById<TextView>(R.id.document)
    val immunizationTable = findViewById<TableLayout>(R.id.immunizationTable)

    textView.movementMethod = ScrollingMovementMethod()

    val doc = intent.getSerializableExtra("doc", IPSDocument::class.java)
    textView.text = parser.encodeResourceToString(doc?.document)
    // val ipsRenderer = IPSRenderer(doc)
    // ipsRenderer.render(this, immunizationTable, documentView)

  }

}