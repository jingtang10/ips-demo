package com.example.library

import ca.uhn.fhir.context.FhirContext
import ca.uhn.fhir.context.FhirVersionEnum
import com.google.android.fhir.library.DocumentGenerator
import org.hl7.fhir.r4.model.CodeableConcept
import org.hl7.fhir.r4.model.Coding
import org.hl7.fhir.r4.model.HumanName
import org.hl7.fhir.r4.model.Identifier
import org.hl7.fhir.r4.model.Observation
import org.hl7.fhir.r4.model.Patient
import org.hl7.fhir.r4.model.Resource
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class DocGenTest {

  val docGenerator = DocumentGenerator()
  private val parser = FhirContext.forCached(FhirVersionEnum.R4).newJsonParser()
  fun createSampleResources(): List<Resource> {
    val resources = mutableListOf<Resource>()

    // Create a Patient resource
    val patient = Patient()
    patient.id = "123"
    patient.identifier.add(Identifier().setSystem("http://example.com").setValue("12345"))
    patient.name.add(HumanName().addGiven("John").setFamily("Doe"))
    resources.add(patient)

    // Create an Observation resource
    val observation = Observation()
    observation.id = "456"
    observation.status = Observation.ObservationStatus.FINAL
    observation.code = CodeableConcept().addCoding(
      Coding()
        .setSystem("http://loinc.org")
        .setCode("12345")
        .setDisplay("Example Observation")
    )
    // observation.valueQuantity = Quantity().setValue(42.0).setUnit("mmHg")
    resources.add(observation)

    // Add more resources here as needed...

    return resources
  }
  @Test
  fun testDocGen() {
    val doc = docGenerator.generateIPS(createSampleResources())
    println(parser.encodeResourceToString(doc.document))
  }
}