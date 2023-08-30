package com.google.android.fhir.ipsapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ca.uhn.fhir.context.FhirContext
import ca.uhn.fhir.context.FhirVersionEnum
import com.example.ipsapp.utils.DocumentUtils
import com.google.android.fhir.library.DocumentGenerator
import com.google.android.fhir.library.IPSDocument
import com.google.android.fhir.library.utils.TitleAdapter
// NEED TO ALSO ASK THEM FOR PASSCODE & EXPIRY DATE

class SelectResources : Activity() {

  private val docUtils = DocumentUtils()
  private val parser = FhirContext.forCached(FhirVersionEnum.R4).newJsonParser()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val doc = docUtils.readFileFromAssets(this, "immunizationBundle.json")
    val bundle = IPSDocument(parser.parseResource(doc) as org.hl7.fhir.r4.model.Bundle)
    val documentGenerator = DocumentGenerator()

    // set the text view value to the body of the response from the POST
    setContentView(R.layout.select_resources)


    val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
    recyclerView.layoutManager = LinearLayoutManager(this)
    val titleList = documentGenerator.generateTitleAdapter(bundle)
    recyclerView.adapter = TitleAdapter(titleList)

    val submitResourcesButton = findViewById<Button>(R.id.submitResourcesButton)
    submitResourcesButton.setOnClickListener {
      val selectedTitles = titleList.filter { it.isChecked }.map { it.title }
      val i = Intent(this@SelectResources, SelectIndividualResources::class.java)
      i.putStringArrayListExtra("selectedTitles", ArrayList(selectedTitles))
      startActivity(i)
    }
  }
}