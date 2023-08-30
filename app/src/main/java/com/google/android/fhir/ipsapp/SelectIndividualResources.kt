package com.google.android.fhir.ipsapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import com.example.ipsapp.utils.DocumentUtils
import com.example.ipsapp.utils.hasCode
import com.google.android.fhir.library.DocumentGenerator
import com.google.android.fhir.library.IPSDocument
import org.hl7.fhir.r4.model.Resource


class SelectIndividualResources : Activity() {

  private val docUtils = DocumentUtils()
  private var map = mutableMapOf<String, ArrayList<Resource>>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.select_individual_resources)

    val documentGenerator = DocumentGenerator()

    val checkBoxes = mutableListOf<CheckBox>()
    val checkboxTitleMap = mutableMapOf<String, String>()
    val bundle = intent.getSerializableExtra("ipsDoc", IPSDocument::class.java)
    val containerLayout: LinearLayout = findViewById(R.id.containerLayout)

    documentGenerator.displayOptions(this, bundle, checkBoxes, checkboxTitleMap, containerLayout, map)


    val submitButton = findViewById<Button>(R.id.goToCreatePasscode)
    submitButton.setOnClickListener {
      val selectedCheckedValuesWithTitles = checkBoxes.filter { it.isChecked }
        .map { checkBox ->
          val title = checkboxTitleMap[checkBox.text.toString()]
          val value = checkBox.text.toString()
          Pair(title, value)
        }

      val outputArray = selectedCheckedValuesWithTitles.mapNotNull { (title, value) ->
        map[title]?.filter { obj ->
          obj.hasCode().first?.coding?.any { it.display == value } == true
        }
      }

      println("Selected values with titles: $selectedCheckedValuesWithTitles")
      println("Output array: $outputArray")


      val i = Intent(this@SelectIndividualResources, CreatePasscode::class.java)
      val stringArrayLists: ArrayList<ArrayList<String>> = ArrayList()
      for (jsonArrayList in outputArray) {
        val stringList = ArrayList<String>()
        for (jsonObject in jsonArrayList) {
          // Convert JSONObject to String and add it to the stringList
          stringList.add(jsonObject.toString())
        }
        stringArrayLists.add(stringList)
      }
      val flattenedList: ArrayList<String> = ArrayList()
      for (innerList in stringArrayLists) {
        flattenedList.addAll(innerList)
      }
      i.putStringArrayListExtra("codingList", flattenedList)
      startActivity(i)
    }
  }
}
