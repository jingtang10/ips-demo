package com.google.android.fhir.ipsapp

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.fhir.library.FhirApplication
import com.google.android.fhir.library.SelectIndividualResourcesViewModel
import com.google.android.fhir.library.dataClasses.SHLData
import com.google.android.fhir.search.StringFilterModifier
import com.google.android.fhir.search.search
import java.io.Serializable
import kotlinx.coroutines.launch
import org.hl7.fhir.r4.model.Patient

class SelectIndividualResources : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.select_individual_resources)

    val containerLayout: LinearLayout = findViewById(R.id.containerLayout)
    val viewModel =
      ViewModelProvider(this@SelectIndividualResources)[SelectIndividualResourcesViewModel::class.java]

    lifecycleScope.launch {
      val fhirEngine = FhirApplication.fhirEngine(application)

      // triggerOneTimeSync()

      val patientBundlesFromWakefield = fhirEngine.search<org.hl7.fhir.r4.model.Bundle> {
        filter(Patient.ADDRESS_CITY, {
          modifier = StringFilterModifier.CONTAINS
          value = "Wakefield"
        })
      }
      println(patientBundlesFromWakefield.size)
      val a = patientBundlesFromWakefield.firstOrNull()
      val bundle = a as org.hl7.fhir.r4.model.Bundle
      viewModel.initializeData(this@SelectIndividualResources, containerLayout, bundle)
    }


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