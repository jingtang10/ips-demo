package com.google.android.fhir.ipsapp

import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.google.android.fhir.library.IPSDocument
import com.google.android.fhir.library.IPSRenderer


class GetData : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(com.google.android.fhir.library.R.layout.activity_ips_renderer)

    val doc = intent.getSerializableExtra("doc") as IPSDocument
    val parentView: ViewGroup = findViewById(com.google.android.fhir.library.R.id.layout)
    // val ipsRenderer = IPSRenderer(tparentView)
    val rendererHandler = IPSRenderer(this, parentView)

    // Call the renderData method to render the data
    rendererHandler.renderData(doc)
  }
}
