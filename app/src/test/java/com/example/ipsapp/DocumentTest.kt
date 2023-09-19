package com.example.ipsapp

import ca.uhn.fhir.context.FhirContext
import ca.uhn.fhir.context.FhirVersionEnum
import com.example.ipsapp.fileExamples.file
import com.example.ipsapp.fileExamples.generated
import com.example.ipsapp.fileExamples.immunizationBundleString
import com.google.android.fhir.library.DocumentGenerator
import com.google.android.fhir.library.dataClasses.IPSDocument
import com.google.android.fhir.library.dataClasses.Title
import org.hl7.fhir.r4.model.Bundle
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


@RunWith(RobolectricTestRunner::class)
@Config(manifest=Config.NONE)
class DocumentTest {

  private val docGenerator = DocumentGenerator()
  private val parser = FhirContext.forCached(FhirVersionEnum.R4).newJsonParser()

  private val fileBundle = parser.parseResource(file) as Bundle
  private val immunizationBundle = parser.parseResource(immunizationBundleString) as Bundle
  private val generatedBundle = parser.parseResource(generated) as Bundle

  @Test
  fun getTitlesFromMinBundleDoc() {
    val list = docGenerator.getTitlesFromDoc(IPSDocument(fileBundle))
    assertEquals(3, list.size)
  }

  @Test
  fun getTitlesFromImmunizationBundle() {
    val list = docGenerator.getTitlesFromDoc(IPSDocument(immunizationBundle))
    println(list)
  }

  @Test
  fun mapCanBeCreatedWithDataForEachTitle() {
    val doc = IPSDocument(fileBundle)
    doc.titles = docGenerator.getTitlesFromDoc(doc) as ArrayList<Title>
    val map = docGenerator.getDataFromDoc(doc)
    println(map)
  }

  @Test
  fun mapCanBeCreatedWithDataForEachTitleInImmunization() {
    val doc = IPSDocument(immunizationBundle)
    doc.titles = docGenerator.getTitlesFromDoc(doc) as ArrayList<Title>
    val map = docGenerator.getDataFromDoc(doc)
    println(map)
  }
  //
  // @Test
  // fun mapCanBeCreatedWithDataForEachTitleInGenerated() {
  //   val map = mutableMapOf<String, ArrayList<Resource>>()
  //   val list = docUtils.getTitlesFromIpsDoc(generated)
  //   for (item in list) {
  //     docUtils.getDataFromDoc(generated, item, map)
  //   }
  //   println(map)
  // }
}