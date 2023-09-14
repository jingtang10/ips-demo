package com.google.android.fhir.library.interfaces

import com.google.android.fhir.library.dataClasses.SHLData

interface SHLGenerator {
  fun generateKey() : String
  fun generateSHL(data : SHLData) : SHLData
}