package com.example.ipsapp

import ca.uhn.fhir.context.FhirContext
import ca.uhn.fhir.context.FhirVersionEnum
import com.example.ipsapp.fileExamples.file
import com.example.ipsapp.fileExamples.generated
import com.example.ipsapp.fileExamples.immunizationBundleString
import com.google.android.fhir.library.DocumentGenerator
import com.google.android.fhir.library.IPSDocument
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
  fun getTitlesFromGeneratedDoc() {
    val list = docGenerator.getTitlesFromDoc(IPSDocument(generatedBundle))
    assertEquals(3, list.size)
  }

  // @Test
  // fun mapCanBeCreatedWithDataForEachTitle() {
  //   val map = mutableMapOf<Title, ArrayList<Resource>>()
  //   val doc = IPSDocument(fileBundle)
  //   val list = docGenerator.getTitlesFromDoc(IPSDocument(fileBundle))
  //   for (item in list) {
  //     docGenerator.getDataFromDoc(doc, item)
  //   }
  //   println(map)
  // }
  //
  // @Test
  // fun mapCanBeCreatedWithDataForEachTitleInImmunization() {
  //   val map = mutableMapOf<String, ArrayList<Resource>>()
  //   val list = docUtils.getTitlesFromIpsDoc(immunizationBundle)
  //   for (item in list) {
  //     docUtils.getDataFromDoc(immunizationBundle, item, map)
  //   }
  //   println(map)
  // }
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