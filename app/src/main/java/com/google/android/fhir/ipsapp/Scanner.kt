package com.example.ipsapp

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Parcelable
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.ipsapp.databinding.ScannerBinding
import com.google.android.fhir.library.SHLData
import com.google.android.fhir.library.Scanner
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.Detector.Detections
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import java.io.IOException

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
