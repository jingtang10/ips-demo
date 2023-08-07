package com.example.ipsapp

import com.example.ipsapp.fileExamples.file
import junit.framework.TestCase.assertEquals
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest


@RunWith(MockitoJUnitRunner::class)
@PrepareForTest(JSONObject::class)
class DocumentTest {

  // private val docUtilsMock = Mockito.mock(DocumentUtils::class.java)
  @InjectMocks
  private lateinit var docUtilsMock: DocumentUtils

  @Mock
  private lateinit var jsonMock: JSONObject
  val jsonArrMock = PowerMockito.mock(JSONArray::class.java)


  @Before
  fun setup() {

  }

  @Test
  fun getTitlesFromDoc() {

    val jsonArray = JSONArray(JSONTokener(file))

    PowerMockito.`when`(jsonMock.has(anyString())).thenReturn(true)
    PowerMockito.`when`(jsonMock.getJSONArray(anyString())).thenReturn(jsonArray)
    PowerMockito.`when`(jsonArrMock.length()).thenReturn(3)
    PowerMockito.`when`(jsonArrMock.getJSONObject(anyInt())).thenReturn(jsonMock)

    val list = docUtilsMock.getTitlesFromIpsDoc(jsonMock)
    assertEquals(3, list.size)
  }

}