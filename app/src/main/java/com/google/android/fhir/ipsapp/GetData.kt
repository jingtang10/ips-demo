package com.google.android.fhir.ipsapp

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.fhir.library.IPSDocument


class GetData : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {

    super.onCreate(savedInstanceState)

    // set the text view value to the body of the response from the POST
    setContentView(R.layout.get_data)
    val textView = findViewById<TextView>(R.id.textView)
    textView.movementMethod = ScrollingMovementMethod()

    val doc = intent.getParcelableExtra("doc", IPSDocument::class.java)
    println(doc.toString())
  }

}