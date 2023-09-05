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
    addedResourcesByType.getOrPut(resourceType) { mutableListOf() }.add(resource)

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
    // narrative.statusAsString = "generated"
    return narrative
  }

  fun createCoding(code: String, display: String, system: String = "http://loinc.org"): Coding {
    val coding = Coding()
    coding.code = code
    coding.display = display
    coding.system = system
    return coding
  }

  private fun getResourceCode(resource: Resource): CodeableConcept {
    val codeableConcept = CodeableConcept()
    codeableConcept.coding = listOf(
      when (resource.resourceType) {
        ResourceType.AllergyIntolerance -> createCoding("48765-2", "Allergies and adverse reactions Document")
        ResourceType.Condition -> createCoding("11450-4", "Problem list Reported")
        ResourceType.Medication -> createCoding("10160-0", "History of Medication")
        ResourceType.Immunization -> createCoding("11369-6", "History of Immunizations")
        ResourceType.Observation -> createCoding("30954-2", "Test Results")
        else -> createCoding("12345", "Display Text", "http://your-coding-system-url.com")
      }
    )
    return codeableConcept
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
      // "Plan of Treatment"

    }
  }

  fun checkSections(sections: MutableList<SectionComponent>): Pair<MutableList<SectionComponent>, MutableList<Resource>> {
    val missingSections = mutableListOf<SectionComponent>()
    val missingResources = mutableListOf<Resource>()

    val sectionTitlesToCheck = listOf("Allergies and Intolerances", "Active Problems", "Medication")

    for (sectionTitle in sectionTitlesToCheck) {
      if (sections.none { it.title == sectionTitle }) {
        val missingResource = createMissingResource(sectionTitle)
        missingSections.add(createResourceSection(missingResource))
        missingResources.add(missingResource)
      }
    }

    return Pair(missingSections, missingResources)
  }

  private fun createMissingResource(sectionTitle: String): Resource {
    val missingResource: Resource = when (sectionTitle) {
      "Allergies and Intolerances" -> createMissingAllergyIntolerance()
      "Active Problems" -> createMissingCondition()
      else -> createMissingMedication()
    }
    return missingResource
  }

  private fun createMissingAllergyIntolerance(): Resource {
    val allergyIntolerance = AllergyIntolerance()
    allergyIntolerance.id = UUID.randomUUID().toString()
    allergyIntolerance.code = CodeableConcept().apply {
      coding.add(Coding().apply {
        system = "http://hl7.org/fhir/uv/ips/CodeSystem/absent-unknown-uv-ips"
        code = "no-allergy-info"
        display = "No information about allergies"
      })
    }
    return allergyIntolerance
  }

  private fun createMissingCondition(): Resource {
    val condition = Condition()
    condition.id = UUID.randomUUID().toString()
    condition.code = CodeableConcept().apply {
      coding.add(Coding().apply {
        system = "http://hl7.org/fhir/uv/ips/CodeSystem/absent-unknown-uv-ips"
        code = "no-problem-info"
        display = "No information about problems"
      })
    }
    return condition
  }

  private fun createMissingMedication(): Resource {
    val medication = Medication()
    medication.id = UUID.randomUUID().toString()
    medication.code = CodeableConcept().apply {
      coding.add(Coding().apply {
        system = "http://hl7.org/fhir/uv/ips/CodeSystem/absent-unknown-uv-ips"
        code = "no-medication-info"
        display = "No information about medications"
      })
    }
    return medication
  }
}