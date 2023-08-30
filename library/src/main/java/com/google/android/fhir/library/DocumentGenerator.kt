package com.google.android.fhir.library

import android.content.Context
import android.view.LayoutInflater
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import ca.uhn.fhir.context.FhirContext
import ca.uhn.fhir.context.FhirVersionEnum
import com.example.ipsapp.utils.DocumentUtils
import com.example.ipsapp.utils.hasCode
import com.google.android.fhir.library.utils.DocumentGeneratorUtils
import com.google.android.fhir.library.utils.TitleAdapter.TitleItem
import org.hl7.fhir.r4.model.Bundle
import org.hl7.fhir.r4.model.CodeableConcept
import org.hl7.fhir.r4.model.Coding
import org.hl7.fhir.r4.model.Composition
import org.hl7.fhir.r4.model.Resource
import org.hl7.fhir.r4.model.ResourceType

class DocumentGenerator : IPSDocumentGenerator {

  val docGenUtils = DocumentGeneratorUtils()
  private val parser = FhirContext.forCached(FhirVersionEnum.R4).newJsonParser()

  override fun getTitlesFromDoc(doc: IPSDocument): List<Title> {
    val bundle = doc.document
    val composition =
      bundle?.entry?.firstOrNull { it.resource.resourceType == ResourceType.Composition }?.resource as Composition
    return composition.section.map { Title(it.title, ArrayList()) }
  }

  override fun getDataFromDoc(
    doc: IPSDocument,
    selectedTitles: List<Title>,
  ): Map<Title, List<String>> {
    TODO("Not yet implemented")
  }

  override fun generateIPS(
    existingDoc: IPSDocument,
    selectedResources: List<Resource>,
  ): IPSDocument {
    TODO("Not yet implemented")
  }

  override fun generateIPS(selectedResources: List<Resource>): IPSDocument {
    val bundle = Bundle()
    bundle.type = Bundle.BundleType.DOCUMENT

    // Create a Composition resource to represent the IPS document
    val composition = Composition()
    composition.type = CodeableConcept().apply {
      coding.add(Coding().apply {
        system = "http://loinc.org"
        code = "60591-5"
        display = "Patient Summary Document"
      })
    }
    // Set other properties of the Composition as needed
    composition.title = "Patient Summary Document Title"
    composition.status = Composition.CompositionStatus.FINAL

    // Create sections for each resource and add them to the Composition
    val sections = mutableListOf<Composition.SectionComponent>()
    for (resource in selectedResources) {
      val section = docGenUtils.createResourceSection(resource)
      sections.add(section)
    }
    composition.section = sections

    // Add the Composition to the bundle
    bundle.addEntry(Bundle.BundleEntryComponent().apply {
      resource = composition
      fullUrl = ""
    })
    return IPSDocument(bundle)
  }

  override fun generateTitleAdapter(bundle: IPSDocument): ArrayList<TitleItem> {
    val titlesFromDoc = getTitlesFromDoc(bundle)
    val titleList: ArrayList<TitleItem> = ArrayList()
    for (item in titlesFromDoc) {
      item.name?.let { TitleItem(it) }?.let { titleList.add(it) }
    }
    return titleList
  }

  override fun displayOptions(
    context: Context,
    bundle: IPSDocument?,
    checkBoxes: MutableList<CheckBox>,
    checkboxTitleMap: MutableMap<String, String>,
    containerLayout: LinearLayout,
    map: MutableMap<String, ArrayList<Resource>>,
  ) {
    val parser = FhirContext.forCached(FhirVersionEnum.R4).newJsonParser()
    val docUtils = DocumentUtils()
    val layoutInflater = LayoutInflater.from(context)
    for (title in bundle?.titles!!) {
      if (title.name != null) {
        val headingView =
          layoutInflater.inflate(R.layout.heading_item, containerLayout, false) as RelativeLayout
        val headingText = headingView.findViewById<TextView>(R.id.headingText)
        headingText.text = title.name
        containerLayout.addView(headingView)

        map += docUtils.getDataFromDoc(parser.encodeResourceToString(bundle.document), title.name!!, map)
        println("THIS IS THE MAP AFTER A FUNCTION $map")

        val resources = map[title.name]

        resources?.forEach { obj ->
          val code = obj.hasCode()
          val codingArray = code.first?.coding ?: emptyList()

          codingArray.firstOrNull { it.hasDisplay() }?.let { codingElement ->
            val displayValue = codingElement.display

            val checkBoxItem =
              layoutInflater.inflate(com.google.android.fhir.library.R.layout.checkbox_item, containerLayout, false) as CheckBox
            checkBoxItem.text = displayValue
            containerLayout.addView(checkBoxItem)
            checkboxTitleMap[displayValue] = title.toString()
            checkBoxes.add(checkBoxItem)
          }
        }
      }
    }
  }
}