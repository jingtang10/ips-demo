package com.example.ipsapp

import android.app.Activity
import android.os.Bundle
import android.widget.TextView
import com.example.IpsApp.R

class ViewSHL : Activity() {

  // Need to encode and compress into JWE and JWT tokens here
  val file = "{\n" +
    "  \"resourceType\" : \"AllergyIntolerance\",\n" +
    "  \"id\" : \"allergyintolerance-with-abatement\",\n" +
    "  \"text\" : {\n" +
    "    \"status\" : \"extensions\",\n" +
    "    \"div\" : \"<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\"><p><b>Generated Narrative: AllergyIntolerance</b><a name=\\\"allergyintolerance-with-abatement\\\"> </a></p><div style=\\\"display: inline-block; background-color: #d9e0e7; padding: 6px; margin: 4px; border: 1px solid #8da1b4; border-radius: 5px; line-height: 60%\\\"><p style=\\\"margin-bottom: 0px\\\">Resource AllergyIntolerance &quot;allergyintolerance-with-abatement&quot; </p></div><p><b>Allergy abatement date</b>: 2010</p><p><b>clinicalStatus</b>: Resolved <span style=\\\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\\\"> (<a href=\\\"http://terminology.hl7.org/5.0.0/CodeSystem-allergyintolerance-clinical.html\\\">AllergyIntolerance Clinical Status Codes</a>#resolved)</span></p><p><b>verificationStatus</b>: Confirmed <span style=\\\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\\\"> (<a href=\\\"http://terminology.hl7.org/5.0.0/CodeSystem-allergyintolerance-verification.html\\\">AllergyIntolerance Verification Status</a>#confirmed)</span></p><p><b>code</b>: Ragweed pollen <span style=\\\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\\\"> (<a href=\\\"https://browser.ihtsdotools.org/\\\">SNOMED CT</a>#256303006)</span></p><p><b>patient</b>: <a href=\\\"Patient-66033.html\\\">Patient/66033</a> &quot; LUX-BRENNARD&quot;</p><p><b>onset</b>: </p></div>\"\n" +
    "  },\n" +
    "  \"extension\" : [{\n" +
    "    \"url\" : \"http://hl7.org/fhir/uv/ips/StructureDefinition/abatement-dateTime-uv-ips\",\n" +
    "    \"valueDateTime\" : \"2010\"\n" +
    "  }],\n" +
    "  \"clinicalStatus\" : {\n" +
    "    \"coding\" : [{\n" +
    "      \"system\" : \"http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical\",\n" +
    "      \"code\" : \"resolved\"\n" +
    "    }]\n" +
    "  },\n" +
    "  \"verificationStatus\" : {\n" +
    "    \"coding\" : [{\n" +
    "      \"system\" : \"http://terminology.hl7.org/CodeSystem/allergyintolerance-verification\",\n" +
    "      \"code\" : \"confirmed\"\n" +
    "    }]\n" +
    "  },\n" +
    "  \"code\" : {\n" +
    "    \"coding\" : [{\n" +
    "      \"system\" : \"http://snomed.info/sct\",\n" +
    "      \"code\" : \"256303006\",\n" +
    "      \"display\" : \"Ragweed pollen\"\n" +
    "    }]\n" +
    "  },\n" +
    "  \"patient\" : {\n" +
    "    \"reference\" : \"Patient/66033\"\n" +
    "  },\n" +
    "  \"_onsetDateTime\" : {\n" +
    "    \"extension\" : [{\n" +
    "      \"url\" : \"http://hl7.org/fhir/StructureDefinition/data-absent-reason\",\n" +
    "      \"valueCode\" : \"unknown\"\n" +
    "    }]\n" +
    "  }\n" +
    "}"

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.view_shl)

    val passcode:String = intent.getStringExtra("passcode").toString()
    val expirationDate:String = intent.getStringExtra("expirationDate").toString()

    val passcodeField = findViewById<TextView>(R.id.passcode)
    val expirationDateField = findViewById<TextView>(R.id.expirationDate)
    passcodeField.text = passcode
    expirationDateField.text = expirationDate

  }

}