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
import java.util.Date
import java.util.UUID
import org.hl7.fhir.r4.model.Bundle
import org.hl7.fhir.r4.model.CodeableConcept
import org.hl7.fhir.r4.model.Coding
import org.hl7.fhir.r4.model.Composition
import org.hl7.fhir.r4.model.DateTimeType
import org.hl7.fhir.r4.model.Reference
import org.hl7.fhir.r4.model.Resource
import org.hl7.fhir.r4.model.ResourceType

class DocumentGenerator : IPSDocumentGenerator {

  private val docGenUtils = DocumentGeneratorUtils()
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

  override fun generateIPS(selectedResources: List<Resource>): IPSDocument {
    val bundle = Bundle()
    bundle.type = Bundle.BundleType.DOCUMENT

    // Create a Composition resource to represent the IPS document
    val composition = Composition()
    composition.id = UUID.randomUUID().toString()
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

    val currentDate = Date()
    val currentDateTime = DateTimeType(currentDate)
    composition.dateElement = currentDateTime
    composition.title = "Patient Summary"
    composition.author.add(Reference("Practitioner/12345"))

    // Create sections for each resource and add them to the Composition
    val sections = mutableListOf<Composition.SectionComponent>()
    for (res in selectedResources) {
      val section = docGenUtils.createResourceSection(res)
      val title = docGenUtils.getResourceTitle(res)

      // Check if a section with the same title already exists
      val existingSection = sections.find { it.title == title }

      if (existingSection != null) {
        // Replace the existing section with the new one
        sections.remove(existingSection)
        sections.add(section)
      } else {
        // Add the new section to the list
        sections.add(section)
      }
    }
    val (missingSections, missingResources) = docGenUtils.checkSections(sections)
    sections.addAll(missingSections)
    composition.section = sections

    // Add the Composition to the bundle
    bundle.addEntry(Bundle.BundleEntryComponent().apply {
      resource = composition
      fullUrl = "urn:uuid:${composition.idBase}"
    })
    for(res in selectedResources) {
      bundle.addEntry(Bundle.BundleEntryComponent().apply {
        resource = res
        fullUrl = "urn:uuid:${res.idElement.idPart}"
      })
    }
    for(res in missingResources) {
      bundle.addEntry(Bundle.BundleEntryComponent().apply {
        resource = res
        fullUrl = "urn:uuid:${res.idElement.idPart}"
      })
    }
    println(parser.encodeResourceToString(bundle))
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