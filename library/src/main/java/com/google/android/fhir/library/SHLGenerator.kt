package com.google.android.fhir.library
interface SHLGenerator {
  fun generateKey() : String
  fun generateSHL(data : SHLData) : SHLData
}