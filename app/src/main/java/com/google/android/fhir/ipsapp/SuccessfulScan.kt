package com.google.android.fhir.ipsapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.fhir.library.SHLData
import kotlinx.coroutines.launch

class SuccessfulScan : AppCompatActivity() {

  private lateinit var viewModel: SuccessfulScanViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.successfulscan)

    val shlData = intent.getParcelableExtra("shlData", SHLData::class.java)

    val viewModelFactory = SuccessfulScanViewModelFactory(shlData)
    viewModel = ViewModelProvider(this, viewModelFactory).get(SuccessfulScanViewModel::class.java)

    val passcodeEditText = findViewById<EditText>(R.id.passcode)
    val hasPasscode = viewModel.hasPasscode(shlData)

    if (!hasPasscode) {
      passcodeEditText.visibility = View.INVISIBLE
    }

    val button = findViewById<Button>(R.id.getData)
    button.setOnClickListener {
      val recipientField = findViewById<EditText>(R.id.recipient).text.toString()
      val passcodeField = passcodeEditText.text.toString()
      lifecycleScope.launch {
        fetchData(recipientField, passcodeField, hasPasscode)
      }
    }
  }

  private suspend fun fetchData(recipient: String, passcode: String, hasPasscode : Boolean) {
    val doc = if (hasPasscode) {
      viewModel.decodeSHLToDocument(recipient, passcode)
    } else {
      viewModel.decodeSHLToDocument(recipient)
    }
    // Handle the IPSDocument data here
    val i = Intent(this@SuccessfulScan, GetData::class.java)
    i.putExtra("doc", doc as java.io.Serializable)
    startActivity(i)
  }
}
