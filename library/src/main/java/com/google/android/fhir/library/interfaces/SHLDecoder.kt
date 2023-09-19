package com.google.android.fhir.library.interfaces

import com.google.android.fhir.library.dataClasses.IPSDocument

interface SHLDecoder {

  /* Sends a POST request to the manifest with the provided JSON body and decodes the returned value into an IPS document */
  suspend fun decodeSHLToDocument(jsonData: String): IPSDocument?

  /* */
  fun storeDocument(doc: IPSDocument)

  /* Checks if the SHL requires a passcode to access */
  fun hasPasscode(): Boolean

}