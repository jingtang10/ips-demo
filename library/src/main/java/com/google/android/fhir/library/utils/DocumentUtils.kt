package com.example.ipsapp.utils

import android.content.Context
import ca.uhn.fhir.context.FhirContext
import ca.uhn.fhir.context.FhirVersionEnum
import org.hl7.fhir.r4.model.AllergyIntolerance
import org.hl7.fhir.r4.model.Bundle
import org.hl7.fhir.r4.model.CodeableConcept
import org.hl7.fhir.r4.model.Composition
import org.hl7.fhir.r4.model.Condition
import org.hl7.fhir.r4.model.Immunization
import org.hl7.fhir.r4.model.Medication
import org.hl7.fhir.r4.model.Observation
import org.hl7.fhir.r4.model.Resource
import org.hl7.fhir.r4.model.ResourceType

class DocumentUtils {

  private val parser = FhirContext.forCached(FhirVersionEnum.R4).newJsonParser()

  fun getTitlesFromIpsDoc(doc: String): List<String> {
    val bundle = parser.parseResource(doc) as Bundle
    val composition =
      bundle.entry.firstOrNull { it.resource.resourceType == ResourceType.Composition }?.resource as Composition
    return composition.section.map { it.title }
  }

  fun getDataFromDoc(
    doc: String,
    title: String,
    map: MutableMap<String, ArrayList<Resource>>,
  ): MutableMap<String, ArrayList<Resource>> {
    val bundle = parser.parseResource(doc) as Bundle

    val filteredResources = bundle.entry.map { it.resource }.filter { resource ->
        val resourceType = resource.resourceType.toString()
        getSearchingCondition(title, resourceType)
      }
    val resourceList = filteredResources.filterNot { shouldExcludeResource(title, it) }
    map[title] = ArrayList(resourceList)
    return map
  }

  private fun shouldExcludeResource(title: String, resource: Resource): Boolean {
    val code = resource.hasCode().second
    return (title == "History of Past Illness" && code == "active") || ((title == "Active Problems" || title == "Allergies and Intolerances") && code != "active")
  }

  private fun getSearchingCondition(resource: String, resourceType: String): Boolean {
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