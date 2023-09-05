package com.example.ipsapp

import com.example.ipsapp.fileExamples.file
import com.example.ipsapp.fileExamples.generated
import com.example.ipsapp.fileExamples.immunizationBundle
import com.example.ipsapp.utils.DocumentUtils
import junit.framework.TestCase.assertEquals
import org.hl7.fhir.r4.model.Resource
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


@RunWith(RobolectricTestRunner::class)
@Config(manifest=Config.NONE)
class DocumentTest {

  private val docUtils = DocumentUtils()

  @Test
  fun getTitlesFromMinBundleDoc() {
    val list = docUtils.getTitlesFromIpsDoc(file)
    assertEquals(3, list.size)
  }

  // @Test
  // fun getTitlesFromImmunizationBundle() {
  //   val list = docUtils.getTitlesFromIpsDoc(immunizationBundle)
  //   println(list)
  // }
  //
  // @Test
  // fun getTitlesFromGeneratedDoc() {
  //   val list = docUtils.getTitlesFromIpsDoc(generated)
  //   assertEquals(3, list.size)
  // }
  //
  // @Test
  // fun mapCanBeCreatedWithDataForEachTitle() {
  //   val map = mutableMapOf<String, ArrayList<Resource>>()
  //   val list = docUtils.getTitlesFromIpsDoc(file)
  //   for (item in list) {
  //     docUtils.getDataFromDoc(file, item, map)
  //   }
  //   println(map)
  // }

  @Test
  fun mapCanBeCreatedWithDataForEachTitleInImmunization() {
    val map = mutableMapOf<String, ArrayList<Resource>>()
    val list = docUtils.getTitlesFromIpsDoc(immunizationBundle)
    for (item in list) {
      docUtils.getDataFromDoc(immunizationBundle, item, map)
    }
    println(map)
  }

  @Test
  fun mapCanBeCreatedWithDataForEachTitleInGenerated() {
    val map = mutableMapOf<String, ArrayList<Resource>>()
    val list = docUtils.getTitlesFromIpsDoc(generated)
    for (item in list) {
      docUtils.getDataFromDoc(generated, item, map)
    }
    println(map)
  }
}