package com.example.ipsapp

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.ipsapp.utils.UrlUtils
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.methods.HttpPost
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.entity.StringEntity
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.impl.client.CloseableHttpClient
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.impl.client.HttpClients
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.util.EntityUtils
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Base64
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject


class ViewSHL : Activity() {

  private val urlUtils = UrlUtils()

  // Need to encode and compress into JWE and JWT tokens here
  private val file = "{\n" +
    "  \"resourceType\" : \"AllergyIntolerance\",\n" +
    "  \"id\" : \"allergyintolerance-with-abatement\",\n" +
    "  \"text\" : {\n" +
    "    \"status\" : \"extensions\",\n" +
    "    \"div\" : \"<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\"><p><b>Generated Narrative: AllergyIntolerance</b><a name=\\\"allergyintolerance-with-abatement\\\"> </a></p><div style=\\\"display: inline-block; background-color: #d9e0e7; padding: 6px; margin: 4px; border: 1px solid #8da1b4; border-radius: 5px; line-height: 60%\\\"><p style=\\\"margin-bottom: 0px\\\">Resource AllergyIntolerance &quot;allergyintolerance-with-abatement&quot; </p></div><p><b>Allergy abatement date</b>: 2010</p><p><b>clinicalStatus</b>: Resolved <span style=\\\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\\\"> (<a href=\\\"http://terminology.hl7.org/5.0.0/CodeSystem-allergyintolerance-clinical.html\\\">AllergyIntolerance Clinical Status Codes</a>#resolved)</span></p><p><b>verificationStatus</b>: Confirmed <span style=\\\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\\\"> (<a href=\\\"http://terminology.hl7.org/5.0.0/CodeSystem-allergyintolerance-verification.html\\\">AllergyIntolerance Verification Status</a>#confirmed)</span></p><p><b>code</b>: Ragweed pollen <span style=\\\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\\\"> (<a href=\\\"https://browser.ihtsdotools.org/\\\">SNOMED CT</a>#256303006)</span></p><p><b>patient</b>: <a href=\\\"Patient-66033.html\\\">Patient/66033</a> &quot; LUX-BRENNARD&quot;</p><p><b>onset</b>: </p></div>\"\n" +
    "  },\n" +
    "  \"extension\" : [{\n" +
    "    \"url\" : \"http://hl7.org/fhir/uv/ips/StructureDefinition/abatement-dateTime-uv-ips\",\n" +
    "    \"valueDateTime\" : \"2010\"\n" +
    "  }],\n" +
    "  \"clinicalStatus\" : {\n" +
    "    \"coding\" : [{\n" +
    "      \"system\" : \"http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical\",\n" +
    "      \"code\" : \"resolved\"\n" +
    "    }]\n" +
    "  },\n" +
    "  \"verificationStatus\" : {\n" +
    "    \"coding\" : [{\n" +
    "      \"system\" : \"http://terminology.hl7.org/CodeSystem/allergyintolerance-verification\",\n" +
    "      \"code\" : \"confirmed\"\n" +
    "    }]\n" +
    "  },\n" +
    "  \"code\" : {\n" +
    "    \"coding\" : [{\n" +
    "      \"system\" : \"http://snomed.info/sct\",\n" +
    "      \"code\" : \"256303006\",\n" +
    "      \"display\" : \"Ragweed pollen\"\n" +
    "    }]\n" +
    "  },\n" +
    "  \"patient\" : {\n" +
    "    \"reference\" : \"Patient/66033\"\n" +
    "  },\n" +
    "  \"_onsetDateTime\" : {\n" +
    "    \"extension\" : [{\n" +
    "      \"url\" : \"http://hl7.org/fhir/StructureDefinition/data-absent-reason\",\n" +
    "      \"valueCode\" : \"unknown\"\n" +
    "    }]\n" +
    "  }\n" +
    "}"

