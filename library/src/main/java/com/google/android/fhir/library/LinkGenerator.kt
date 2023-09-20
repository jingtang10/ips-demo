package com.google.android.fhir.library

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.android.fhir.library.dataClasses.SHLData
import com.google.android.fhir.library.interfaces.SHLGenerator
import com.google.android.fhir.library.utils.GenerateShlUtils
import java.security.SecureRandom
import java.util.Base64

class LinkGenerator : SHLGenerator {

  val generateShlUtils = GenerateShlUtils()

  @RequiresApi(Build.VERSION_CODES.O)
  override fun generateKey(): String {
    val random = SecureRandom()
    val keyBytes = ByteArray(32)
    random.nextBytes(keyBytes)
    return Base64.getUrlEncoder().encodeToString(keyBytes)
  }

  @RequiresApi(Build.VERSION_CODES.O)
  override fun generateSHL(context: Context, shlData: SHLData, passcode: String): Bitmap? {
    var bitmap: Bitmap? = null
    generateShlUtils.generateAndPostPayload(passcode, shlData, context) { output ->
      bitmap = output
    }

    return bitmap
  }
}