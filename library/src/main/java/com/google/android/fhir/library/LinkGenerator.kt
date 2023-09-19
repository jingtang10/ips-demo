package com.google.android.fhir.library

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.android.fhir.library.dataClasses.SHLData
import com.google.android.fhir.library.interfaces.SHLGenerator
import java.security.SecureRandom
import java.util.Base64

class LinkGenerator : SHLGenerator {
  @RequiresApi(Build.VERSION_CODES.O)
  override fun generateKey(): String {
    val random = SecureRandom()
    val keyBytes = ByteArray(32)
    random.nextBytes(keyBytes)
    return Base64.getUrlEncoder().encodeToString(keyBytes)
  }

  override fun generateSHL(context: Context, data: SHLData, passcode: String): SHLData {
    TODO("Not yet implemented")
  }

}