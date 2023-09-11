package com.google.android.fhir.library

import android.content.Context
import android.widget.CheckBox
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import ca.uhn.fhir.context.FhirContext
import ca.uhn.fhir.context.FhirVersionEnum
import com.google.android.fhir.library.utils.DocumentUtils
import com.google.android.fhir.library.utils.hasCode
import org.hl7.fhir.r4.model.Patient
import org.hl7.fhir.r4.model.Resource
import org.hl7.fhir.r4.model.ResourceType

class SelectIndividualResourcesViewModel : ViewModel() {
  private var map = mutableMapOf<String, ArrayList<Resource>>()
  private val parser = FhirContext.forCached(FhirVersionEnum.R4).newJsonParser()
  private val documentGenerator = DocumentGenerator()
  private val checkboxStates = mutableStateListOf<Boolean>()
  private lateinit var patient: Resource
  private val checkBoxes = mutableListOf<CheckBox>()
  private val checkboxTitleMap = mutableMapOf<String, String>()


  @Composable
  fun initializeData(context: Context, onButtonClick: () -> Unit) {
    val docUtils = DocumentUtils()
    val doc = docUtils.readFileFromAssets(context, "immunizationBundle.json")
    val ipsDoc = IPSDocument(parser.parseResource(doc) as org.hl7.fhir.r4.model.Bundle)
    ipsDoc.titles = ArrayList(documentGenerator.getTitlesFromDoc(ipsDoc))
    patient =
      ipsDoc.document.entry.firstOrNull { it.resource.resourceType == ResourceType.Patient }?.resource
        ?: Patient()

    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
      documentGenerator.DisplayOptions2(
        context, ipsDoc, checkBoxes, checkboxTitleMap, map, onButtonClick, checkboxStates
      )
    }

    // documentGenerator.displayOptions(
    //   context, ipsDoc, checkBoxes, checkboxTitleMap, containerLayout, map
    // )
  }

  fun generateIPSDocument(): IPSDocument {
    // Generate IPS document based on selected values
    val selectedValues =
      checkBoxes.filterIndexed { index, _ -> checkboxStates.getOrNull(index) == true }
        .map { checkBox ->
          val title = checkboxTitleMap[checkBox.text.toString()]
          val value = checkBox.text.toString()
          Pair(title, value)
        }

    val outputArray = selectedValues.flatMap { (title, value) ->
      map[title]?.filter { obj ->
        obj.hasCode().first?.coding?.any { it.display == value } == true
      } ?: emptyList()
    } + patient
    return documentGenerator.generateIPS(outputArray)
  }
}