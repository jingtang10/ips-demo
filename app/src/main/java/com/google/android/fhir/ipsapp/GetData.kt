package com.google.android.fhir.ipsapp

import android.os.Bundle
import android.widget.TableLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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

    // textView.movementMethod = ScrollingMovementMethod()

    val doc = intent.getSerializableExtra("doc", IPSDocument::class.java)
    // textView.text = parser.encodeResourceToString(doc?.document)
    val ipsRenderer = IPSRenderer(doc)
    ipsRenderer.render(this, immunizationTable, allergiesTable, resultsTable, documentView, medicationTable, problemsTable)

  }

}