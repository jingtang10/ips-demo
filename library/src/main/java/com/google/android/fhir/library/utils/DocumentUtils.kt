package com.google.android.fhir.library.utils

import android.content.Context
import org.hl7.fhir.r4.model.AllergyIntolerance
import org.hl7.fhir.r4.model.CodeableConcept
import org.hl7.fhir.r4.model.Coding
import org.hl7.fhir.r4.model.Condition
import org.hl7.fhir.r4.model.Immunization
import org.hl7.fhir.r4.model.Medication
import org.hl7.fhir.r4.model.Observation
import org.hl7.fhir.r4.model.Resource

class DocumentUtils {

  fun shouldExcludeResource(title: String, resource: Resource): Boolean {
    val code = resource.hasCode().second
    return (title == "History of Past Illness" && code == "active") || ((title == "Active Problems" || title == "Allergies and Intolerances") && code != "active")
  }

  fun getSearchingCondition(resource: String, resourceType: String): Boolean {
    return when (resource) {
      "Allergies and Intolerances" -> resourceType == "AllergyIntolerance"
      "Medication" -> resourceType == "Medication"
      "Active Problems" -> resourceType == "Condition"
      "Immunizations" -> resourceType == "Immunization"
      "Results" -> resourceType == "Observation"

      "History of Past Illness" -> resourceType == "Condition" // inside div
      "Plan of Treatment" -> false // inside div

      // titles have to change
      "procedure history" -> false
      "medical devices" -> false
      else -> false
    }
  }

  fun readFileFromAssets(context: Context, filename: String): String {
    return context.assets.open(filename).bufferedReader().use {
      it.readText()
    }
  }

  fun getCodings(res: Resource): MutableList<Coding>? {
    return res.hasCode().first?.coding
  }
}

fun Resource.hasCode(): Pair<CodeableConcept?, String?> {
  return when (this) {
    is AllergyIntolerance -> Pair(code, clinicalStatus.coding.firstOrNull()?.code)
    is Condition -> Pair(code, clinicalStatus.coding.firstOrNull()?.code)
    is Medication -> Pair(code, null)
    is Observation -> Pair(code, null)
    is Immunization -> Pair(vaccineCode, "")
    else -> Pair(null, null)
  }
}