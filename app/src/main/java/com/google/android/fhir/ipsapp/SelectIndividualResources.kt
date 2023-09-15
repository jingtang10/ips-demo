package com.google.android.fhir.ipsapp

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.fhir.library.SelectIndividualResourcesViewModel
import com.google.android.fhir.library.dataClasses.SHLData
import java.io.Serializable

class SelectIndividualResources : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.select_individual_resources)

    val containerLayout: LinearLayout = findViewById(R.id.containerLayout)
    val viewModel = ViewModelProvider(this)[SelectIndividualResourcesViewModel::class.java]
    viewModel.initializeData(this, containerLayout)


    val submitButton = findViewById<Button>(R.id.goToCreatePasscode)
    submitButton.setOnClickListener {
      val ipsDoc = viewModel.generateIPSDocument()
      val shlData = SHLData(ipsDoc)
      val i = Intent()
      i.component = ComponentName(this@SelectIndividualResources, CreatePasscode::class.java)
      i.putExtra("shlData", shlData as Serializable)
      startActivity(i)
    }
  }
}