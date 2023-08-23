package com.google.android.fhir.library

import com.google.android.fhir.library.IPSDocument
import com.google.android.fhir.library.SHLData

interface SHLDecoder {
  fun decodeSHLToDocument(shlData: SHLData) : IPSDocument
  fun storeDocument(doc : IPSDocument)
}