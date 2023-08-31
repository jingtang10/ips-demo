package com.example.library

import ca.uhn.fhir.context.FhirContext
import ca.uhn.fhir.context.FhirVersionEnum
import com.google.android.fhir.library.DocumentGenerator
import org.hl7.fhir.r4.model.AllergyIntolerance
import org.hl7.fhir.r4.model.CodeableConcept
import org.hl7.fhir.r4.model.Coding
import org.hl7.fhir.r4.model.HumanName
import org.hl7.fhir.r4.model.Identifier
import org.hl7.fhir.r4.model.Medication
import org.hl7.fhir.r4.model.Observation
import org.hl7.fhir.r4.model.Patient
import org.hl7.fhir.r4.model.Reference
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

  val res1 = "{\n" +
    "      \"resourceType\" : \"Observation\",\n" +
    "      \"id\" : \"b4916505-a06b-460c-9be8-011609282457\",\n" +
    "      \"text\" : {\n" +
    "        \"status\" : \"generated\",\n" +
    "        \"div\" : \"<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\"><p><b>Generated Narrative: Observation</b><a name=\\\"b4916505-a06b-460c-9be8-011609282457\\\"> </a></p><div style=\\\"display: inline-block; background-color: #d9e0e7; padding: 6px; margin: 4px; border: 1px solid #8da1b4; border-radius: 5px; line-height: 60%\\\"><p style=\\\"margin-bottom: 0px\\\">Resource Observation &quot;b4916505-a06b-460c-9be8-011609282457&quot; </p></div><p><b>status</b>: final</p><p><b>category</b>: Laboratory <span style=\\\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\\\"> (<a href=\\\"http://terminology.hl7.org/5.0.0/CodeSystem-observation-category.html\\\">Observation Category Codes</a>#laboratory)</span></p><p><b>code</b>: E Ab [Presence] in Serum or Plasma <span style=\\\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\\\"> (<a href=\\\"https://loinc.org/\\\">LOINC</a>#1018-1)</span></p><p><b>subject</b>: <a href=\\\"#Patient_2b90dd2b-2dab-4c75-9bb9-a355e07401e8\\\">See above (Patient/2b90dd2b-2dab-4c75-9bb9-a355e07401e8)</a></p><p><b>effective</b>: 2015-10-10 09:35:00+0100</p><p><b>performer</b>: <a href=\\\"#Organization_45a5c5b1-4ec1-4d60-b4b2-ff5a84a41fd7\\\">See above (Organization/45a5c5b1-4ec1-4d60-b4b2-ff5a84a41fd7)</a></p><p><b>value</b>: Positive <span style=\\\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\\\"> (<a href=\\\"https://browser.ihtsdotools.org/\\\">SNOMED CT</a>#10828004)</span></p></div>\"\n" +
    "      },\n" +
    "      \"status\" : \"final\",\n" +
    "      \"category\" : [{\n" +
    "        \"coding\" : [{\n" +
    "          \"system\" : \"http://terminology.hl7.org/CodeSystem/observation-category\",\n" +
    "          \"code\" : \"laboratory\"\n" +
    "        }]\n" +
    "      }],\n" +
    "      \"code\" : {\n" +
    "        \"coding\" : [{\n" +
    "          \"system\" : \"http://loinc.org\",\n" +
    "          \"code\" : \"1018-1\",\n" +
    "          \"display\" : \"E Ab [Presence] in Serum or Plasma\"\n" +
    "        }]\n" +
    "      },\n" +
    "      \"subject\" : {\n" +
    "        \"reference\" : \"Patient/2b90dd2b-2dab-4c75-9bb9-a355e07401e8\"\n" +
    "      },\n" +
    "      \"effectiveDateTime\" : \"2015-10-10T09:35:00+01:00\",\n" +
    "      \"performer\" : [{\n" +
    "        \"reference\" : \"Organization/45a5c5b1-4ec1-4d60-b4b2-ff5a84a41fd7\"\n" +
    "      }],\n" +
    "      \"valueCodeableConcept\" : {\n" +
    "        \"coding\" : [{\n" +
    "          \"system\" : \"http://snomed.info/sct\",\n" +
    "          \"code\" : \"10828004\",\n" +
    "          \"display\" : \"Positive\"\n" +
    "        }]\n" +
    "      }\n" +
    "    }"

  val res2 =
    "     { \"resourceType\" : \"Organization\",\n" +
    "      \"id\" : \"45a5c5b1-4ec1-4d60-b4b2-ff5a84a41fd7\",\n" +
    "      \"text\" : {\n" +
    "        \"status\" : \"generated\",\n" +
    "        \"div\" : \"<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\"><p><b>Generated Narrative: Organization</b><a name=\\\"45a5c5b1-4ec1-4d60-b4b2-ff5a84a41fd7\\\"> </a></p><div style=\\\"display: inline-block; background-color: #d9e0e7; padding: 6px; margin: 4px; border: 1px solid #8da1b4; border-radius: 5px; line-height: 60%\\\"><p style=\\\"margin-bottom: 0px\\\">Resource Organization &quot;45a5c5b1-4ec1-4d60-b4b2-ff5a84a41fd7&quot; </p></div><p><b>active</b>: true</p><p><b>type</b>: Other <span style=\\\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\\\"> (<a href=\\\"http://terminology.hl7.org/5.0.0/CodeSystem-organization-type.html\\\">Organization type</a>#other)</span></p><p><b>name</b>: Laboratoire de charme</p></div>\"\n" +
    "      },\n" +
    "      \"active\" : true,\n" +
    "      \"type\" : [{\n" +
    "        \"coding\" : [{\n" +
    "          \"system\" : \"http://terminology.hl7.org/CodeSystem/organization-type\",\n" +
    "          \"code\" : \"other\"\n" +
    "        }]\n" +
    "      }],\n" +
    "      \"name\" : \"Laboratoire de charme\"\n" +
    "    }\n" +
    "  },\n" +
    "  {\n" +
    "    \"fullUrl\" : \"urn:uuid:aa11a2be-3e36-4be7-b58a-6fc3dace2741\",\n" +
    "    \"resource\" : {\n" +
    "      \"resourceType\" : \"Observation\",\n" +
    "      \"id\" : \"aa11a2be-3e36-4be7-b58a-6fc3dace2741\",\n" +
    "      \"text\" : {\n" +
    "        \"status\" : \"generated\",\n" +
    "        \"div\" : \"<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\"><p><b>Generated Narrative: Observation</b><a name=\\\"aa11a2be-3e36-4be7-b58a-6fc3dace2741\\\"> </a></p><div style=\\\"display: inline-block; background-color: #d9e0e7; padding: 6px; margin: 4px; border: 1px solid #8da1b4; border-radius: 5px; line-height: 60%\\\"><p style=\\\"margin-bottom: 0px\\\">Resource Observation &quot;aa11a2be-3e36-4be7-b58a-6fc3dace2741&quot; </p></div><p><b>status</b>: final</p><p><b>category</b>: Laboratory <span style=\\\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\\\"> (<a href=\\\"http://terminology.hl7.org/5.0.0/CodeSystem-observation-category.html\\\">Observation Category Codes</a>#laboratory)</span></p><p><b>code</b>: ABO and Rh group [Type] in Blood <span style=\\\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\\\"> (<a href=\\\"https://loinc.org/\\\">LOINC</a>#882-1)</span></p><p><b>subject</b>: <a href=\\\"#Patient_2b90dd2b-2dab-4c75-9bb9-a355e07401e8\\\">See above (Patient/2b90dd2b-2dab-4c75-9bb9-a355e07401e8)</a></p><p><b>effective</b>: 2015-10-10 09:15:00+0100</p><p><b>performer</b>: <a href=\\\"#Organization_45a5c5b1-4ec1-4d60-b4b2-ff5a84a41fd7\\\">See above (Organization/45a5c5b1-4ec1-4d60-b4b2-ff5a84a41fd7)</a></p><p><b>value</b>: Blood group A Rh(D) positive <span style=\\\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\\\"> (<a href=\\\"https://browser.ihtsdotools.org/\\\">SNOMED CT</a>#278149003)</span></p></div>\"\n" +
    "      },\n" +
    "      \"status\" : \"final\",\n" +
    "      \"category\" : [{\n" +
    "        \"coding\" : [{\n" +
    "          \"system\" : \"http://terminology.hl7.org/CodeSystem/observation-category\",\n" +
    "          \"code\" : \"laboratory\"\n" +
    "        }]\n" +
    "      }],\n" +
    "      \"code\" : {\n" +
    "        \"coding\" : [{\n" +
    "          \"system\" : \"http://loinc.org\",\n" +
    "          \"code\" : \"882-1\",\n" +
    "          \"display\" : \"ABO and Rh group [Type] in Blood\"\n" +
    "        }]\n" +
    "      },\n" +
    "      \"subject\" : {\n" +
    "        \"reference\" : \"Patient/2b90dd2b-2dab-4c75-9bb9-a355e07401e8\"\n" +
    "      },\n" +
    "      \"effectiveDateTime\" : \"2015-10-10T09:15:00+01:00\",\n" +
    "      \"performer\" : [{\n" +
    "        \"reference\" : \"Organization/45a5c5b1-4ec1-4d60-b4b2-ff5a84a41fd7\"\n" +
    "      }],\n" +
    "      \"valueCodeableConcept\" : {\n" +
    "        \"coding\" : [{\n" +
    "          \"system\" : \"http://snomed.info/sct\",\n" +
    "          \"code\" : \"278149003\",\n" +
    "          \"display\" : \"Blood group A Rh(D) positive\"\n" +
    "        }]\n" +
    "      }}"

  val list = listOf<Resource>(parser.parseResource(res1) as Resource)
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

    // Create an AllergyIntolerance resource
    val allergy = AllergyIntolerance()
    allergy.id = "789"
    allergy.clinicalStatus = CodeableConcept().addCoding(
      Coding()
        .setSystem("http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical")
        .setCode("active")
        .setDisplay("allergy")
    )
    allergy.type = AllergyIntolerance.AllergyIntoleranceType.ALLERGY
    // allergy.category =
    allergy.patient = Reference(patient) // Link the allergy to the patient
    allergy.criticality = AllergyIntolerance.AllergyIntoleranceCriticality.HIGH
    // allergy.category = listOf(Enumeration(AllergyIntolerance.AllergyIntoleranceCategory.FOOD))
    resources.add(allergy)

    val allergy2 = AllergyIntolerance()
    allergy2.id = "78910"
    allergy2.clinicalStatus = CodeableConcept().addCoding(
      Coding()
        .setSystem("http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical")
        .setCode("active")
        .setDisplay("allergy")
    )
    allergy2.type = AllergyIntolerance.AllergyIntoleranceType.ALLERGY
    // allergy.category =
    allergy2.patient = Reference(patient) // Link the allergy to the patient
    allergy2.criticality = AllergyIntolerance.AllergyIntoleranceCriticality.HIGH
    // allergy.category = listOf(Enumeration(AllergyIntolerance.AllergyIntoleranceCategory.FOOD))
    resources.add(allergy2)

    // Create a Medication resource
    val medication = Medication()
    medication.id = "101"
    medication.code = CodeableConcept().addCoding(
      Coding()
        .setSystem("http://www.nlm.nih.gov/research/umls/rxnorm")
        .setCode("198103")
        .setDisplay("Aspirin 81 MG Oral Tablet")
    )
    resources.add(medication)

    // Add more resources here as needed...
    return resources
  }

  @Test
  fun testDocGen() {
    // val doc = docGenerator.generateIPS(createSampleResources())
    val doc = docGenerator.generateIPS(list)
    println(parser.encodeResourceToString(doc.document))
  }
}