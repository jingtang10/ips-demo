package com.google.android.fhir.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SuccessfulScanViewModelFactory(private val shlData: SHLData?) : ViewModelProvider.Factory {
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(SuccessfulScanViewModel::class.java)) {
      return shlData?.let { SuccessfulScanViewModel(it) } as T
    }
    throw IllegalArgumentException("Unknown ViewModel class")
  }
}
