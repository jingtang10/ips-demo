package com.google.android.fhir.library

interface SHLDecoder {

  fun decodeSHLToDocument(recipient: String) : IPSDocument
  fun decodeSHLToDocument(recipient: String, passcode: String) : IPSDocument
  fun storeDocument(doc : IPSDocument)

  fun hasPasscode() : Boolean
}