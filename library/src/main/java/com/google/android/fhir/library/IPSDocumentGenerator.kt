package com.google.android.fhir.library

import com.google.android.fhir.library.utils.TitleAdapter.TitleItem
import org.hl7.fhir.r4.model.Resource

interface IPSDocumentGenerator {
  fun getTitlesFromDoc(doc : IPSDocument) : List<Title>

  fun getDataFromDoc(doc : IPSDocument, selectedTitles : List<Title>) : Map<Title, List<String>>

  fun generateIPS(existingDoc : IPSDocument, selectedResources : List<Resource>) : IPSDocument

  fun generateIPS(selectedResources : List<Resource>)
  fun generateTitleAdapter(bundle: IPSDocument): ArrayList<TitleItem>

}