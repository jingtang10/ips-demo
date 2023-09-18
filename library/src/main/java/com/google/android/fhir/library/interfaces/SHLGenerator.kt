package com.google.android.fhir.library.interfaces

import android.content.Context
import com.google.android.fhir.library.dataClasses.SHLData

interface SHLGenerator {
  // Should generateKey be part of this interface or should it just be in utils?
  fun generateKey() : String

  // Passcode added to generateSHL as it shouldn't be wrapped in the SHL object
  fun generateSHL(context: Context, data : SHLData, passcode: String) : SHLData
}