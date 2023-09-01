package com.google.android.fhir.ipsapp

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.DatePicker
import android.widget.EditText
import androidx.annotation.RequiresApi
import com.google.android.fhir.library.IPSDocument
import java.util.Calendar

class CreatePasscode : Activity() {

  @RequiresApi(Build.VERSION_CODES.O)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.create_passcode)

    val datePicker = findViewById<DatePicker>(R.id.datePicker)
    var expirationDate = ""

    val ipsDoc = intent.getSerializableExtra("ipsDoc", IPSDocument::class.java)

    // Do I need to include time with this?
    val today: Calendar = Calendar.getInstance()
    datePicker.minDate = today.timeInMillis

    val submitResourcesButton = findViewById<Button>(R.id.generateSHL)
    val checkboxDate = findViewById<CheckBox>(R.id.checkboxDate)

    /*
     When the submit button is pressed, the state of the checkbox is checked, and the passcode
     and expiration date are added to the intent to be passed into the next activity.
     They are empty strings if they haven't been inputted
    */
    submitResourcesButton.setOnClickListener {
      val i = Intent(this@CreatePasscode, GenerateSHL::class.java)

      val passcodeField = findViewById<EditText>(R.id.passcode).text.toString()
      val labelField = findViewById<EditText>(R.id.label).text.toString()
      if (!checkboxDate.isChecked) {
        expirationDate = ""
      }
      i.putExtra("expirationDate", expirationDate)
      i.putExtra("passcode", passcodeField)
      i.putExtra("label", labelField)
      i.putExtra("ipsDoc", ipsDoc)
      startActivity(i)
    }

    // Set the initial state of the DatePicker based on the Checkbox state
    datePicker.isEnabled = checkboxDate.isChecked
    checkboxDate.setOnCheckedChangeListener { _, isChecked ->
      datePicker.isEnabled = isChecked
    }

    // updates the variable to the selected date
    datePicker.setOnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
      expirationDate = "$year-${monthOfYear + 1}-$dayOfMonth"
    }
  }
}