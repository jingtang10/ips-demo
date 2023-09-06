package com.google.android.fhir.ipsapp

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.view.SurfaceView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.fhir.library.scan.Scanner
import java.io.Serializable

class ScannerActivity : AppCompatActivity() {

  private lateinit var scanner: Scanner

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.scanner)

    val surfaceView = findViewById<SurfaceView>(R.id.cameraSurfaceView)

    // Initialize the scanner and call scan
    scanner = Scanner(this, surfaceView.holder)
    scanner.scan(callback = { shlData ->
      // Handle successful scan result
      val i = Intent()
      i.component = ComponentName(this@ScannerActivity, SuccessfulScan::class.java)
      i.putExtra("shlData", shlData as Serializable)
      startActivity(i)
    }, failCallback = { error ->
      // Handle scan failure
      println("fail")
    })
  }

  // Release scanner resources when the activity is destroyed
  override fun onDestroy() {
    super.onDestroy()
    scanner.release()
  }
}
