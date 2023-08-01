package com.example.ipsapp

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.DatePicker
import android.widget.EditText
import androidx.annotation.RequiresApi
import java.nio.charset.StandardCharsets
import java.security.SecureRandom
import java.util.Base64
import java.util.Calendar
import org.json.JSONObject

class CreatePasscode : Activity() {

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
  @RequiresApi(Build.VERSION_CODES.O)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.create_passcode)

    var shlink = ""

    val datePicker = findViewById<DatePicker>(R.id.datePicker)
    var expirationDate = ""

    // Do I need to include time with this?
    val today: Calendar = Calendar.getInstance()
    datePicker.minDate = today.timeInMillis

    val submitResourcesButton = findViewById<Button>(R.id.generateSHL)
    val checkboxDate = findViewById<CheckBox>(R.id.checkboxDate)

    /*
     When the submit button is pressed, the state of the checkbox is checked, and the passcode
     and expiration date are added to the intent to be passed into the next activity.
     They are empty strings if they haven't been inputted
    */
    submitResourcesButton.setOnClickListener {
      val i = Intent(this@CreatePasscode, ViewSHL::class.java)

      val shlinkPayload = constructSHLinkPayload(file, "My Data", "", "your_decryption_key")
      val shlinkBare = "shlink:/${base64UrlEncode(shlinkPayload)}"
      val viewerUrl = "https://viewer.example.org#"
      var link = viewerUrl + shlinkBare

      // println(link)


      val passcodeField = findViewById<EditText>(R.id.passcode).text.toString()
      if (!checkboxDate.isChecked) {
        expirationDate = ""
      }
      i.putExtra("expirationDate", expirationDate)
      i.putExtra("passcode", passcodeField)
      Log.d("passcode", passcodeField)
      Log.d("date", expirationDate)
      Log.d("link", link)
      startActivity(i)
    }

    // Set the initial state of the DatePicker based on the Checkbox state
    datePicker.isEnabled = checkboxDate.isChecked
    checkboxDate.setOnCheckedChangeListener { _, isChecked ->
      datePicker.isEnabled = isChecked
    }

    // updates the variable to the selected date
    datePicker.setOnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
      expirationDate = "$year-${monthOfYear + 1}-$dayOfMonth"
    }
  }

  @RequiresApi(Build.VERSION_CODES.O)
  fun constructSHLinkPayload(manifestUrl: String, label: String?, flags: String?, key: String): String {
    val payloadObject = JSONObject()
    payloadObject.put("url", manifestUrl)
    payloadObject.put("key", key)

    if (flags != "") {
      payloadObject.put("flag", flags)
    }

    if (label != null) {
      payloadObject.put("label", label)
    }

    // Optional: Add 'exp' and 'v' properties if needed.

    return payloadObject.toString()
  }

  @RequiresApi(Build.VERSION_CODES.O)
  fun base64UrlEncode(data: String): String {
    val bytes = data.toByteArray(StandardCharsets.UTF_8)
    return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
  }

@RequiresApi(Build.VERSION_CODES.O)
  fun generateManifestUrl(): String {
    val random = SecureRandom()
    val randomBytes = ByteArray(32)
    random.nextBytes(randomBytes)
    val base64Url = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes)
    return "https://shl.example.org/manifests/$base64Url/manifest.json"
  }
}