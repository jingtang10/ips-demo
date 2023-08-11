package com.example.ipsapp

import com.example.ipsapp.fileExamples.file
import com.example.ipsapp.utils.UrlUtils
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest= Config.NONE)
class QRGeneratorTest {

  private val urlUtilsMock = Mockito.mock(UrlUtils::class.java)

  @Test
  fun postingToServerReturnsManifestIdAndToken() {
    runBlocking {
      val res = urlUtilsMock.getManifestUrl()
      println(res)
      Assert.assertTrue(res.contains("id") && res.contains("managementToken") &&
                          res.contains("active"))
    }
  }

  @Test
  fun canConvertFilesIntoJweTokens() {
    val encryptionKey = urlUtilsMock.generateRandomKey()
    val contentJson = Gson().toJson(file)
    val contentEncrypted = urlUtilsMock.encrypt(contentJson, encryptionKey)
    println(contentEncrypted)
    Assert.assertEquals(contentEncrypted.split('.').size, 5)
  }

  @Test
  fun fileCanSuccessfullyBeEncryptedAndDecrypted() {
    val key = urlUtilsMock.generateRandomKey()
    val content = Gson().toJson(file)

    val encrypted = urlUtilsMock.encrypt(content, key)
    val decrypted = urlUtilsMock.decodeShc(encrypted, key)
    Assert.assertEquals(content, decrypted)
  }

}