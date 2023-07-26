package com.example.ipsapp

import android.app.Activity
import android.os.Bundle
import android.widget.DatePicker
import java.util.Calendar

class CreatePasscode : Activity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.create_passcode)

    val datePicker = findViewById<DatePicker>(R.id.datePicker)

    // disables dates before today - can't set expiration date in the past
    // Do I need to include time with this?
    val today: Calendar = Calendar.getInstance()
    datePicker.minDate = today.timeInMillis
  }
}