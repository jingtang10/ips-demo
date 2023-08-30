package com.google.android.fhir.library.scan

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.view.SurfaceHolder
import androidx.core.app.ActivityCompat
import com.google.android.fhir.library.SHLData
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.Detector.Detections
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import java.io.IOException

class Scanner(private val context: Context, private val surfaceHolder: SurfaceHolder) {

  private val requestCodeCameraPermission = 1001
  private lateinit var cameraSource: CameraSource
  private lateinit var barcodeDetector: BarcodeDetector
  private var scanCallback: ((SHLData) -> Unit)? = null
  private var failCallback: ((Error) -> Unit)? = null

  fun scan(callback: (SHLData) -> Unit, failCallback: (Error) -> Unit) {
    this.scanCallback = callback
    this.failCallback = failCallback

    if (hasCameraPermission()) {
      setup()
    } else {
      val error = Error("Camera permission not granted")
      failCallback.invoke(error)
    }
  }

  private fun setup() {
    barcodeDetector = BarcodeDetector.Builder(context)
      .setBarcodeFormats(Barcode.ALL_FORMATS)
      .build()

    cameraSource = CameraSource.Builder(context, barcodeDetector)
      .setRequestedPreviewSize(1920, 1080)
      .setAutoFocusEnabled(true)
      .build()

    surfaceHolder.addCallback(object : SurfaceHolder.Callback {
      override fun surfaceCreated(holder: SurfaceHolder) {
        try {
          // Start preview after 1s delay
          if (ActivityCompat.checkSelfPermission(
              context,
              Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
          ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
          }
          cameraSource.start(holder)
        } catch (e: IOException) {
          e.printStackTrace()
          failCallback?.invoke(Error("Failed to start camera"))
        }
      }

      override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        // Not needed for this example
      }

      override fun surfaceDestroyed(holder: SurfaceHolder) {
        cameraSource.stop()
      }
    })

    barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
      override fun release() {
        // Scanner has been closed
      }
      var scanSucceeded = false
      override fun receiveDetections(detections: Detections<Barcode>) {

        if (scanSucceeded) {
          return
        }

        val barcodes = detections.detectedItems
        if (barcodes.size() == 1) {
          val scannedValue = barcodes.valueAt(0).rawValue
          val shlData = SHLData(scannedValue)
          scanCallback?.invoke(shlData)
          scanSucceeded = true
        }
      }
    })
  }

  private fun stopScanning() {
    cameraSource.stop()
  }

  private fun hasCameraPermission(): Boolean {
    return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
  }

  fun release() {
    stopScanning()
    cameraSource.release()
    barcodeDetector.release()
  }
}
