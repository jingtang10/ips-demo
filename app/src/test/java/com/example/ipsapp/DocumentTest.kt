package com.example.ipsapp

import com.example.ipsapp.fileExamples.file
import com.example.ipsapp.fileExamples.immunizationBundle
// import com.example.ipsapp.fileExamples.immunizationBundle
import com.example.ipsapp.utils.DocumentUtils
import junit.framework.TestCase.assertEquals
import org.json.JSONObject
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


@RunWith(RobolectricTestRunner::class)
@Config(manifest=Config.NONE)
class DocumentTest {

  private val docUtilsMock = Mockito.mock(DocumentUtils::class.java)

  val fileJson = JSONObject(file)
  val immunizationJson = JSONObject(immunizationBundle)

  @Test
  fun getTitlesFromMinBundleDoc() {
    val list = docUtilsMock.getTitlesFromIpsDoc(immunizationJson)
    assertEquals(3, list.size)
  }

  @Test
  fun getTitlesFromImmunizationBundle() {
    val list = docUtilsMock.getTitlesFromIpsDoc(immunizationJson)
    println(list)
  }

  @Test
  fun allergiesCanBeRetrievedFromDoc() {
    val list = docUtilsMock.getAllergiesFromDoc(fileJson)
    println(list)
  }

  @Test
  fun medicationsCanBeRetrievedFromDoc() {
    val list = docUtilsMock.getMedicationFromDoc(immunizationJson)
    println(list)
  }

  @Test
  fun mapCanBeCreatedWithDataForEachTitle() {
    val map = mutableMapOf<String, List<String>>()
    val list = docUtilsMock.getTitlesFromIpsDoc(fileJson)
    for (item in list) {
      docUtilsMock.getDataFromDoc(fileJson, item, map)
    }
    println(map)
  }

  @Test
  fun mapCanBeCreatedWithDataForEachTitleInImmunization() {
    val map = mutableMapOf<String, List<String>>()
    val list = docUtilsMock.getTitlesFromIpsDoc(immunizationJson)
    for (item in list) {
      docUtilsMock.getDataFromDoc(immunizationJson, item, map)
    }
    println(map)
  }
}