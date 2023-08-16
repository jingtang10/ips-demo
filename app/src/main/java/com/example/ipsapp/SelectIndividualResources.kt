package com.example.ipsapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.ipsapp.fileExamples.file
import com.example.ipsapp.fileExamples.immunizationBundle
import com.example.ipsapp.utils.DocumentUtils
import org.apache.commons.lang3.tuple.MutablePair
import org.json.JSONArray
import org.json.JSONObject
import android.text.method.ScrollingMovementMethod

class SelectIndividualResources : Activity() {

  val docUtils = DocumentUtils()
  var map = mutableMapOf<String, MutablePair<List<String>, ArrayList<JSONObject>>>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.select_individual_resources)

    val doc = JSONObject(docUtils.readFileFromAssets(this, "immunizationBundle.json"))

    val checkBoxes = mutableListOf<CheckBox>()
    val checkboxTitleMap = mutableMapOf<String, String>()


    val selectedTitles = intent.getStringArrayListExtra("selectedTitles") ?: emptyList()
    // val selectedResourcesTextView = findViewById<TextView>(R.id.selectedResourcesTextView)
    // val selectedResourcesListView = findViewById<ListView>(R.id.selectedResourcesListView)
    //
    // selectedResourcesTextView.text = "Selected Resources:"
    // selectedResourcesListView.adapter = SelectedTitlesAdapter(this, selectedTitles)

    val containerLayout: LinearLayout = findViewById(R.id.containerLayout)

    for (title in selectedTitles) {
      val headingView = layoutInflater.inflate(R.layout.heading_item, containerLayout, false) as RelativeLayout
      val headingText = headingView.findViewById<TextView>(R.id.headingText)
      headingText.text = title
      containerLayout.addView(headingView)

      this.map += docUtils.getDataFromDoc(doc, title, map)
      println("THIS IS THE MAP AFTER A FUNCTION $map")

      val data = map[title]?.left

      if (!data.isNullOrEmpty()) {
        for (item in data) {
          val checkBoxItem = layoutInflater.inflate(R.layout.checkbox_item, containerLayout, false) as CheckBox
          checkBoxItem.text = item
          containerLayout.addView(checkBoxItem)

          checkboxTitleMap[item] = title
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

      val outputArray = mutableListOf<ArrayList<JSONObject>>()

      for ((title, value) in selectedCheckedValuesWithTitles) {
        val objArray = map[title]?.right
        if (objArray != null) {
          for (obj in objArray) {
            val codingArray = obj.getJSONObject("code")?.getJSONArray("coding")
            if (codingArray != null && codingArray.length() > 0) {
              for (i in 0 until codingArray.length()) {
                val codingElement = codingArray.getJSONObject(i)
                val displayValue = codingElement.optString("display")
                println("display val $displayValue")
                println("val $value")
                if (displayValue.equals(value)) {
                  map[title]?.right?.let { it1 -> outputArray.add(it1) }
                  break
                }
                // }
              }

            }}
            println("Title: $title, Value: $value")
            println(outputArray)

        }
      }

      val i = Intent(this@SelectIndividualResources,CreatePasscode::class.java)
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
      // i.putStringArrayListExtra("selectedCheckedValues", ArrayList(selectedCheckedValues))
      startActivity(i)
    }
  }

  // private fun fetchDataForTitle(title: String?): List<String> {
  //   return when (title) {
  //     "Allergies and Intolerances" -> docUtils.getAllergiesFromDoc(JSONObject(file))
  //     "Medication" -> docUtils.getMedicationFromDoc(JSONObject(file))
  //     else -> emptyList()
  //   }
  // }

  inner class SelectedTitlesAdapter(context: Context, private val titles: List<String?>) :
    ArrayAdapter<String?>(context, android.R.layout.simple_list_item_1, titles) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
      val title = titles[position]
      val view = convertView ?: LayoutInflater.from(context)
        .inflate(android.R.layout.simple_list_item_1, parent, false)
      (view as TextView).text = title ?: "N/A"
      return view
    }
  }

}
