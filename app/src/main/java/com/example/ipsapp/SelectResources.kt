package com.example.ipsapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ipsapp.fileExamples.file
import com.example.ipsapp.utils.DocumentUtils
import org.json.JSONObject

//maybe some checkboxes with some resource options -- something like that??
// and then a button like submit or something which goes to next screen, which can then encode and generate QR Code
// use placeholder/pre-written file atm - maybe the one received from the ips+shl document

// NEED TO ALSO ASK THEM FOR PASSCODE & EXPIRY DATE


class SelectResources : Activity() {

  val documentUtils = DocumentUtils()

  data class TitleItem(val title: String, var isChecked: Boolean = false)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // set the text view value to the body of the response from the POST
    setContentView(R.layout.select_resources)

    val listTitles = documentUtils.getTitlesFromIpsDoc(JSONObject(file))
    val listAllergies = documentUtils.getAllergiesFromDoc(JSONObject(file))
    println(listTitles)

    val titleList: ArrayList<TitleItem> = ArrayList()
    val allergiesList: ArrayList<TitleItem> = ArrayList()

    for (item in listTitles) {
      titleList.add(TitleItem(item))
    }

    for (item in listAllergies) {
      titleList.add(TitleItem(item))
    }

    val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
    recyclerView.layoutManager = LinearLayoutManager(this)
    val titleAdapter = TitleAdapter(titleList)
    recyclerView.adapter = titleAdapter

    val submitResourcesButton = findViewById<Button>(R.id.submitResourcesButton)
    submitResourcesButton.setOnClickListener {
      val selectedTitles = titleList.filter { it.isChecked }.map { it.title }
      val i = Intent(this@SelectResources, CreatePasscode::class.java)
      i.putStringArrayListExtra("selectedTitles", ArrayList(selectedTitles))
      startActivity(i)
    }
  }

  inner class TitleAdapter(private val titles: List<TitleItem>) :
    RecyclerView.Adapter<TitleAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
      val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
      val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      val view = LayoutInflater.from(parent.context)
        .inflate(R.layout.item_title_checkbox, parent, false)
      return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      val titleItem = titles[position]
      holder.titleTextView.text = titleItem.title
      holder.checkBox.isChecked = titleItem.isChecked

      holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
        titleItem.isChecked = isChecked
      }
    }

    override fun getItemCount(): Int {
      return titles.size
    }
  }

}