package com.google.android.fhir.library.interfaces

import com.google.android.fhir.library.dataClasses.SHLData

interface SHLScanner {
  fun scanSHLQRCode(callback: (SHLData) -> Unit, failCallback: (Error) -> Unit)
  fun stopScan()
}