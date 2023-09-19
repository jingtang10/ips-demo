package com.google.android.fhir.library

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.google.android.fhir.library.dataClasses.IPSDocument
import com.google.android.fhir.library.dataClasses.SHLData
import com.google.android.fhir.library.decode.Decoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SuccessfulScanViewModel(shlData: SHLData?) : ViewModel() {

  private val decoder = Decoder(shlData)

  @RequiresApi(Build.VERSION_CODES.O)
  suspend fun decodeSHLToDocument(jsonBody: String): IPSDocument? {
    return withContext(Dispatchers.IO) {
      decoder.decodeSHLToDocument(jsonBody)
    }
  }

  fun hasPasscode(): Boolean {
    return decoder.hasPasscode()
  }

  @RequiresApi(Build.VERSION_CODES.O)
  fun constructSHL() {
    decoder.constructShlObj()
  }

  @RequiresApi(Build.VERSION_CODES.O)
  suspend fun fetchData(
    recipient: String,
    passcode: String,
    hasPasscode: Boolean,
  ): IPSDocument? {
    val jsonBody = if (hasPasscode) {
      "{\"passcode\":\"${passcode}\", \"recipient\":\"${recipient}\"}"
    } else {
      "{\"recipient\":\"${recipient}\"}"
    }
    return decodeSHLToDocument(jsonBody)
  }

}
