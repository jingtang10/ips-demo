package com.example.ipsapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.ipsapp.utils.DocumentUtils
import com.example.ipsapp.utils.hasCode
import org.apache.commons.lang3.tuple.MutablePair
import org.hl7.fhir.r4.model.Resource


class SelectIndividualResources : Activity() {

  private val docUtils = DocumentUtils()
  private var map = mutableMapOf<String, ArrayList<Resource>>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.select_individual_resources)

    val doc = docUtils.readFileFromAssets(this, "immunizationBundle.json")

    val checkBoxes = mutableListOf<CheckBox>()
    val checkboxTitleMap = mutableMapOf<String, String>()
    val selectedTitles = intent.getStringArrayListExtra("selectedTitles") ?: emptyList()
    val containerLayout: LinearLayout = findViewById(R.id.containerLayout)

    for (title in selectedTitles) {
      val headingView =
        layoutInflater.inflate(R.layout.heading_item, containerLayout, false) as RelativeLayout
      val headingText = headingView.findViewById<TextView>(R.id.headingText)
      headingText.text = title
      containerLayout.addView(headingView)

      this.map += docUtils.getDataFromDoc(doc, title, map)
      println("THIS IS THE MAP AFTER A FUNCTION $map")

      val resources = map[title]

      resources?.forEach { obj ->
        val code = obj.hasCode()
        val codingArray = code?.coding ?: emptyList()

        codingArray.firstOrNull { it.hasDisplay() }?.let { codingElement ->
          val displayValue = codingElement.display

          val checkBoxItem = layoutInflater.inflate(R.layout.checkbox_item, containerLayout, false) as CheckBox
          checkBoxItem.text = displayValue
          containerLayout.addView(checkBoxItem)
          checkboxTitleMap[displayValue] = title.toString()
          checkBoxes.add(checkBoxItem)
        }
      }
    }


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
          obj.hasCode()?.coding?.any { it.display == value } == true
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

