package com.example.ipsapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import ca.uhn.fhir.context.FhirContext
import ca.uhn.fhir.context.FhirVersionEnum
import com.google.android.fhir.library.R
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
import com.nimbusds.jose.EncryptionMethod
import com.nimbusds.jose.JWEAlgorithm
import com.nimbusds.jose.JWEHeader
import com.nimbusds.jose.JWEObject
import com.nimbusds.jose.Payload
import com.nimbusds.jose.crypto.DirectEncrypter
import java.nio.charset.StandardCharsets
import java.security.SecureRandom
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Base64
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject

class GenerateShlUtils {

  private val parser = FhirContext.forCached(FhirVersionEnum.R4).newJsonParser()

  fun getManifestUrl(): String {
    val httpClient: CloseableHttpClient = HttpClients.createDefault()
    val httpPost = HttpPost("https://api.vaxx.link/api/shl")
    httpPost.addHeader("Content-Type", "application/json")

    // Recipient and passcode entered by the user on this screen
    val jsonData = "{}"
    val entity = StringEntity(jsonData)

    httpPost.entity = entity

    return httpClient.execute(httpPost).use { response ->
      val responseBody = EntityUtils.toString(response.entity, StandardCharsets.UTF_8)
      responseBody
    }
  }

  @RequiresApi(Build.VERSION_CODES.O)
  fun generateRandomKey(): String {
    val random = SecureRandom()
    val keyBytes = ByteArray(32)
    random.nextBytes(keyBytes)
    println(keyBytes.toString())
    return Base64.getUrlEncoder().encodeToString(keyBytes)
  }

  @RequiresApi(Build.VERSION_CODES.O)
  fun encrypt(data: String, key: String): String {
    val header = JWEHeader(JWEAlgorithm.DIR, EncryptionMethod.A256GCM)
    val jweObj = JWEObject(header, Payload(data))
    val decodedKey = Base64.getUrlDecoder().decode(key)
    val encrypter = DirectEncrypter(decodedKey)

    jweObj.encrypt(encrypter)
    return jweObj.serialize()
  }

  @RequiresApi(Build.VERSION_CODES.O)
  fun postPayload(file: String, manifestUrl: String, key: String, managementToken: String) {

    // encode the file here (convert to JWE)
    // val encryptionKey = urlUtils.generateRandomKey()
    // Log.d("enc key", encryptionKey)

    // val contentJson = Gson().toJson(file.trim())
    val contentEncrypted = encrypt(file, key)
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
    httpPost.addHeader("Content-Type", "application/fhir+json")
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

  @RequiresApi(Build.VERSION_CODES.O)
  @OptIn(DelicateCoroutinesApi::class)
  fun generatePayload(
    passcode: String,
    shlData: SHLData,
    qrView: ImageView,
    context: Context,
  ) {
    val expirationDate = shlData.exp!!
    val labelData = shlData.label!!
    val bundle = shlData.ipsDoc!!.document
    GlobalScope.launch(Dispatchers.IO) {
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
      val key = generateRandomKey()
      val managementToken = jsonPostRes.getString("managementToken")

      var exp = ""
      if (expirationDate != "") {
        exp = dateStringToEpochSeconds(expirationDate).toString()
      }

      val shLinkPayload = constructSHLinkPayload(manifestUrl, labelData, flags, key, exp)

      // fix this link and put the logo in the middle
      // probably don't need the viewer
      val shLink = "https://demo.vaxx.link/viewer#shlink:/${shLinkPayload}"

      val qrCodeBitmap = generateQRCode(context, shLink)
      if (qrCodeBitmap != null) {
        val handler = Handler(Looper.getMainLooper())
        handler.post {
          qrView.setImageBitmap(qrCodeBitmap)
        }
      }
      println(shLinkPayload)

      var data = ""
      data = parser.encodeResourceToString(bundle)
      postPayload(data, manifestUrl, key, managementToken)
    }
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

  @RequiresApi(Build.VERSION_CODES.O)
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
    return base64UrlEncode(jsonPayload)
  }

  private fun convertDrawableToBitmap(drawable: Drawable, width: Int, height: Int): Bitmap {
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
  }
}