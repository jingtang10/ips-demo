package com.google.android.fhir.ipsapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.fhir.document.IPSDocument


class GetData : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.get_data)
    val doc = intent.getSerializableExtra("doc", IPSDocument::class.java)
  }

}