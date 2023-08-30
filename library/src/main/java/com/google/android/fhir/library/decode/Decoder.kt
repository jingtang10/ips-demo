package com.google.android.fhir.library.decode

import android.os.Build
import androidx.annotation.RequiresApi
import ca.uhn.fhir.context.FhirContext
import ca.uhn.fhir.context.FhirVersionEnum
import com.example.ipsapp.utils.DocumentUtils
import com.example.ipsapp.utils.ReadShlUtils
import com.google.android.fhir.library.IPSDocument
import com.google.android.fhir.library.SHLData
import com.google.android.fhir.library.SHLDecoder
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.methods.HttpPost
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.entity.StringEntity
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.impl.client.CloseableHttpClient
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.impl.client.HttpClients
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.util.EntityUtils
import java.nio.charset.StandardCharsets
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.coroutineScope
import org.hl7.fhir.r4.model.Bundle
import org.json.JSONObject

class Decoder(private val shlData: SHLData?) : SHLDecoder {

  private val readShlUtils = ReadShlUtils()
  val docUtils = DocumentUtils()
  private val parser = FhirContext.forCached(FhirVersionEnum.R4).newJsonParser()

  @RequiresApi(Build.VERSION_CODES.O)
  override suspend fun decodeSHLToDocument(recipient: String): IPSDocument {
    constructShlObj()
    val jsonData = "{\"recipient\":\"${recipient}\"}"
    val bundle = postToServer(jsonData)
    return IPSDocument(bundle)
  }

  @RequiresApi(Build.VERSION_CODES.O)
  override suspend fun decodeSHLToDocument(recipient: String, passcode: String): IPSDocument {
    constructShlObj()
    val jsonData = "{\"passcode\":\"${passcode}\", \"recipient\":\"${recipient}\"}"
    val bundle = postToServer(jsonData)
    return IPSDocument(bundle)
  }

  override fun storeDocument(doc: IPSDocument) {
    TODO("Not yet implemented")
  }

  override fun hasPasscode(): Boolean {
    shlData?.flag?.forEach {
      if (it == 'P') {
        return true
      }
    }
    return false
  }

  @RequiresApi(Build.VERSION_CODES.O)
  @OptIn(DelicateCoroutinesApi::class)
  private suspend fun postToServer(jsonData: String): Bundle = coroutineScope {
    val httpClient: CloseableHttpClient = HttpClients.createDefault()
    val httpPost = HttpPost(shlData?.manifestUrl)
    httpPost.addHeader("Content-Type", "application/smart-health-card")

    val entity = StringEntity(jsonData)

    httpPost.entity = entity
    val response = httpClient.execute(httpPost)

    val responseBody = EntityUtils.toString(response.entity, StandardCharsets.UTF_8)
    httpClient.close()

    val jsonObject = JSONObject(responseBody)

      // throw error when passcode wrong
      val filesArray = jsonObject.getJSONArray("files")

      // create a string array and add the 'embedded' data to it
      // need to work out what to do when it has a location instead
      val embeddedList = ArrayList<String>()
      for (i in 0 until filesArray.length()) {
        val fileObject = filesArray.getJSONObject(i)
        if (fileObject.has("embedded")) {
          val embeddedValue = fileObject.getString("embedded")
          embeddedList.add(embeddedValue)
        } else {
          val loc = fileObject.getString("location")
          readShlUtils.getRequest(loc)?.let { embeddedList.add(it) }
        }
      }
      val embeddedArray = embeddedList.toTypedArray()
      val bundle = decodeEmbeddedArray(embeddedArray)

      return@coroutineScope bundle
    }


  @RequiresApi(Build.VERSION_CODES.O)
  fun decodeEmbeddedArray(embeddedArray : Array<String>) : Bundle {
    // now need to base64Url decode the thing in here to get the fhir resource
    // go through each element of the responseBody and do this (somehow set different text views on the page to the data

      var healthData = ""
      // may be worth putting this loop in decodeShc?
      for (elem in embeddedArray) {
        println("for loop")
        val decodedShc = shlData?.key?.let { readShlUtils.decodeShc(elem, it) }
        if (decodedShc != "") {
          println("1")

          val toDecode = decodedShc?.let { readShlUtils.extractVerifiableCredential(it) }
          if (toDecode == "") {
            healthData = decodedShc
            break
          }
          //so this gives you the JWT string to split, decode and decompress
          val obj =
            JSONObject(toDecode?.let { readShlUtils.decodeAndDecompressPayload(it) }.toString())
          val doc =
            obj.getJSONObject("vc").getJSONObject("credentialSubject").getJSONObject("fhirBundle")
          return parser.parseResource(doc.toString()) as Bundle
        }
        else {
          println("2")
          healthData = healthData + "\n" + elem + "\n"
        }
      }
      // set the text view to the final outputted data
      return Bundle()
  }

  @RequiresApi(Build.VERSION_CODES.O)
  fun constructShlObj() {
    if (shlData?.fullLink != null) {
      val extractedJson = readShlUtils.extractUrl(shlData.fullLink!!)
      val decodedJson = readShlUtils.decodeUrl(extractedJson)
      shlData.shl = extractedJson
      // this gets you the url needed for the POST request
      val json = String(decodedJson, StandardCharsets.UTF_8)
      val jsonObject = JSONObject(json)
      println(jsonObject)
      val url = jsonObject.get("url")
      shlData.manifestUrl = url.toString()
      val key = jsonObject.get("key")
      shlData.key = key.toString()
      if (jsonObject.has("flag")) {
        val flags: String = jsonObject.getString("flag")
        shlData.flag = flags
      }
    }
  }
}
