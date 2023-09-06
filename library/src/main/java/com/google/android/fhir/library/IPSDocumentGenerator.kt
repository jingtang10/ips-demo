package com.google.android.fhir.library

import android.content.Context
import android.widget.CheckBox
import android.widget.LinearLayout
import org.hl7.fhir.r4.model.Resource

interface IPSDocumentGenerator {
  fun getTitlesFromDoc(doc : IPSDocument) : List<Title>

  fun getDataFromDoc(doc : IPSDocument, selectedTitles : List<Title>) : Map<Title, List<String>>

  fun generateIPS(selectedResources : List<Resource>): IPSDocument

  fun displayOptions(
    context : Context,
    bundle: IPSDocument?,
    checkBoxes: MutableList<CheckBox>,
    checkboxTitleMap: MutableMap<String, String>,
    containerLayout: LinearLayout,
    map: MutableMap<String, ArrayList<Resource>>,
  )

}