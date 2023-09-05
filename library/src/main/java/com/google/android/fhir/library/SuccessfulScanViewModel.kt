package com.google.android.fhir.library

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.google.android.fhir.library.decode.Decoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SuccessfulScanViewModel(shlData: SHLData?) : ViewModel() {
  private val decoder = Decoder(shlData) // Initialize the decoder with null SHLData

  // Function to decode SHL data and return IPSDocument
  @RequiresApi(Build.VERSION_CODES.O)
  suspend fun decodeSHLToDocument(recipient: String, passcode: String? = null): IPSDocument {
    return withContext(Dispatchers.IO) {
      if (passcode != null) {
        decoder.decodeSHLToDocument(recipient, passcode)
      } else {
        decoder.decodeSHLToDocument(recipient)
      }
    }
  }

  @RequiresApi(Build.VERSION_CODES.O)
  suspend fun decodeSHLToDocument(recipient: String): IPSDocument {
    return withContext(Dispatchers.IO) {
      decoder.decodeSHLToDocument(recipient)
    }
  }

  // Function to check if SHLData has a passcode
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
  ): IPSDocument {
    val doc = if (hasPasscode) {
      decodeSHLToDocument(recipient, passcode)
    } else {
      decodeSHLToDocument(recipient)
    }
    return doc
  }

}
