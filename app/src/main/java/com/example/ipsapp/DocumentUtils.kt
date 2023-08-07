package com.example.ipsapp

import org.json.JSONObject

open class DocumentUtils {

  fun getTitlesFromIpsDoc(doc : JSONObject) : List<String> {
    val titleList = ArrayList<String>()

    val entryArray = doc.getJSONArray("entry")

    for (i in 0 until entryArray.length()) {
      val entryObject = entryArray.getJSONObject(i)
      val resourceObject = entryObject.getJSONObject("resource")

      if (resourceObject.has("section")) {
        val sectionArray = resourceObject.getJSONArray("section")

        for (j in 0 until sectionArray.length()) {
          val sectionObject = sectionArray.getJSONObject(j)
          val title = sectionObject.getString("title")
          titleList.add(title)
        }
      }
    }
    println(titleList)
    return titleList
  }
}