package com.google.android.fhir.library

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.widget.HorizontalScrollView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import org.hl7.fhir.r4.model.AllergyIntolerance
import org.hl7.fhir.r4.model.Composition
import org.hl7.fhir.r4.model.Condition
import org.hl7.fhir.r4.model.Immunization
import org.hl7.fhir.r4.model.Medication
import org.hl7.fhir.r4.model.Observation
import org.hl7.fhir.r4.model.ResourceType

class IPSRenderer(val doc: IPSDocument?) {

  private fun createTextView(context: Context, text: String): TextView {
    return TextView(context).apply {
      this.text = text
      setPadding(8, 8, 8, 8)
      gravity = Gravity.CENTER
    }
  }

  private fun createHorizontalScrollView(
    context: Context,
    textView: TextView,
  ): HorizontalScrollView {
    return HorizontalScrollView(context).apply {
      addView(textView)
    }
  }

  fun render(
    context: Context,
    immunizationTable: TableLayout,
    allergiesTable: TableLayout,
    resultsTable: TableLayout,
    documentView: TextView,
    medicationTable: TableLayout,
    problemsTable: TableLayout
  ) {
    val entries = doc?.document?.entry
    entries?.firstOrNull()?.let { firstEntry ->
      if (firstEntry.resource.resourceType == ResourceType.Composition) {
        documentView.text = "Summary Date: \n${(firstEntry.resource as Composition).date}"
      }
    }
    entries?.forEach { entry ->
      when (entry.resource.resourceType) {
        ResourceType.Immunization -> {
          val date = (entry.resource as Immunization).occurrenceDateTimeType.valueAsString
          val vaccine = (entry.resource as Immunization).vaccineCode.coding[0].display
          val row = TableRow(context)
          val dateTextView = createTextView(context, date)
          val vaccineHorizontalTextView = createTextView(context, vaccine)
          val horizontalScrollView = createHorizontalScrollView(context, vaccineHorizontalTextView)
          row.addView(dateTextView)
          row.addView(horizontalScrollView)
          row.setBackgroundColor(Color.LTGRAY)
          immunizationTable.addView(row)
        }

        ResourceType.AllergyIntolerance -> {
          val allergy = (entry.resource as AllergyIntolerance).code.coding[0].display
          if ((entry.resource as AllergyIntolerance).clinicalStatus.coding.firstOrNull()?.code == "active") {
            val row = TableRow(context)
            val allergyTextView = createTextView(context, allergy)
            row.addView(allergyTextView)
            row.setBackgroundColor(Color.LTGRAY)
            allergiesTable.addView(row)
          }
        }

        ResourceType.Observation -> {
          val date = (entry.resource as Observation).effectiveDateTimeType.valueAsString
          val resultName = (entry.resource as Observation).code.coding.firstOrNull()?.display
          if (resultName != null) {
            val row = TableRow(context)
            val dateTextView = createTextView(context, date)
            val resultNameTextView = createTextView(context, resultName)
            val horizontalScrollView = createHorizontalScrollView(context, resultNameTextView)
            row.addView(dateTextView)
            row.addView(horizontalScrollView)
            row.setBackgroundColor(Color.LTGRAY)
            resultsTable.addView(row)
          }
        }

        ResourceType.Medication -> {
          val medication = (entry.resource as Medication).code.coding[0].display
          // if ((entry.resource as AllergyIntolerance).clinicalStatus.coding.firstOrNull()?.code == "active") {
            val row = TableRow(context)
            val medicationTextView = createTextView(context, medication)
            row.addView(medicationTextView)
            row.setBackgroundColor(Color.LTGRAY)
            medicationTable.addView(row)
          // }
        }

        ResourceType.Condition -> {
          val problem = (entry.resource as Condition).code.coding.firstOrNull()?.display
          if ((entry.resource as Condition).clinicalStatus.coding.firstOrNull()?.code == "active") {
            if (problem != null) {
              val row = TableRow(context)
              val problemTextView = createTextView(context, problem)
              val horizontalScrollView = createHorizontalScrollView(context, problemTextView)
              row.addView(horizontalScrollView)
              row.setBackgroundColor(Color.LTGRAY)
              problemsTable.addView(row)
            }
          }
        }

        else -> {}
      }
    }
    val immunizationGap = TableRow(context)
    immunizationGap.layoutParams = TableRow.LayoutParams(
      TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT
    )
    immunizationGap.minimumHeight = 30 // Set the desired gap height here
    immunizationTable.addView(immunizationGap)

    val allergiesGap = TableRow(context)
    allergiesGap.layoutParams = TableRow.LayoutParams(
      TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT
    )
    allergiesGap.minimumHeight = 30 // Set the desired gap height here
    allergiesTable.addView(allergiesGap)

    val resultsGap = TableRow(context)
    resultsGap.layoutParams = TableRow.LayoutParams(
      TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT
    )
    resultsGap.minimumHeight = 30 // Set the desired gap height here
    resultsTable.addView(resultsGap)

    val medicationGap = TableRow(context)
    medicationGap.layoutParams = TableRow.LayoutParams(
      TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT
    )
    medicationGap.minimumHeight = 30 // Set the desired gap height here
    medicationTable.addView(medicationGap)
  }
}
