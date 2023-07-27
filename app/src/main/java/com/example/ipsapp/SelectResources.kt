package com.example.ipsapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.example.IpsApp.R

//maybe some checkboxes with some resource options -- something like that??
// and then a button like submit or something which goes to next screen, which can then encode and generate QR Code
// use placeholder/pre-written file atm - maybe the one received from the ips+shl document

// NEED TO ALSO ASK THEM FOR PASSCODE & EXPIRY DATE


class SelectResources : Activity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // set the text view value to the body of the response from the POST
    setContentView(R.layout.select_resources)

    val submitResourcesButton = findViewById<Button>(R.id.submitResourcesButton)
    submitResourcesButton.setOnClickListener {
      val i = Intent(this@SelectResources, CreatePasscode::class.java)
      startActivity(i)
    }
  }
}