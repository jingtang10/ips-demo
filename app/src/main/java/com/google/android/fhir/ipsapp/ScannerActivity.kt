package com.google.android.fhir.ipsapp

import android.os.Bundle
import android.view.SurfaceView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.fhir.library.Scanner

class ScannerActivity : AppCompatActivity() {

  private lateinit var scanner: Scanner

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.scanner)

    val surfaceView = findViewById<SurfaceView>(R.id.cameraSurfaceView)

    // Initialize the scanner
    scanner = Scanner(this, surfaceView.holder)

    // Call the scan method with callbacks
    scanner.scan(
      callback = { shlData ->
        // Handle successful scan result
      },
      failCallback = { error ->
        // Handle scan failure
      }
    )
  }
}