  @RequiresApi(Build.VERSION_CODES.O)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.view_shl)

    val passcode:String = intent.getStringExtra("passcode").toString()
    val labelData:String = intent.getStringExtra("label").toString()
    val expirationDate:String = intent.getStringExtra("expirationDate").toString()

    val passcodeField = findViewById<TextView>(R.id.passcode)
    val expirationDateField = findViewById<TextView>(R.id.expirationDate)
    passcodeField.text = passcode
    expirationDateField.text = expirationDate

    generatePayload(passcode, labelData, expirationDate)
  }

  @OptIn(DelicateCoroutinesApi::class)
  @RequiresApi(Build.VERSION_CODES.O)
  fun generatePayload(passcode: String, labelData: String, expirationDate: String) {
    val qrView = findViewById<ImageView>(R.id.qrCode)

    GlobalScope.launch(Dispatchers.IO) {
      val httpClient: CloseableHttpClient = HttpClients.createDefault()
      val httpPost = HttpPost("https://api.vaxx.link/api/shl")
      httpPost.addHeader("Content-Type", "application/json")

      // Recipient and passcode entered by the user on this screen
      val jsonData = "{}"
      val entity = StringEntity(jsonData)

      httpPost.entity = entity
      val response = httpClient.execute(httpPost)

      val responseBody = EntityUtils.toString(response.entity, StandardCharsets.UTF_8)
      Log.d("Response status: ", "${response.statusLine.statusCode}")
      Log.d("Response body: ", responseBody)

      httpClient.close()

      val jsonPostRes = JSONObject(responseBody)


      // Look at this manifest url
      val manifestUrl = "https://api.vaxx.link/api/shl/${jsonPostRes.getString("id")}"
      Log.d("manifest", manifestUrl)
      var flags = ""
      if (passcode != "") {
        flags = "P"
      }
      val key = urlUtils.generateRandomKey()
      val managementToken = jsonPostRes.getString("managementToken")

      var exp = ""
      if (expirationDate != "") {
        exp = dateStringToEpochSeconds(expirationDate).toString()
      }

      val shLinkPayload = constructSHLinkPayload(manifestUrl, labelData, flags, key, exp)

      // fix this link and put the logo in the middle
      // probably don't need the viewer
      val shLink = "https://demo.vaxx.link/viewer#shlink:/${shLinkPayload}"
      // val shLink = "shlink:/$shLinkPayload"

      // val logoPath = "app/src/main/assets/smart-logo.png"
      // val logoScale = 0.06

      // val drawableResource = R.drawable.smart_logo
      // val drawable = ContextCompat.getDrawable(this, drawableResource)

      // if (drawable != null) {

      val qrCodeBitmap = generateQRCode(this@ViewSHL, shLink)
      if (qrCodeBitmap != null) {
        runOnUiThread {
          qrView.setImageBitmap(qrCodeBitmap)
        }
      }
      println(shLinkPayload)

      postPayload(manifestUrl, key, managementToken)
    }
  }

  @RequiresApi(Build.VERSION_CODES.O)
  private fun postPayload(manifestUrl: String, key: String, managementToken: String) {

    // encode the file here (convert to JWE)
    // val encryptionKey = urlUtils.generateRandomKey()
    // Log.d("enc key", encryptionKey)

    // val contentJson = Gson().toJson(file.trim())
    val contentEncrypted = urlUtils.encrypt(file, key)
    Log.d("encrypted content", contentEncrypted)
    // Log.d("encryption key", key)

    val jwtHeader = "{\"zip\": \"DEF\", \"alg\": \"ES256\", \"kid\": \"${key}\"}"
    val finalContent = Base64.getUrlEncoder().withoutPadding()
      .encodeToString(jwtHeader.toByteArray()) + "." + contentEncrypted
    Log.d("final content", finalContent)

    // val encryptedContent = encryptContent(JSONObject(file), encryptionKey)


    val httpClient: CloseableHttpClient = HttpClients.createDefault()
    Log.d("manifest", manifestUrl)
    val httpPost = HttpPost("$manifestUrl/file")
    httpPost.addHeader("Content-Type", "application/smart-health-card")
    // httpPost.addHeader("Content-Length", file.length.toString())
    httpPost.addHeader("Authorization", "Bearer $managementToken")

    val entity = StringEntity(contentEncrypted)

    httpPost.entity = entity
    val response = httpClient.execute(httpPost)

    val responseBody = EntityUtils.toString(response.entity, StandardCharsets.UTF_8)
    Log.d("Response status: ", "${response.statusLine.statusCode}")
    Log.d("Response body: ", responseBody)
    httpClient.close()
  }

  @RequiresApi(Build.VERSION_CODES.O)
  fun constructSHLinkPayload(
    manifestUrl: String,
    label: String?,
    flags: String?,
    key: String,
    exp: String?,
  ): String {
    val payloadObject = JSONObject()
    payloadObject.put("url", manifestUrl)
    payloadObject.put("key", key)

    if (flags != "") {
      payloadObject.put("flag", flags)
    }

    if (label != "") {
      payloadObject.put("label", label)
    }

    if (exp != "") {
      payloadObject.put("exp", exp)
    }

    val jsonPayload = payloadObject.toString()
    return base64UrlEncode(jsonPayload)
  }

  // converts the inputted expiry date to epoch seconds
  @RequiresApi(Build.VERSION_CODES.O)
  fun dateStringToEpochSeconds(dateString: String): Long {
    val formatter = DateTimeFormatter.ofPattern("yyyy-M-d")
    val localDate = LocalDate.parse(dateString, formatter)
    val zonedDateTime = localDate.atStartOfDay(ZoneOffset.UTC)
    return zonedDateTime.toEpochSecond()
  }

  @RequiresApi(Build.VERSION_CODES.O)
  fun base64UrlEncode(data: String): String {
    val bytes = data.toByteArray(StandardCharsets.UTF_8)
    return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
  }

  private fun generateQRCode(context: Context, content: String): Bitmap? {
    // val logoScale = 0.06
    try {
      val hints = mutableMapOf<EncodeHintType, Any>()
      hints[EncodeHintType.MARGIN] = 2

      val qrCodeWriter = QRCodeWriter()
      val bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 512, 512, hints)
      val width = bitMatrix.width
      val height = bitMatrix.height
      val qrCodeBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

      for (x in 0 until width) {
        for (y in 0 until height) {
          qrCodeBitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
        }
      }

      return qrCodeBitmap
    } catch (e: WriterException) {
      e.printStackTrace()
      return null
    }
  }

  // this is for putting the logo in the qr
  // fun drawableToBitmap(context: Context, drawableId: Int): Bitmap {
  //   return try {
  //     val drawable = ContextCompat.getDrawable(context, drawableId)
  //     val bitmap = Bitmap.createBitmap(
  //       drawable!!.intrinsicWidth,
  //       drawable.intrinsicHeight,
  //       Bitmap.Config.ARGB_8888
  //     )
  //     val canvas = Canvas(bitmap)
  //     drawable.setBounds(0, 0, canvas.width, canvas.height)
  //     drawable.draw(canvas)
  //     bitmap
  //   } catch (e: Exception) {
  //     e.printStackTrace()
  //     createBitmap(0, 0)
  //   }
  // }




}
