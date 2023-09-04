package com.google.android.fhir.ipsapp

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import ca.uhn.fhir.context.FhirContext
import ca.uhn.fhir.context.FhirVersionEnum
import com.example.ipsapp.utils.GenerateShlUtils
import com.google.android.fhir.library.SHLData
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
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject


class GenerateSHL : Activity() {

  private val generateShlUtils = GenerateShlUtils()
  private val parser = FhirContext.forCached(FhirVersionEnum.R4).newJsonParser()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.view_shl)

    val passcode: String = intent.getStringExtra("passcode").toString()

    val shlData = intent.getParcelableExtra("shlData", SHLData::class.java)

    println("generation ${parser.encodeResourceToString(shlData?.ipsDoc?.document)}")

    val passcodeField = findViewById<TextView>(R.id.passcode)
    val expirationDateField = findViewById<TextView>(R.id.expirationDate)
    passcodeField.text = passcode
    expirationDateField.text = shlData?.exp

    if (shlData?.ipsDoc?.document != null) {
      generatePayload(
        passcode,
        shlData
      )
    }
  }

  @OptIn(DelicateCoroutinesApi::class)
  fun generatePayload(
    passcode: String,
    shlData: SHLData
  ) {
    val qrView = findViewById<ImageView>(R.id.qrCode)

    GlobalScope.launch(Dispatchers.IO) {
      val expirationDate = shlData.exp!!
      val labelData = shlData.label!!
      val httpClient: CloseableHttpClient = HttpClients.createDefault()
      val httpPost = HttpPost("https://api.vaxx.link/api/shl")
      httpPost.addHeader("Content-Type", "application/json")

      // Recipient and passcode entered by the user on this screen
      val jsonData: String
      var flags = ""
      if (passcode != "") {
        flags = "P"
        jsonData = "{\"passcode\" : \"$passcode\"}"
      } else {
        jsonData = "{}"
      }
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
      val key = generateShlUtils.generateRandomKey()
      val managementToken = jsonPostRes.getString("managementToken")

      var exp = ""
      if (expirationDate != "") {
        exp = generateShlUtils.dateStringToEpochSeconds(expirationDate).toString()
      }

      val shLinkPayload = constructSHLinkPayload(manifestUrl, labelData, flags, key, exp)


      val shLink = "https://demo.vaxx.link/viewer#shlink:/${shLinkPayload}"


      val qrCodeBitmap = generateQRCode(this@GenerateSHL, shLink)
      if (qrCodeBitmap != null) {
        runOnUiThread {
          qrView.setImageBitmap(qrCodeBitmap)
        }
      }
      println(shLinkPayload)

      val data = parser.encodeResourceToString(shlData.ipsDoc?.document)
      generateShlUtils.postPayload(data, manifestUrl, key, managementToken)
    }
  }

  private fun constructSHLinkPayload(
    manifestUrl: String,
    label: String?,
    flags: String?,
    key: String,
    exp: String?,
  ): String {
    val payloadObject = JSONObject()
    payloadObject.put("url", manifestUrl)
    payloadObject.put("key", key)

    payloadObject.put("flag", flags)

    if (label != "") {
      payloadObject.put("label", label)
    }

    if (exp != "") {
      payloadObject.put("exp", exp)
    }

    val jsonPayload = payloadObject.toString()
    return generateShlUtils.base64UrlEncode(jsonPayload)
  }

  private fun generateQRCode(context: Context, content: String): Bitmap? {
    val logoScale = 0.4
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

      val logoDrawable = ContextCompat.getDrawable(context, R.drawable.smart_logo)
      val logoAspectRatio =
        logoDrawable!!.intrinsicWidth.toFloat() / logoDrawable.intrinsicHeight.toFloat()

      val logoWidth = (width * logoScale).toInt()
      val logoHeight = (logoWidth / logoAspectRatio).toInt()

      val logoBitmap = convertDrawableToBitmap(logoDrawable, logoWidth, logoHeight)


      val backgroundBitmap =
        Bitmap.createBitmap(logoBitmap.width, logoBitmap.height, Bitmap.Config.RGB_565)
      backgroundBitmap.eraseColor(Color.WHITE)

      val canvas = Canvas(backgroundBitmap)
      canvas.drawBitmap(logoBitmap, 0f, 0f, null)

      val centerX = (width - logoBitmap.width) / 2
      val centerY = (height - logoBitmap.height) / 2

      val finalBitmap = Bitmap.createBitmap(qrCodeBitmap)
      val finalCanvas = Canvas(finalBitmap)
      finalCanvas.drawBitmap(backgroundBitmap, centerX.toFloat(), centerY.toFloat(), null)

      return finalBitmap
    } catch (e: WriterException) {
      e.printStackTrace()
      return null
    }
  }

  private fun convertDrawableToBitmap(drawable: Drawable, width: Int, height: Int): Bitmap {
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
  }
}
