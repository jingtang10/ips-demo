package com.example.library

interface SHLScanner {
  fun scanSHLQRCode() : SHLData
  fun onScanSuccess(callback : (SHLData) -> Unit)
  fun onScanFail(callback : (Error) -> Unit)
}