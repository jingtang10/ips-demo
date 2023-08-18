package com.example.ipsapp

import com.example.ipsapp.fileExamples.file
import com.example.ipsapp.fileExamples.immunizationBundle
// import com.example.ipsapp.fileExamples.immunizationBundle
import com.example.ipsapp.utils.DocumentUtils
import junit.framework.TestCase.assertEquals
import org.apache.commons.lang3.tuple.MutablePair
import org.hl7.fhir.r4.model.Resource
import org.json.JSONArray
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

  private val fileJson = JSONObject(file)
  private val immunizationJson = JSONObject(immunizationBundle)

  @Test
  fun getTitlesFromMinBundleDoc() {
    val list = docUtilsMock.getTitlesFromIpsDoc(file)
    assertEquals(3, list.size)
  }

  @Test
  fun getTitlesFromImmunizationBundle() {
    val list = docUtilsMock.getTitlesFromIpsDoc(immunizationBundle)
    println(list)
  }

  @Test
  fun mapCanBeCreatedWithDataForEachTitle() {
    val map = mutableMapOf<String, MutablePair<List<String>, ArrayList<Resource>>>()
    val list = docUtilsMock.getTitlesFromIpsDoc(file)
    for (item in list) {
      docUtilsMock.getDataFromDoc(file, item, map)
    }
    println(map)
  }

  @Test
  fun mapCanBeCreatedWithDataForEachTitleInImmunization() {
    val map = mutableMapOf<String, MutablePair<List<String>, ArrayList<Resource>>>()
    val list = docUtilsMock.getTitlesFromIpsDoc(immunizationBundle)
    for (item in list) {
      docUtilsMock.getDataFromDoc(immunizationBundle, item, map)
    }
    println(map)
  }
}