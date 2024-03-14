package com.google.android.fhir.document.scan

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.view.SurfaceHolder
import androidx.core.app.ActivityCompat
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.barcode.BarcodeDetector
import java.io.IOException

/** Manages camera operations for starting and stopping the camera preview. */
object CameraManager {

  /**
   * Starts the camera preview.
   *
   * @param context Context for camera initialization.
   * @param holder SurfaceHolder for displaying the camera preview.
   * @param cameraSource CameraSource for barcode detection.
   * @param failCallback Callback invoked if camera preview fails.
   */
  fun startCameraPreview(
    context: Context,
    holder: SurfaceHolder,
    cameraSource: CameraSource,
    failCallback: ((Error) -> Unit)?,
  ) {
    // Start the camera preview if camera permission is granted.
    if (hasCameraPermission(context)) {
      try {
        if (
          ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA,
          ) != PackageManager.PERMISSION_GRANTED
        ) {
          return
        }
        cameraSource.start(holder)
      } catch (e: IOException) {
        failCallback?.invoke(Error("Failed to start camera"))
      }
    }
  }

  /**
   * Checks if the app has camera permission.
   *
   * @param context Context for permission check.
   * @return True if camera permission is granted, false otherwise.
   */
  fun hasCameraPermission(context: Context): Boolean {
    return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
  }

  /**
   * Creates a CameraSource instance for barcode detection.
   *
   * @param context Context for camera source creation.
   * @param barcodeDetector BarcodeDetector for barcode detection.
   * @return CameraSource for barcode detection.
   */
  fun createCameraSource(context: Context, barcodeDetector: BarcodeDetector): CameraSource {
    return CameraSource.Builder(context, barcodeDetector)
      .setRequestedPreviewSize(1920, 1080)
      .setAutoFocusEnabled(true)
      .build()
  }
}
