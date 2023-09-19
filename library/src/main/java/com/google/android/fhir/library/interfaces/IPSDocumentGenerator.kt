package com.google.android.fhir.library.interfaces

import android.content.Context
import android.widget.CheckBox
import com.google.android.fhir.library.dataClasses.IPSDocument
import com.google.android.fhir.library.dataClasses.Title
import org.hl7.fhir.r4.model.Resource

interface IPSDocumentGenerator {

  /* Returns a list of all the titles of the sections present in an IPS document */
  fun getTitlesFromDoc(doc: IPSDocument): List<Title>

  /* Returns a map of all the sections in the document to the list of resources listed under that section */
  fun getDataFromDoc(doc: IPSDocument): Map<Title, List<Resource>>

  /* Generates a new IPS document given a list of patient-selected resources */
  fun generateIPS(selectedResources: List<Resource>): IPSDocument

  /* Renders an IPS document for an android device */
  fun displayOptions(
    context: Context,
    bundle: IPSDocument,
    checkBoxes: MutableList<CheckBox>,
    checkboxTitleMap: MutableMap<String, String>
  ): Map<Title, List<Resource>>

}