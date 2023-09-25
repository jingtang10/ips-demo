package com.google.android.fhir.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.fhir.library.dataClasses.SHLData

class SuccessfulScanViewModelFactory(private val shlData: SHLData?) : ViewModelProvider.Factory {

  /* Allows the viewModel to take in shlData as a parameter */
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(SuccessfulScanViewModel::class.java)) {
      return shlData?.let { SuccessfulScanViewModel(it) } as T
    }
    throw IllegalArgumentException("Unknown ViewModel class")
  }
}