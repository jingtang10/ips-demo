package com.google.android.fhir.library.utils

import org.hl7.fhir.r4.model.CodeableConcept
import org.hl7.fhir.r4.model.Coding
import org.hl7.fhir.r4.model.Composition.SectionComponent
import org.hl7.fhir.r4.model.Narrative
import org.hl7.fhir.r4.model.Reference
import org.hl7.fhir.r4.model.Resource

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

  private fun getResourceText(resource: Resource): Narrative? {
    val narrative = Narrative()
    // Set the text value for the narrative based on your resource
    narrative.divAsString = "aaaaa"
    return narrative
  }

  private fun getResourceCode(resource: Resource): CodeableConcept? {
    val codeableConcept = CodeableConcept()
    // Create and set coding information within the codeableConcept
    val coding = Coding()
    coding.system = "http://your-coding-system-url.com"
    coding.code = "12345"
    coding.display = "Display Text"
    codeableConcept.coding = listOf(coding)
    return codeableConcept
  }

  private fun getResourceTitle(resource: Resource): String? {
    return "title"
  }
}