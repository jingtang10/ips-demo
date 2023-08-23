package com.example.ipsapp


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {
    private val cameraPermissionRequest = 123

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val openCameraButton = findViewById<Button>(R.id.scanQRButton)
        openCameraButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED
            ) {
                openScanner()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    cameraPermissionRequest
                )
            }
        }

        val generateQRButton = findViewById<Button>(R.id.generateQRButton)
        generateQRButton.setOnClickListener {
            val i = Intent(this@MainActivity, SelectResources::class.java)
            // val i = Intent(this@MainActivity, CreatePasscode::class.java)
            startActivity(i)
        }
    }

    // when the button is pressed, go to the QR scanner activity
    private fun openScanner() {
        val i = Intent(this@MainActivity, Scanner::class.java)
        startActivity(i)
    }

    // ask for permission to use the camera
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == cameraPermissionRequest) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openScanner()
            }
        }
    }
}
