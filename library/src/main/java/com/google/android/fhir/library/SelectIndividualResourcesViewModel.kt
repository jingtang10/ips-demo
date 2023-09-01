package com.google.android.fhir.library

import android.content.Context
import android.widget.CheckBox
import android.widget.LinearLayout
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ca.uhn.fhir.context.FhirContext
import ca.uhn.fhir.context.FhirVersionEnum
import com.example.ipsapp.utils.DocumentUtils
import com.example.ipsapp.utils.hasCode
import org.hl7.fhir.r4.model.Resource

class SelectIndividualResourcesViewModel : ViewModel() {
  private var map = mutableMapOf<String, ArrayList<Resource>>()
  private val parser = FhirContext.forCached(FhirVersionEnum.R4).newJsonParser()
  val documentGenerator = DocumentGenerator()

  val checkBoxes = mutableListOf<CheckBox>()
  val checkboxTitleMap = mutableMapOf<String, String>()
  val selectedCheckedValuesWithTitles = MutableLiveData<List<Pair<String?, String>>>()

  fun initializeData(context: Context, containerLayout: LinearLayout) {
    val docUtils = DocumentUtils()
    val doc = docUtils.readFileFromAssets(context, "immunizationBundle.json")
    val bundle = IPSDocument(parser.parseResource(doc) as org.hl7.fhir.r4.model.Bundle)
    bundle.titles = ArrayList(docUtils.getTitlesFromIpsDoc(doc).map { Title(it) })

    documentGenerator.displayOptions(
      context, bundle, checkBoxes, checkboxTitleMap, containerLayout, map
    )
  }

  fun onCheckBoxSelected() {
    // Handle checkbox selection and update selectedCheckedValuesWithTitles
    val selectedValues = checkBoxes.filter { it.isChecked }.map { checkBox ->
      val title = checkboxTitleMap[checkBox.text.toString()]
      val value = checkBox.text.toString()
      Pair(title, value)
    }
    selectedCheckedValuesWithTitles.value = selectedValues
  }

  fun generateIPSDocument(): IPSDocument {
    // Generate IPS document based on selected values
    val selectedValues = selectedCheckedValuesWithTitles.value ?: emptyList()
    val outputArray = selectedValues.flatMap { (title, value) ->
      map[title]?.filter { obj ->
        obj.hasCode().first?.coding?.any { it.display == value } == true
      } ?: emptyList()
    }
    return documentGenerator.generateIPS(outputArray)
  }
}