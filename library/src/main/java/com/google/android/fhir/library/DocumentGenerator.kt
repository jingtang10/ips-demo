package com.google.android.fhir.library

import com.google.android.fhir.library.utils.TitleAdapter.TitleItem
import org.hl7.fhir.r4.model.Composition
import org.hl7.fhir.r4.model.Resource
import org.hl7.fhir.r4.model.ResourceType

class DocumentGenerator : IPSDocumentGenerator {
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

  override fun generateIPS(selectedResources: List<Resource>) {
    TODO("Not yet implemented")
  }

  override fun generateTitleAdapter(bundle: IPSDocument): ArrayList<TitleItem> {
    val titlesFromDoc = getTitlesFromDoc(bundle)
    val titleList: ArrayList<TitleItem> = ArrayList()
    for (item in titlesFromDoc) {
      item.name?.let { TitleItem(it) }?.let { titleList.add(it) }
    }
    return titleList
  }
}