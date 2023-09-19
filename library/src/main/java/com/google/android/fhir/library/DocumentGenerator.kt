package com.google.android.fhir.library

import android.content.Context
import android.view.LayoutInflater
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ca.uhn.fhir.context.FhirContext
import ca.uhn.fhir.context.FhirVersionEnum
import com.google.android.fhir.library.dataClasses.IPSDocument
import com.google.android.fhir.library.dataClasses.Title
import com.google.android.fhir.library.interfaces.IPSDocumentGenerator
import com.google.android.fhir.library.utils.DocumentGeneratorUtils
import com.google.android.fhir.library.utils.DocumentUtils
import org.hl7.fhir.r4.model.Composition
import org.hl7.fhir.r4.model.Observation
import org.hl7.fhir.r4.model.Organization
import org.hl7.fhir.r4.model.Reference
import org.hl7.fhir.r4.model.Resource
import org.hl7.fhir.r4.model.ResourceType

class DocumentGenerator : IPSDocumentGenerator {

  private val docGenUtils = DocumentGeneratorUtils()
  private val docUtils = DocumentUtils()
  private val parser = FhirContext.forCached(FhirVersionEnum.R4).newJsonParser()

  override fun getTitlesFromDoc(doc: IPSDocument): List<Title> {
    val bundle = doc.document
    val composition =
      bundle.entry?.firstOrNull { it.resource.resourceType == ResourceType.Composition }?.resource as Composition
    return composition.section.map { Title(it.title, ArrayList()) }
  }

  override fun getDataFromDoc(doc: IPSDocument): Map<Title, List<Resource>> {
    val bundle = doc.document
    val map: MutableMap<Title, List<Resource>> = mutableMapOf()
    for (title in doc.titles) {
      val filteredResources = bundle.entry.map { it.resource }.filter { resource ->
        val resourceType = resource.resourceType.toString()
        docUtils.getSearchingCondition(title.name, resourceType)
      }
      val resourceList =
        filteredResources.filterNot { docUtils.shouldExcludeResource(title.name, it) }
      map[title] = ArrayList(resourceList)
    }
    return map
  }

  override fun generateIPS(selectedResources: List<Resource>): IPSDocument {
    val composition = docGenUtils.createIPSComposition()
    val sections = docGenUtils.createIPSSections(selectedResources)
    val (missingSections, missingResources) = docGenUtils.checkSections(sections)
    val referenced = mutableListOf<Resource>()

    selectedResources.forEach { resource ->
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
    if (resource is Observation) {
      val performerReferences = resource.performer
      performerReferences.forEach { performerReference ->
        if (performerReference.reference.isNotBlank()) {
          val organization = createOrganizationFromReference(performerReference)
          organizationReferences.add(organization)
        }
      }
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
    bundle: IPSDocument,
    checkBoxes: MutableList<CheckBox>,
    checkboxTitleMap: MutableMap<String, String>,
  ): Map<Title, List<Resource>> {
    val map = getDataFromDoc(bundle)
    val containerLayout = (context as AppCompatActivity).findViewById<LinearLayout>(R.id.containerLayout)
    for (title in bundle.titles) {
      val resources = map[title]
      if (resources?.any { docUtils.getCodings(it)?.isNotEmpty() == true } == true) {
        val headingView = createHeadingView(context, title.name, containerLayout)
        containerLayout.addView(headingView)
        resources.forEach { obj ->
          docUtils.getCodings(obj)?.firstOrNull { it.hasDisplay() }?.let { codingElement ->
            val displayValue = codingElement.display
            val checkBoxItem = createCheckBox(context, displayValue, containerLayout)
            containerLayout.addView(checkBoxItem)
            checkboxTitleMap[displayValue] = title.name
            checkBoxes.add(checkBoxItem)
          }
        }
      }
    }
    return map
  }

  private fun createHeadingView(context: Context, titleName: String, containerLayout: LinearLayout): RelativeLayout {
    val layoutInflater = LayoutInflater.from(context)
    val headingView = layoutInflater.inflate(R.layout.heading_item, containerLayout,false) as RelativeLayout
    val headingText = headingView.findViewById<TextView>(R.id.headingText)
    headingText.text = titleName
    return headingView
  }

  private fun createCheckBox(context: Context, text: String, containerLayout: LinearLayout): CheckBox {
    val layoutInflater = LayoutInflater.from(context)
    val checkBoxItem = layoutInflater.inflate(R.layout.checkbox_item, containerLayout,false) as CheckBox
    checkBoxItem.text = text
    return checkBoxItem
  }

}