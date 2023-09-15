package com.google.android.fhir.ipsapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.fhir.library.dataClasses.IPSDocument
import com.google.android.fhir.library.IPSRenderer


class GetData : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(com.google.android.fhir.library.R.layout.get_data)
    val doc = intent.getSerializableExtra("doc", IPSDocument::class.java)
    val ipsRenderer = IPSRenderer(doc)
    ipsRenderer.render(this)

  }

}