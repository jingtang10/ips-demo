package com.google.android.fhir.ipsapp

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ca.uhn.fhir.context.FhirContext
import ca.uhn.fhir.context.FhirVersionEnum
import com.google.android.fhir.library.IPSDocument
import com.google.android.fhir.library.IPSRenderer


class GetData : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {

    super.onCreate(savedInstanceState)

    // set the text view value to the body of the response from the POST
    setContentView(R.layout.get_data)
    // val textView = findViewById<TextView>(R.id.textView)

    val patientView = findViewById<TextView>(R.id.patient)
    val documentView = findViewById<TextView>(R.id.document)
    val immunizationTable = findViewById<TableLayout>(R.id.immunizationTable)
    val allergiesTable = findViewById<TableLayout>(R.id.allergiesTable)
    val resultsTable = findViewById<TableLayout>(R.id.resultsTable)
    val medicationTable = findViewById<TableLayout>(R.id.medicationTable)
    val problemsTable = findViewById<TableLayout>(R.id.problemsTable)

    val allergiesSection = findViewById<LinearLayout>(R.id.allergiesSection)
    val problemSection = findViewById<LinearLayout>(R.id.problemSection)
    val medicationSection = findViewById<LinearLayout>(R.id.medicationSection)
    val immunizationSection = findViewById<LinearLayout>(R.id.immunizationSection)
    val resultsSection = findViewById<LinearLayout>(R.id.resultsSection)

    allergiesSection.visibility = View.GONE
    problemSection.visibility = View.GONE
    medicationSection.visibility = View.GONE
    immunizationSection.visibility = View.GONE
    resultsSection.visibility = View.GONE
    // textView.movementMethod = ScrollingMovementMethod()

    val doc = intent.getSerializableExtra("doc", IPSDocument::class.java)
    // textView.text = parser.encodeResourceToString(doc?.document)
    val parser = FhirContext.forCached(FhirVersionEnum.R4).newJsonParser()
    patientView.text = parser.encodeResourceToString(doc?.document)
    val ipsRenderer = IPSRenderer(doc)
    ipsRenderer.render(this, immunizationTable, allergiesTable, resultsTable, documentView, medicationTable, problemsTable, allergiesSection, problemSection, medicationSection, immunizationSection, resultsSection)

  }

}