package com.example.ipsapp

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.ipsapp.databinding.ScannerBinding
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.Detector.Detections
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import java.io.IOException


class Scanner : AppCompatActivity() {
  private val requestCodeCameraPermission = 1001
  private lateinit var cameraSource: CameraSource
  private lateinit var barcodeDetector: BarcodeDetector
  private var scannedValue = ""
  private lateinit var binding: ScannerBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ScannerBinding.inflate(layoutInflater)
    val view = binding.root
    setContentView(view)


    if (ContextCompat.checkSelfPermission(
        this@Scanner, android.Manifest.permission.CAMERA
      ) != PackageManager.PERMISSION_GRANTED
    ) {
      askForCameraPermission()
    } else {
      setupControls()
    }

    val aniSlide: Animation =
      AnimationUtils.loadAnimation(this@Scanner, R.anim.scanner_animation)
    binding.barcodeLine.startAnimation(aniSlide)
  }


  private fun setupControls() {
    barcodeDetector =
      BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.ALL_FORMATS).build()

    cameraSource = CameraSource.Builder(this, barcodeDetector)
      .setRequestedPreviewSize(1920, 1080)
      .setAutoFocusEnabled(true) //you should add this feature
      .build()

    binding.cameraSurfaceView.getHolder().addCallback(object : SurfaceHolder.Callback {
      @SuppressLint("MissingPermission")
      override fun surfaceCreated(holder: SurfaceHolder) {
        try {
          //Start preview after 1s delay
          cameraSource.start(holder)
        } catch (e: IOException) {
          e.printStackTrace()
        }
      }

      @SuppressLint("MissingPermission")
      override fun surfaceChanged(
        holder: SurfaceHolder,
        format: Int,
        width: Int,
        height: Int,
      ) {
        try {
          cameraSource.start(holder)
        } catch (e: IOException) {
          e.printStackTrace()
        }
      }

      override fun surfaceDestroyed(holder: SurfaceHolder) {
        cameraSource.stop()
      }
    })


    barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
      override fun release() {
        Toast.makeText(applicationContext, "Scanner has been closed", Toast.LENGTH_SHORT)
          .show()
      }

      override fun receiveDetections(detections: Detections<Barcode>) {
        val barcodes = detections.detectedItems
        if (barcodes.size() == 1) {
          scannedValue = barcodes.valueAt(0).rawValue

          runOnUiThread {
            cameraSource.stop()
            val i = Intent(this@Scanner, SuccessfulScan::class.java)
            Log.d("Extracted Data", scannedValue)
            i.putExtra("scannedData", scannedValue)
            startActivity(i)
          }
        }
      }
    })
  }

  private fun askForCameraPermission() {
    ActivityCompat.requestPermissions(
      this@Scanner,
      arrayOf(android.Manifest.permission.CAMERA),
      requestCodeCameraPermission
    )
  }

  override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray,
  ) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    if (requestCode == requestCodeCameraPermission && grantResults.isNotEmpty()) {
      if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        setupControls()
      } else {
        Toast.makeText(applicationContext, "Permission Denied", Toast.LENGTH_SHORT).show()
      }
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    cameraSource.stop()
  }
}