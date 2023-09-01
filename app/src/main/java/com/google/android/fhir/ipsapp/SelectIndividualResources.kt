package com.google.android.fhir.ipsapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import ca.uhn.fhir.context.FhirContext
import ca.uhn.fhir.context.FhirVersionEnum
import com.example.ipsapp.utils.DocumentUtils
import com.example.ipsapp.utils.hasCode
import com.google.android.fhir.library.DocumentGenerator
import com.google.android.fhir.library.IPSDocument
import com.google.android.fhir.library.Title
import java.io.Serializable
import org.hl7.fhir.r4.model.Resource


class SelectIndividualResources : Activity() {

  private var map = mutableMapOf<String, ArrayList<Resource>>()
  private val parser = FhirContext.forCached(FhirVersionEnum.R4).newJsonParser()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.select_individual_resources)

    val documentGenerator = DocumentGenerator()
    val docUtils = DocumentUtils()

    val checkBoxes = mutableListOf<CheckBox>()
    val checkboxTitleMap = mutableMapOf<String, String>()
    // val bundle = intent.getSerializableExtra("ipsDoc", IPSDocument::class.java)
    val containerLayout: LinearLayout = findViewById(R.id.containerLayout)

    val doc = docUtils.readFileFromAssets(this, "immunizationBundle.json")
    val bundle = IPSDocument(parser.parseResource(doc) as org.hl7.fhir.r4.model.Bundle)
    bundle.titles = ArrayList(docUtils.getTitlesFromIpsDoc(doc).map { Title(it) })

    documentGenerator.displayOptions(this, bundle, checkBoxes, checkboxTitleMap, containerLayout, map)


    val submitButton = findViewById<Button>(R.id.goToCreatePasscode)
    submitButton.setOnClickListener {
      val selectedCheckedValuesWithTitles = checkBoxes.filter { it.isChecked }
        .map { checkBox ->
          val title = checkboxTitleMap[checkBox.text.toString()]
          val value = checkBox.text.toString()
          Pair(title, value)
        }

      val outputArray = selectedCheckedValuesWithTitles.flatMap { (title, value) ->
        map[title]?.filter { obj ->
          obj.hasCode().first?.coding?.any { it.display == value } == true
        } ?: emptyList()
      }

      // val outputArray = selectedCheckedValuesWithTitles.mapNotNull { (title, value) ->
      //   map[title]?.filter { obj ->
      //     obj.hasCode().first?.coding?.any { it.display == value } == true
      //   }
      // }
      //
      // println("Selected values with titles: $selectedCheckedValuesWithTitles")
      // println("Output array: $outputArray")

      val ipsDoc = documentGenerator.generateIPS(outputArray)
      val i = Intent(this@SelectIndividualResources, CreatePasscode::class.java)
      // val stringArrayLists: ArrayList<ArrayList<String>> = ArrayList()
      // for (jsonArrayList in outputArray) {
      //   println(jsonArrayList)
      //   val stringList = ArrayList<String>()
      //   for (jsonObject in jsonArrayList) {
      //     // Convert JSONObject to String and add it to the stringList
      //     stringList.add(jsonObject.toString())
      //   }
      //   stringArrayLists.add(stringList)
      // }
      // val flattenedList: ArrayList<String> = ArrayList()
      // for (innerList in stringArrayLists) {
      //   flattenedList.addAll(innerList)
      // }
      i.putExtra("ipsDoc", ipsDoc as Serializable)
      startActivity(i)
    }
  }
}
