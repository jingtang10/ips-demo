package com.google.android.fhir.library.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.fhir.library.R

class TitleAdapter(private val titles: List<TitleItem>) :
  RecyclerView.Adapter<TitleAdapter.ViewHolder>() {

  data class TitleItem(val title: String, var isChecked: Boolean = false)
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