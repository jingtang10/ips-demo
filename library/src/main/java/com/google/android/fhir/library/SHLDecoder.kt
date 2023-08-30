package com.google.android.fhir.library

interface SHLDecoder {

  suspend fun decodeSHLToDocument(recipient: String) : IPSDocument
  suspend fun decodeSHLToDocument(recipient: String, passcode: String) : IPSDocument
  fun storeDocument(doc : IPSDocument)

  fun hasPasscode() : Boolean
}