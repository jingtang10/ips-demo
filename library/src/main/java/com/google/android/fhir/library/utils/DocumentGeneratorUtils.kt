package com.google.android.fhir.library.utils

import org.hl7.fhir.r4.model.CodeableConcept
import org.hl7.fhir.r4.model.Coding
import org.hl7.fhir.r4.model.Composition.SectionComponent
import org.hl7.fhir.r4.model.Narrative
import org.hl7.fhir.r4.model.Reference
import org.hl7.fhir.r4.model.Resource
import org.hl7.fhir.r4.model.ResourceType
// import ca.uhn.fhir.m

class DocumentGeneratorUtils {

  fun createResourceSection(resource: Resource): SectionComponent {
    val section = SectionComponent()


    // Set section title, code, and text (you can customize these as needed)
    section.title = getResourceTitle(resource)
    section.code = getResourceCode(resource)
    section.text = getResourceText(resource)

    // Set the reference to the FHIR resource within the section
    section.entry.add(Reference().setReference(resource.idElement.toVersionless().toString()))
    return section
  }

  private fun getResourceText(resource: Resource): Narrative {
    val narrative = Narrative()
    narrative.statusAsString = "generated"
    return narrative
  }

  private fun getResourceCode(resource: Resource): CodeableConcept {
    val codeableConcept = CodeableConcept()
    // Create and set coding information within the codeableConcept
    val coding = Coding()
    return when(resource.resourceType) {
      ResourceType.AllergyIntolerance -> {
        coding.system = "http://loinc.org"
        coding.code = "48765-2"
        coding.display = "Allergies and adverse reactions Document"
        codeableConcept.coding = listOf(coding)
        codeableConcept
      }
      ResourceType.Condition -> {
        coding.system = "http://loinc.org"
        coding.code = "11450-4"
        coding.display = "Problem list Reported"
        codeableConcept.coding = listOf(coding)
        codeableConcept
      }
      ResourceType.Medication -> {
        coding.system = "http://loinc.org"
        coding.code = "10160-0"
        coding.display = "History of Medication"
        codeableConcept.coding = listOf(coding)
        codeableConcept
      }
      ResourceType.Immunization -> {
        coding.system = "http://loinc.org"
        coding.code = "11369-6"
        coding.display = "History of Immunizations"
        codeableConcept.coding = listOf(coding)
        codeableConcept
      }
      ResourceType.Observation -> {
        coding.system = "http://loinc.org"
        coding.code = "30954-2"
        coding.display = "Test Results"
        codeableConcept.coding = listOf(coding)
        codeableConcept
      }
      else -> {
        coding.system = "http://your-coding-system-url.com"
        coding.code = "12345"
        coding.display = "Display Text"
        codeableConcept.coding = listOf(coding)
        codeableConcept
      }
      // "History of Past Illness"
      // "Plan of Treatment"

    }

  }

  private fun getResourceTitle(resource: Resource): String? {
    return when(resource.resourceType) {
      ResourceType.AllergyIntolerance -> "Allergies and Intolerances"
      ResourceType.Condition -> "Active Problem"
      ResourceType.Medication -> "Medication"
      ResourceType.Immunization -> "Immunizations"
      ResourceType.Observation -> "Results"
      else -> null
      // "History of Past Illness"
      // "Plan of Treatment"

    }
  }
}