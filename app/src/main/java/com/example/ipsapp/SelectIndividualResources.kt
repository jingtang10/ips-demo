package com.example.ipsapp

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.ipsapp.fileExamples.file
import com.example.ipsapp.utils.DocumentUtils
import org.json.JSONObject

class SelectIndividualResources : Activity() {

  val docUtils = DocumentUtils()
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.select_individual_resources)


    val selectedTitles = intent.getStringArrayListExtra("selectedTitles") ?: emptyList()
    // val selectedResourcesTextView = findViewById<TextView>(R.id.selectedResourcesTextView)
    // val selectedResourcesListView = findViewById<ListView>(R.id.selectedResourcesListView)
    //
    // selectedResourcesTextView.text = "Selected Resources:"
    // selectedResourcesListView.adapter = SelectedTitlesAdapter(this, selectedTitles)

    val containerLayout: LinearLayout = findViewById(R.id.containerLayout)

    val map = mutableMapOf<String, List<String>>()

    for (title in selectedTitles) {
      val headingView = layoutInflater.inflate(R.layout.heading_item, containerLayout, false) as RelativeLayout
      val headingText = headingView.findViewById<TextView>(R.id.headingText)
      headingText.text = title
      containerLayout.addView(headingView)

      docUtils.getDataFromDoc(JSONObject(file), title, map)

      val data = map.get(title) // Replace this with your data fetching logic

      if (data != null) {
        for (item in data) {
          val checkBoxItem = layoutInflater.inflate(R.layout.checkbox_item, containerLayout, false) as CheckBox
          checkBoxItem.text = item
          containerLayout.addView(checkBoxItem)
        }
      }
    }
  }

  private fun fetchDataForTitle(title: String?): List<String> {
    return when (title) {
      "Allergies and Intolerances" -> docUtils.getAllergiesFromDoc(JSONObject(file))
      "Medication" -> docUtils.getMedicationFromDoc(JSONObject(file))
      else -> emptyList()
    }
  }

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
