package com.example.ipsapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.ipsapp.utils.UrlUtils
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.methods.HttpGet
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.methods.HttpPost
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.entity.StringEntity
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.impl.client.CloseableHttpClient
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.impl.client.HttpClients
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.util.EntityUtils
import java.nio.charset.StandardCharsets
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject


class SuccessfulScan : AppCompatActivity() {

  private val urlUtils = UrlUtils()

  @RequiresApi(Build.VERSION_CODES.O)
  override fun onCreate (savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.successfulscan)


    val scannedData:String = intent.getStringExtra("scannedData").toString()
    val extractedJson = urlUtils.extractUrl(scannedData)
    val decodedJson = urlUtils.decodeUrl(extractedJson)

    val passcodeEditText = findViewById<EditText>(R.id.passcode)
    passcodeEditText.visibility = View.INVISIBLE

    // this gets you the url needed for the POST request
    val json = String(decodedJson, StandardCharsets.UTF_8)
    val jsonObject = JSONObject(json)
    println(jsonObject)
    val url = jsonObject.get("url")
    val key = jsonObject.get("key")
    if (jsonObject.has("flag")) {
      val flags : String = jsonObject.getString("flag")
      for (i in flags.indices) {
        if (flags[i] == 'P') {
          passcodeEditText.visibility = View.VISIBLE
        }
      }
    }
    Log.d("url", url.toString())
    Log.d("key", key.toString())

    // when button is pushed, the inputted data is passed into fetchData()
    val button = findViewById<Button>(R.id.getData)
    button.setOnClickListener {
      val recipientField = findViewById<EditText>(R.id.recipient).text.toString()
      val passcodeField = passcodeEditText.text.toString()
      fetchData(url.toString(), recipientField, passcodeField, key.toString())
    }
  }


  // Handles the POST request and passes the response body into the next activity
  @OptIn(DelicateCoroutinesApi::class)
  private fun fetchData (url: String, recipient: String, passcode: String, key: String) {
    GlobalScope.launch(Dispatchers.IO) {
      val httpClient: CloseableHttpClient = HttpClients.createDefault()
      val httpPost = HttpPost(url)
      httpPost.addHeader("Content-Type", "application/smart-health-card")

      // Recipient and passcode entered by the user on this screen
      val jsonData : String = if (passcode == "") {
        "{\"recipient\":\"${recipient}\"}"
      } else {
        "{\"passcode\":\"${passcode}\", \"recipient\":\"${recipient}\"}"
      }
      val entity = StringEntity(jsonData)

      httpPost.entity = entity
      val response = httpClient.execute(httpPost)

      val responseBody = EntityUtils.toString(response.entity, StandardCharsets.UTF_8)
      Log.d("Response status: ", "${response.statusLine.statusCode}")
      Log.d("Response body: ", responseBody)
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
          getRequest(loc)?.let { embeddedList.add(it) }
          Log.d("here", loc)
        }
      }

      val embeddedArray = embeddedList.toTypedArray()

      launch(Dispatchers.Main) {
        val i = Intent(this@SuccessfulScan, GetData::class.java)
        i.putExtra("embeddedArray", embeddedArray)
        i.putExtra("key", key)
        startActivity(i)
      }
    }
  }

  private fun getRequest(url: String): String? {
    val httpClient: CloseableHttpClient = HttpClients.createDefault()
    val httpGet = HttpGet(url)

    val response = httpClient.execute(httpGet)

    val responseBody = EntityUtils.toString(response.entity, StandardCharsets.UTF_8)
    Log.d("Response status: ", "${response.statusLine.statusCode}")
    Log.d("Response body: ", responseBody)
    httpClient.close()

    return responseBody
  }
}