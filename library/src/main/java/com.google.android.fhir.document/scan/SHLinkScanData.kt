package com.google.android.fhir.document.scan

import com.google.android.fhir.document.IPSDocument
import com.google.android.fhir.document.decode.ReadSHLinkUtils
import java.io.Serializable
import java.nio.charset.StandardCharsets
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber

/**
 * Represents a SHL data structure, which stores information related to SHL content required for the
 * scanning process.
 *
 * SHLs, or Smart Health Links, are a standardized format for securely sharing health-related
 * information. For official documentation and specifications, see
 * [SHL Documentation](https://docs.smarthealthit.org/smart-health-links/).
 *
 * @property fullLink The full Smart Health Link (could include an optional SHL viewer).
 * @property encodedShlPayload The Base64Url-encoded SHL payload.
 * @property manifestUrl The URL to the SHL manifest.
 * @property key The key for decoding the data.
 * @property label A label describing the SHL data.
 * @property flag Flags indicating specific conditions or requirements (e.g., "P" for passcode).
 * @property expirationTime The expiration time of the SHL data.
 * @property versionNumber The version number of the SHL data.
 * @property ipsDoc The IPS document linked to by the SHL.
 */
data class SHLinkScanData(
  val fullLink: String,
  val encodedShlPayload: String,
  val manifestUrl: String,
  val key: String,
  val label: String,
  val flag: String,
  val expirationTime: String,
  val versionNumber: String,
  val ipsDoc: IPSDocument?,

) : Serializable {

  companion object {
    fun create(fullLink: String): SHLinkScanData {
      val extractedJson = ReadSHLinkUtils.extractUrl(fullLink)
      val decodedJson = ReadSHLinkUtils.decodeUrl(extractedJson)
      try {
        val jsonObject = JSONObject(String(decodedJson, StandardCharsets.UTF_8))
        return SHLinkScanData(
          fullLink,
          extractedJson,
          jsonObject.optString("url", ""),
          jsonObject.optString("key", ""),
          jsonObject.optString("label", ""),
          jsonObject.optString("flag", ""),
          jsonObject.optString("expirationTime", ""),
          jsonObject.optString("versionNumber", ""),
          null,
        )
      } catch (exception: JSONException) {
        Timber.e(exception, "Error creating JSONObject from decodedJson: $decodedJson")
        throw exception
      }
    }
  }
}
