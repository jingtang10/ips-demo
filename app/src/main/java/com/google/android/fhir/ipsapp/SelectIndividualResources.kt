package com.google.android.fhir.ipsapp

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.ViewModelProvider
import com.google.android.fhir.library.SHLData
import com.google.android.fhir.library.SelectIndividualResourcesViewModel
import java.io.Serializable

class SelectIndividualResources : AppCompatActivity() {

  private lateinit var viewModel: SelectIndividualResourcesViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.select_individual_resources)

    val composeView: ComposeView = findViewById(R.id.composeView)
    viewModel = ViewModelProvider(this)[SelectIndividualResourcesViewModel::class.java]
    composeView.setContent {
      viewModel.initializeData(this, onButtonClick = {
        val ipsDoc = viewModel.generateIPSDocument()
        val shlData = SHLData(ipsDoc)
        val i = Intent()
        i.component = ComponentName(this@SelectIndividualResources, CreatePasscode::class.java)
        i.putExtra("shlData", shlData as Serializable)
        startActivity(i)
      })
    }


    // val submitButton = findViewById<Button>(R.id.goToCreatePasscode)
    // submitButton.setOnClickListener {
    //   val ipsDoc = viewModel.generateIPSDocument()
    //   val shlData = SHLData(ipsDoc)
    //   val i = Intent()
    //   i.component = ComponentName(this@SelectIndividualResources, CreatePasscode::class.java)
    //   i.putExtra("shlData", shlData as Serializable)
    //   startActivity(i)
    // }
  }
}