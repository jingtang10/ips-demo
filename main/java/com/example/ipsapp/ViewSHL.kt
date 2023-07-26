package com.example.ipsapp

import android.app.Activity
import android.os.Bundle
import android.widget.TextView

class ViewSHL : Activity() {

  // Need to encode and compress into JWE and JWT tokens here
  // Also need to actually have the full file, not just the cut off thing I have right now

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.view_shl)

    val passcode:String = intent.getStringExtra("passcode").toString()
    val expirationDate:String = intent.getStringExtra("expirationDate").toString()

    val passcodeField = findViewById<TextView>(R.id.passcode)
    val expirationDateField = findViewById<TextView>(R.id.expirationDate)
    passcodeField.text = passcode
    expirationDateField.text = expirationDate
  }

}
