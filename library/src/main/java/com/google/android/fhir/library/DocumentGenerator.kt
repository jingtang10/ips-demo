package com.google.android.fhir.library

import android.content.Context
import android.view.LayoutInflater
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ca.uhn.fhir.context.FhirContext
import ca.uhn.fhir.context.FhirVersionEnum
import com.google.android.fhir.library.utils.DocumentGeneratorUtils
import com.google.android.fhir.library.utils.DocumentUtils
import com.google.android.fhir.library.utils.hasCode
import org.hl7.fhir.r4.model.Composition
import org.hl7.fhir.r4.model.Observation
import org.hl7.fhir.r4.model.Organization
import org.hl7.fhir.r4.model.Reference
import org.hl7.fhir.r4.model.Resource
import org.hl7.fhir.r4.model.ResourceType

class DocumentGenerator : IPSDocumentGenerator {

  private val docGenUtils = DocumentGeneratorUtils()
  val docUtils = DocumentUtils()
  private val parser = FhirContext.forCached(FhirVersionEnum.R4).newJsonParser()

  override fun getTitlesFromDoc(doc: IPSDocument): List<Title> {
    val bundle = doc.document
    val composition =
      bundle.entry?.firstOrNull { it.resource.resourceType == ResourceType.Composition }?.resource as Composition
    return composition.section.map { Title(it.title, ArrayList()) }
  }

  override fun getDataFromDoc(
    doc: IPSDocument,
    selectedTitles: List<Title>,
  ): Map<Title, List<String>> {
    TODO("Not yet implemented")
  }

  override fun generateIPS(selectedResources: List<Resource>): IPSDocument {
    val composition = docGenUtils.createIPSComposition()
    val sections = docGenUtils.createIPSSections(selectedResources)
    val (missingSections, missingResources) = docGenUtils.checkSections(sections)
    val referenced = mutableListOf<Resource>()

    selectedResources.forEach { resource ->
      // Check if the resource is of type Observation and has performer references
      val references = findReferences(resource)
      referenced.addAll(references)
    }
    sections.addAll(missingSections)
    composition.section = sections
    val bundle =
      docGenUtils.addResourcesToDoc(composition, selectedResources + referenced, missingResources)
    println(parser.encodeResourceToString(bundle))
    return IPSDocument(bundle)
  }

  private fun findReferences(resource: Resource): List<Resource> {
    val organizationReferences = mutableListOf<Resource>()

    // Check if the resource type is Observation and if it has a performer reference
    if (resource is Observation) {
      val performerReferences = (resource as Observation).performer
      performerReferences.forEach { performerReference ->
        if (performerReference.reference.isNotBlank()) {
          val organization = createOrganizationFromReference(performerReference)
          organizationReferences.add(organization)
        }
      }
      // }
    }
    return organizationReferences
  }

  private fun createOrganizationFromReference(reference: Reference): Organization {
    val organization = Organization()
    organization.id = reference.reference
    organization.name = "Unknown Organization"
    return organization
  }

  override fun displayOptions(
    context: Context,
    bundle: IPSDocument?,
    checkBoxes: MutableList<CheckBox>,
    checkboxTitleMap: MutableMap<String, String>,
    containerLayout: LinearLayout,
    map: MutableMap<String, ArrayList<Resource>>,
  ) {
    val layoutInflater = LayoutInflater.from(context)

    for (title in bundle?.titles!!) {
      if (title.name != null) {
        map += docUtils.getDataFromDoc(
          parser.encodeResourceToString(bundle.document), title.name!!, map
        )
        val resources = map[title.name]

        val codingArrayNotEmpty = resources?.any { obj ->
          val code = obj.hasCode()
          val codingArray = code.first?.coding ?: emptyList()
          !codingArray.isNullOrEmpty()
        } ?: false

        if (codingArrayNotEmpty) {
          val headingView =
            layoutInflater.inflate(R.layout.heading_item, containerLayout, false) as RelativeLayout
          val headingText = headingView.findViewById<TextView>(R.id.headingText)
          headingText.text = title.name
          containerLayout.addView(headingView)

          resources?.forEach { obj ->
            val code = obj.hasCode()
            val codingArray = code.first?.coding ?: emptyList()

            codingArray.firstOrNull { it.hasDisplay() }?.let { codingElement ->
              val displayValue = codingElement.display

              val checkBoxItem =
                layoutInflater.inflate(R.layout.checkbox_item, containerLayout, false) as CheckBox
              checkBoxItem.text = displayValue
              containerLayout.addView(checkBoxItem)
              checkboxTitleMap[displayValue] = title.name.toString()
              checkBoxes.add(checkBoxItem)
            }
          }
        }
      }
    }
  }

  @Composable
  fun DisplayOptions2(
    context: Context,
    bundle: IPSDocument?,
    checkBoxes: MutableList<CheckBox>,
    checkboxTitleMap: MutableMap<String, String>,
    map: MutableMap<String, ArrayList<Resource>>,
    onButtonClick: () -> Unit,
    checkboxStates: SnapshotStateList<Boolean>,
  ) {
    val layoutInflater = LayoutInflater.from(context)

    Column(
      modifier = Modifier.verticalScroll(rememberScrollState()) // Enable vertical scrolling
    ) {

      for (title in bundle?.titles ?: emptyList()) {
        if (title.name != null) {
          map += docUtils.getDataFromDoc(
            parser.encodeResourceToString(bundle?.document), title.name!!, map
          )
          val resources = map[title.name]

          val codingArrayNotEmpty = resources?.any { obj ->
            val code = obj.hasCode()
            val codingArray = code.first?.coding ?: emptyList()
            !codingArray.isNullOrEmpty()
          } ?: false

          if (codingArrayNotEmpty) {
            Column {
              Text(text = title.name!!, modifier = Modifier.padding(16.dp))
              resources?.forEachIndexed { index, obj ->
                val code = obj.hasCode()
                val codingArray = code.first?.coding ?: emptyList()

                codingArray.firstOrNull { it.hasDisplay() }?.let { codingElement ->
                  val displayValue = codingElement.display

                  val isChecked = remember { mutableStateOf(checkboxStates.getOrNull(index) ?: false) }

                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(16.dp)
                  ) {
                    Checkbox(
                      checked = isChecked.value,
                      onCheckedChange = { newCheckedValue ->
                        isChecked.value = newCheckedValue
                        // Add the checked checkbox to the list
                        if (newCheckedValue) {
                          checkBoxes.add(CheckBox(context)) // Replace 'context' with your actual context
                        }
                        // checkboxStates[index] = newCheckedValue
                      },
                    )
                    Text(text = displayValue, modifier = Modifier.padding(start = 8.dp))
                  }
                  checkboxTitleMap[displayValue] = title.name.toString()
                }
              }
            }
          }
        }
      }
      Button(
        onClick = { onButtonClick() },
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp)
      ) {
        Text(text = "Click Me")
      }
    }
  }
}