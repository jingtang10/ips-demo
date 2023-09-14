package com.google.android.fhir.library.interfaces

import com.google.android.fhir.library.dataClasses.SHLData

interface SHLScanner {
  fun scanSHLQRCode() : SHLData
  fun onScanSuccess(callback : (SHLData) -> Unit)
  fun onScanFail(callback : (Error) -> Unit)
}