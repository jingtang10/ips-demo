package com.google.android.fhir.ipsapp

import androidx.lifecycle.ViewModel
import com.google.android.fhir.library.IPSDocument
import com.google.android.fhir.library.SHLData
import com.google.android.fhir.library.decode.Decoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SuccessfulScanViewModel(shlData: SHLData?) : ViewModel() {
  private val decoder = Decoder(shlData) // Initialize the decoder with null SHLData

  // Function to decode SHL data and return IPSDocument
  suspend fun decodeSHLToDocument(recipient: String, passcode: String? = null): IPSDocument {
    return withContext(Dispatchers.IO) {
      if (passcode != null) {
        decoder.decodeSHLToDocument(recipient, passcode)
      } else {
        decoder.decodeSHLToDocument(recipient)
      }
    }
  }

  suspend fun decodeSHLToDocument(recipient: String): IPSDocument {
    return withContext(Dispatchers.IO) {

        decoder.decodeSHLToDocument(recipient)

    }
  }

  // Function to check if SHLData has a passcode
  fun hasPasscode(shlData: SHLData?): Boolean {
    return decoder.hasPasscode()
  }
}