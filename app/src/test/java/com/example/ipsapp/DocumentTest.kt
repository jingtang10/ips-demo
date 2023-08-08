package com.example.ipsapp

import com.example.ipsapp.fileExamples.file
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

  @Test
  fun getTitlesFromDoc() {
    val list = docUtilsMock.getTitlesFromIpsDoc(JSONObject(file))
    assertEquals(3, list.size)
  }

  @Test
  fun allergiesCanBeRetrievedFromDoc() {
    val list = docUtilsMock.getAllergiesFromDoc(JSONObject(file))
    println(list)
  }

}