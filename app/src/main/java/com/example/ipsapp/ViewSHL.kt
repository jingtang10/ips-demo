package com.example.ipsapp

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.util.Half.toFloat
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.nio.charset.StandardCharsets
import java.util.Base64
import java.util.EnumMap
import org.json.JSONObject


class ViewSHL : Activity() {

  val urlUtils = UrlUtils()

  // Need to encode and compress into JWE and JWT tokens here
  val file = "{\n" +
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
    val qrView = findViewById<ImageView>(R.id.qrCode)

    // need to encode and compress the payload
    val encodedPayload = urlUtils.encodeAndCompressPayload(file)
    Log.d("MyTag", "Encoded Payload: $encodedPayload")

    val passcode:String = intent.getStringExtra("passcode").toString()
    val expirationDate:String = intent.getStringExtra("expirationDate").toString()

    val passcodeField = findViewById<TextView>(R.id.passcode)
    val expirationDateField = findViewById<TextView>(R.id.expirationDate)
    passcodeField.text = passcode
    // expirationDateField.text = expirationDate


    // Look at this manifest url
    val manifestUrl = "http://localhost:8080/fhir/Bundle"
    val label = "Back-to-school immunizations for Oliver Brown"
    val flags = ""
    val key = "rxTgYlOaKJPFtcEd0qcceN8wEU4p94SqAwIWQe6uX7Q"

    val shLinkPayload = constructSHLinkPayload(manifestUrl, label, flags, key)

    // fix this link and put the logo in the middle
    val shLink = "https://viewer.example.org#shlink:/${shLinkPayload}"
    // val shLink = "shlink:/$shLinkPayload"

    // println(shLink)

    val shlLinkURI = shLink
    val logoPath = "app/src/main/assets/smart-logo.png"
    val logoScale = 0.06

    // val drawableResource = R.drawable.smart_logo
    // val drawable = ContextCompat.getDrawable(this, drawableResource)

    // if (drawable != null) {

      val qrCodeBitmap = generateQRCode(this, shlLinkURI, R.drawable.smart_logo)
      if (qrCodeBitmap != null) {
        qrView.setImageBitmap(qrCodeBitmap)
      }
    // }
  }
  @RequiresApi(Build.VERSION_CODES.O)
  fun constructSHLinkPayload(manifestUrl: String, label: String?, flags: String?, key: String): String {
    val payloadObject = JSONObject()
    payloadObject.put("url", manifestUrl)
    payloadObject.put("key", key)

    if (flags != null) {
      payloadObject.put("flag", flags)
    }

    if (label != null) {
      payloadObject.put("label", label)
    }

    val jsonPayload = payloadObject.toString()
    val base64EncodedPayload = base64UrlEncode(jsonPayload)
    // return "shlink:/$base64EncodedPayload"
    return base64EncodedPayload
  }

  @RequiresApi(Build.VERSION_CODES.O)
  fun base64UrlEncode(data: String): String {
    val bytes = data.toByteArray(StandardCharsets.UTF_8)
    return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
  }

  fun generateQRCode(context: Context, content: String, logoDrawableId: Int): Bitmap? {
    val logoScale = 6
    try {
      val hints = mutableMapOf<EncodeHintType, Any>()
      hints[EncodeHintType.MARGIN] = 2 // Adjust QR code margin as needed

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

  fun drawableToBitmap(context: Context, drawableId: Int): Bitmap {
    return try {
      val drawable = ContextCompat.getDrawable(context, drawableId)
      val bitmap = Bitmap.createBitmap(
        drawable!!.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
      )
      val canvas = Canvas(bitmap)
      drawable.setBounds(0, 0, canvas.width, canvas.height)
      drawable.draw(canvas)
      bitmap
    } catch (e: Exception) {
      e.printStackTrace()
      createBitmap(0, 0)
    }
  }




}
