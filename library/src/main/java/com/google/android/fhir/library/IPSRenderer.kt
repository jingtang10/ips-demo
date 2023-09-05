package com.google.android.fhir.library

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.widget.HorizontalScrollView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import org.hl7.fhir.r4.model.Composition
import org.hl7.fhir.r4.model.Immunization
import org.hl7.fhir.r4.model.ResourceType

class IPSRenderer(val doc: IPSDocument?) {
  fun render(context : Context, immunizationTable: TableLayout, documentView: TextView) {


    val entries = doc?.document?.entry
    if (entries?.first()?.hasResource() == true) {
      if (entries.first()!!.resource.resourceType == ResourceType.Composition) {
        documentView.text = (entries.first()!!.resource as Composition).date.toString()
      }
    }
    if (entries != null) {
      for (entry in entries) {
        if (entry.resource.resourceType === ResourceType.Immunization) {
          val date: String = (entry.resource as Immunization).occurrenceDateTimeType.valueAsString
          val vaccine: String = (entry.resource as Immunization).vaccineCode.coding.get(0).display

          // Create a new table row
          val row = TableRow(context)

          // Create two TextViews for date and vaccine
          val dateTextView = TextView(context)
          dateTextView.text = date
          dateTextView.setPadding(8, 8, 8, 8)
          dateTextView.gravity = Gravity.CENTER

          val vaccineTextView = TextView(context)
          vaccineTextView.text = vaccine
          vaccineTextView.setPadding(8, 8, 8, 8)
          vaccineTextView.gravity = Gravity.CENTER

// Add the TextViews to the row
          row.addView(dateTextView)

// Create a new TextView for the horizontalScrollView
          val vaccineHorizontalTextView = TextView(context)
          vaccineHorizontalTextView.text = vaccine
          vaccineHorizontalTextView.setPadding(8, 8, 8, 8)
          vaccineHorizontalTextView.gravity = Gravity.CENTER

          val horizontalScrollView = HorizontalScrollView(context)
          horizontalScrollView.addView(vaccineHorizontalTextView)
          row.addView(horizontalScrollView)
          row.setBackgroundColor(Color.YELLOW)

// Add the row to the TableLayout
          immunizationTable.addView(row)

        }
      }
    }
  }


}