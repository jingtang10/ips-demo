package com.google.android.fhir.library.utils

import java.util.UUID
import org.hl7.fhir.r4.model.AllergyIntolerance
import org.hl7.fhir.r4.model.CodeableConcept
import org.hl7.fhir.r4.model.Coding
import org.hl7.fhir.r4.model.Composition.SectionComponent
import org.hl7.fhir.r4.model.Condition
import org.hl7.fhir.r4.model.Medication
import org.hl7.fhir.r4.model.Narrative
import org.hl7.fhir.r4.model.Reference
import org.hl7.fhir.r4.model.Resource
import org.hl7.fhir.r4.model.ResourceType

// import ca.uhn.fhir.m

class DocumentGeneratorUtils {

  private val addedResourcesByType: MutableMap<String, MutableList<Resource>> = mutableMapOf()

  fun createResourceSection(resource: Resource): SectionComponent {
    val section = SectionComponent()

    section.title = getResourceTitle(resource)
    section.code = getResourceCode(resource)
    section.text = getResourceText(resource)

    val resourceType = resource.resourceType.toString()
    addedResourcesByType
      .getOrPut(resourceType) { mutableListOf() }
      .add(resource)

    section.entry.clear()
    addedResourcesByType[resourceType]?.distinctBy { it.idElement.toVersionless() }
      ?.forEach { addedResource ->
        val fullId = addedResource.idElement.toVersionless()
        val baseId = addedResource.idElement.toVersionless().baseUrl
        val id = if (baseId == null) {
          fullId
        } else {
          fullId.toString().removePrefix(baseId)
        }
        section.entry.add(Reference().setReference(id.toString()))
      }
    return section
  }

  private fun getResourceText(resource: Resource): Narrative {
    val narrative = Narrative()
    narrative.statusAsString = "generated"
    return narrative
  }

  private fun getResourceCode(resource: Resource): CodeableConcept {
    val coding = Coding()
    coding.system = "http://loinc.org"
    val codeableConcept = CodeableConcept()
    codeableConcept.coding = listOf(coding)

    return when (resource.resourceType) {
      ResourceType.AllergyIntolerance -> {
        coding.code = "48765-2"
        coding.display = "Allergies and adverse reactions Document"
        codeableConcept
      }

      ResourceType.Condition -> {
        coding.code = "11450-4"
        coding.display = "Problem list Reported"
        codeableConcept
      }

      ResourceType.Medication -> {
        coding.code = "10160-0"
        coding.display = "History of Medication"
        codeableConcept
      }

      ResourceType.Immunization -> {
        coding.code = "11369-6"
        coding.display = "History of Immunizations"
        codeableConcept
      }

      ResourceType.Observation -> {
        coding.code = "30954-2"
        coding.display = "Test Results"
        codeableConcept
      }

      else -> {
        coding.system = "http://your-coding-system-url.com"
        coding.code = "12345"
        coding.display = "Display Text"
        codeableConcept
      }
    }
  }

  fun getResourceTitle(resource: Resource): String? {
    return when (resource.resourceType) {
      ResourceType.AllergyIntolerance -> {
        val allergy = resource as AllergyIntolerance
        when (allergy.clinicalStatus.coding.firstOrNull()?.code) {
          "active" -> "Allergies and Intolerances"
          else -> "History of Past Illness"
        }
      }

      ResourceType.Condition -> {
        val condition = resource as Condition
        when (condition.clinicalStatus.coding.firstOrNull()?.code) {
          "active" -> "Active Problems"
          else -> "History of Past Illness"
        }
      }

      ResourceType.Medication -> "Medication"
      ResourceType.Immunization -> "Immunizations"
      ResourceType.Observation -> "Results"
      else -> null
      // "History of Past Illness"
      // "Plan of Treatment"

    }
  }

  fun checkSections(sections: MutableList<SectionComponent>): Pair<MutableList<SectionComponent>, MutableList<Resource>> {
    val missingSections = mutableListOf<SectionComponent>()
    val missingResources = mutableListOf<Resource>()
    if (sections.find { it.title == "Allergies and Intolerances" } == null) {
      val allergyIntolerance = AllergyIntolerance()
      allergyIntolerance.id = UUID.randomUUID().toString()
      allergyIntolerance.code = CodeableConcept().apply {
        coding.add(Coding().apply {
          system = "http://hl7.org/fhir/uv/ips/CodeSystem/absent-unknown-uv-ips"
          code = "no-allergy-info"
          display = "No information about allergies"
        })
      }
      missingSections.add(createResourceSection(allergyIntolerance))
      missingResources.add(allergyIntolerance)
    }
    if (sections.find { it.title == "Active Problems" } == null) {
      val condition = Condition()
      condition.id = UUID.randomUUID().toString()
      condition.code = CodeableConcept().apply {
        coding.add(Coding().apply {
          system = "http://hl7.org/fhir/uv/ips/CodeSystem/absent-unknown-uv-ips"
          code = "no-problem-info"
          display = "No information about problems"
        })
      }
      missingSections.add(createResourceSection(condition))
      missingResources.add(condition)
    }
    if (sections.find { it.title == "Medication" } == null) {
      val medication = Medication()
      medication.id = UUID.randomUUID().toString()
      medication.code = CodeableConcept().apply {
        coding.add(Coding().apply {
          system = "http://hl7.org/fhir/uv/ips/CodeSystem/absent-unknown-uv-ips"
          code = "no-medication-info"
          display = "No information about medications"
        })
      }
      missingSections.add(createResourceSection(medication))
      missingResources.add(medication)
    }
    return Pair(missingSections, missingResources)
  }
}