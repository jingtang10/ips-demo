package com.google.android.fhir.library

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
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
    problemsTable: TableLayout,
    allergiesSection: LinearLayout,
    problemSection: LinearLayout,
    medicationSection: LinearLayout,
    immunizationSection: LinearLayout,
    resultsSection: LinearLayout
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
          val separator = View(context)
          separator.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            5 // Height of the separator line in pixels
          )
          separator.setBackgroundColor(Color.BLACK)
          immunizationTable.addView(separator)
          immunizationSection.visibility = View.VISIBLE
        }

        ResourceType.AllergyIntolerance -> {
          val allergy = (entry.resource as AllergyIntolerance).code.coding[0].display
          if ((entry.resource as AllergyIntolerance).clinicalStatus.coding.firstOrNull()?.code == "active") {
            val row = TableRow(context)
            val allergyTextView = createTextView(context, allergy)
            row.addView(allergyTextView)
            row.setBackgroundColor(Color.LTGRAY)
            allergiesTable.addView(row)
            val separator = View(context)
            separator.layoutParams = TableRow.LayoutParams(
              TableRow.LayoutParams.MATCH_PARENT,
              5 // Height of the separator line in pixels
            )
            separator.setBackgroundColor(Color.BLACK)
            allergiesTable.addView(separator)
            allergiesSection.visibility = View.VISIBLE
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
            val separator = View(context)
            separator.layoutParams = TableRow.LayoutParams(
              TableRow.LayoutParams.MATCH_PARENT,
              5 // Height of the separator line in pixels
            )
            separator.setBackgroundColor(Color.BLACK)
            resultsTable.addView(separator)
            resultsSection.visibility = View.VISIBLE
          }
        }

        ResourceType.Medication -> {
          val medication = (entry.resource as Medication).code.coding
          val medicationDisplays = medication.joinToString("\n") { "${it.display} (${it.code})" }
          val medicationSystems = medication.joinToString("\n") { it.system }
          // if ((entry.resource as AllergyIntolerance).clinicalStatus.coding.firstOrNull()?.code == "active") {
            val row = TableRow(context)
            val medicationTextView = createTextView(context, medicationDisplays)
          val medicationSystemView = createTextView(context, medicationSystems)
            medicationTextView.gravity = Gravity.START
            medicationSystemView.gravity = Gravity.START
            row.addView(medicationTextView)
            row.addView(medicationSystemView)
            row.setBackgroundColor(Color.LTGRAY)
            medicationTable.addView(row)
            val separator = View(context)
            separator.layoutParams = TableRow.LayoutParams(
              TableRow.LayoutParams.MATCH_PARENT,
              5 // Height of the separator line in pixels
            )
            separator.setBackgroundColor(Color.BLACK)
            medicationTable.addView(separator)
            medicationSection.visibility = View.VISIBLE
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
              val separator = View(context)
              separator.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                5 // Height of the separator line in pixels
              )
              separator.setBackgroundColor(Color.BLACK)
              problemsTable.addView(separator)
              problemSection.visibility = View.VISIBLE
            }
          }
        }

        else -> {}
      }
    }
    addGapToTable(immunizationTable, 30)
    addGapToTable(allergiesTable, 30)
    addGapToTable(problemsTable, 30)
    addGapToTable(resultsTable, 30)
    addGapToTable(medicationTable, 30)
  }

  private fun addGapToTable(table: TableLayout, gapHeight: Int) {
    val gapRow = TableRow(table.context)
    val params = TableRow.LayoutParams(
      TableRow.LayoutParams.MATCH_PARENT,
      TableRow.LayoutParams.WRAP_CONTENT
    )
    gapRow.layoutParams = params
    gapRow.minimumHeight = gapHeight
    table.addView(gapRow)
  }
}
