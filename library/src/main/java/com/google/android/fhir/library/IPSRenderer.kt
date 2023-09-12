package com.google.android.fhir.library

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import org.hl7.fhir.r4.model.AllergyIntolerance
import org.hl7.fhir.r4.model.Composition
import org.hl7.fhir.r4.model.Condition
import org.hl7.fhir.r4.model.Immunization
import org.hl7.fhir.r4.model.Medication
import org.hl7.fhir.r4.model.Observation
import org.hl7.fhir.r4.model.Patient
import org.hl7.fhir.r4.model.ResourceType

class IPSRenderer(private val context: Context, private val parentView: ViewGroup) {

  fun renderData(doc: IPSDocument) {
    val immunizationTable: MaterialCardView = parentView.findViewById(R.id.immunizationTable)
    val allergiesTable: MaterialCardView = parentView.findViewById(R.id.allergiesTable)
    val resultsTable: MaterialCardView = parentView.findViewById(com.google.android.fhir.library.R.id.resultsTable)
    val medicationTable: MaterialCardView = parentView.findViewById(com.google.android.fhir.library.R.id.medicationTable)
    val problemsTable: MaterialCardView = parentView.findViewById(com.google.android.fhir.library.R.id.problemsTable)
    val allergiesSection: MaterialCardView = parentView.findViewById(com.google.android.fhir.library.R.id.allergiesSection)
    val problemSection: MaterialCardView = parentView.findViewById(com.google.android.fhir.library.R.id.problemSection)
    val medicationSection: MaterialCardView = parentView.findViewById(com.google.android.fhir.library.R.id.medicationSection)
    val immunizationSection: MaterialCardView = parentView.findViewById(com.google.android.fhir.library.R.id.immunizationSection)
    val resultsSection: MaterialCardView = parentView.findViewById(com.google.android.fhir.library.R.id.resultsSection)
    val patientTable: MaterialCardView = parentView.findViewById(com.google.android.fhir.library.R.id.patientTable)
    val documentTable: MaterialCardView = parentView.findViewById(com.google.android.fhir.library.R.id.documentTable)
    val entries = doc?.document?.entry

    var hasImmunizationData = false
    var hasAllergiesData = false
    var hasResultsData = false
    var hasMedicationData = false
    var hasProblemsData = false

    entries?.firstOrNull()?.let { firstEntry ->
      if (firstEntry.resource.resourceType == ResourceType.Composition) {
        val documentText = "Summary Date: ${(firstEntry.resource as Composition).dateElement.value}"
        val docHorizontalTextView = createTextView(context, documentText)
        docHorizontalTextView.textAlignment = View.TEXT_ALIGNMENT_VIEW_START
        addCardViewToTable(documentTable, docHorizontalTextView)
      }
    }

    entries?.forEach { entry ->
      when (entry.resource.resourceType) {
        ResourceType.Immunization -> {
          val date = (entry.resource as Immunization).occurrenceDateTimeType.valueAsString
          val vaccine = (entry.resource as Immunization).vaccineCode.coding[0].display
          val dateTextView = createTextView(context, date)
          val vaccineHorizontalTextView = createTextView(context, vaccine)
          addCardViewToTable(immunizationTable, dateTextView)
          addCardViewToTable(immunizationTable, vaccineHorizontalTextView)
          hasImmunizationData = true
        }

        ResourceType.Patient -> {
          val patient = (entry.resource as Patient)
          val patientText =
            "Name: ${patient.name.first().givenAsSingleString} ${patient.name.first().family} \nBirth Date: ${patient.birthDateElement.valueAsString}"
          val patientTextView = createTextView(context, patientText)
          addCardViewToTable(patientTable, patientTextView)
        }

        ResourceType.AllergyIntolerance -> {
          val allergyEntry = (entry.resource as AllergyIntolerance)
          val allergy = allergyEntry.code.coding[0].display
          if (allergyEntry.clinicalStatus.coding.firstOrNull()?.code == "active") {
            val categories =
              allergyEntry.category.joinToString(" - ") { it.valueAsString }
            val allergyText =
              "${allergyEntry.type.name} - $categories - Criticality: ${allergyEntry.criticality.name}\n$allergy"
            val allergyTextView = createTextView(context, allergyText)
            addCardViewToTable(allergiesTable, allergyTextView)
            hasAllergiesData = true
          }
        }

        ResourceType.Observation -> {
          val observation = entry.resource as Observation
          val date = observation.effectiveDateTimeType.valueAsString
          val resultName = observation.code.coding.firstOrNull()?.display
          val value = if (observation.hasValueCodeableConcept()) {
            observation.valueCodeableConcept.coding.firstOrNull()?.display
          } else {
            "${observation.valueQuantity.value}${observation.valueQuantity.unit}"
          }
          val category =
            (entry.resource as Observation).category.firstOrNull()?.coding?.firstOrNull()?.code
          if (resultName != null) {
            val resultsDisplay =
              "Name: $resultName \nDate/Time: $date \nValue: $value\nCategory: $category"
            val resultNameTextView = createTextView(context, resultsDisplay)
            addCardViewToTable(resultsTable, resultNameTextView)
            hasResultsData = true
          }
        }

        ResourceType.Medication -> {
          val medication = (entry.resource as Medication).code.coding
          val medicationDisplays =
            medication.joinToString("\n") { "${it.display} (${it.code}) (${it.system})" }
          val medicationTextView = createTextView(context, medicationDisplays)
          addCardViewToTable(medicationTable, medicationTextView)
          hasMedicationData = true
        }

        ResourceType.Condition -> {
          val problem = (entry.resource as Condition).code.coding
          if ((entry.resource as Condition).clinicalStatus.coding.firstOrNull()?.code == "active") {
            if (problem != null) {
              val conditionDisplay =
                problem.joinToString("\n") { "${it.display} (${it.code}) (${it.system})" }
              val problemTextView = createTextView(context, conditionDisplay)
              addCardViewToTable(problemsTable, problemTextView)
             hasProblemsData = true
            }
          }
        }

        else -> {
        }
      }
    }

    immunizationTable.visibility = if (hasImmunizationData) View.VISIBLE else View.GONE
    allergiesTable.visibility = if (hasAllergiesData) View.VISIBLE else View.GONE
    resultsTable.visibility = if (hasResultsData) View.VISIBLE else View.GONE
    medicationTable.visibility = if (hasMedicationData) View.VISIBLE else View.GONE
    problemsTable.visibility = if (hasProblemsData) View.VISIBLE else View.GONE

    addGapToCardView(immunizationTable, 30)
    addGapToCardView(allergiesTable, 30)
    addGapToCardView(problemsTable, 30)
    addGapToCardView(resultsTable, 30)
    addGapToCardView(medicationTable, 30)
    addGapToCardView(patientTable, 30)
    addGapToCardView(documentTable, 30)
  }

  private fun createTextView(context: Context, text: String): MaterialTextView {
    return MaterialTextView(context).apply {
      this.text = text
      layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
      setPadding(8, 8, 8, 8)
      gravity = Gravity.CENTER
    }
  }

  private fun addCardViewToTable(table: MaterialCardView, view: View) {
    val cardView = MaterialCardView(context)
    cardView.strokeWidth = 2
    cardView.strokeColor = Color.BLACK
    cardView.addView(view)
    // cardView.radius = resources.getDimension(R.dimen.card_corner_radius)
    cardView.isClickable = true
    cardView.isFocusable = true
    table.addView(cardView)
  }

  private fun addGapToCardView(table: MaterialCardView, gapHeight: Int) {
    val gapView = View(context)
    val params = LayoutParams(LayoutParams.MATCH_PARENT, gapHeight)
    gapView.layoutParams = params
    table.addView(gapView)
  }
}
