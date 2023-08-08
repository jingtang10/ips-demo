package com.example.ipsapp.utils

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

  fun getDataFromDoc(doc : JSONObject, title : String, map : MutableMap<String, List<String>>) : MutableMap<String, List<String>> {
    val entryArray = doc.getJSONArray("entry")

    val displayList = ArrayList<String>()

    // Iterate through the entry array and filter based on criteria
    for (i in 0 until entryArray.length()) {
      val entry = entryArray.getJSONObject(i)
      val resource = entry.getJSONObject("resource")

      val resourceType = resource.getString("resourceType")
      var type = ""
      if (resource.has("type")) {
        type = resource.getString("type")
      }

      val condition = getSearchingCondition(title, resourceType, type)
      // println(title)
      // println(resourceType)
      // println(type)
      println(condition)
      if (condition) {
        val codingArray = resource.getJSONObject("code").getJSONArray("coding")
        for (j in 0 until codingArray.length()) {
          val coding = codingArray.getJSONObject(j)
          val display = coding.getString("display")
          displayList.add(display)
        }
      }
    }
    map[title] = displayList
    return map
  }

  fun getAllergiesFromDoc(doc : JSONObject) : List<String> {
    val entryArray = doc.getJSONArray("entry")

    val displayList = ArrayList<String>()

    // Iterate through the entry array and filter based on criteria
    for (i in 0 until entryArray.length()) {
      val entry = entryArray.getJSONObject(i)
      val resource = entry.getJSONObject("resource")

      val resourceType = resource.getString("resourceType")
      var type = ""
      if (resource.has("type")) {
        type = resource.getString("type")
      }

      if (resourceType == "AllergyIntolerance" && type == "allergy") {
        val codingArray = resource.getJSONObject("code").getJSONArray("coding")
        for (j in 0 until codingArray.length()) {
          val coding = codingArray.getJSONObject(j)
          val display = coding.getString("display")
          displayList.add(display)
        }
      }
    }
    return displayList
  }

  fun getMedicationFromDoc(doc : JSONObject) : List<String> {
    val entryArray = doc.getJSONArray("entry")

    val displayList = ArrayList<String>()

    // Iterate through the entry array and filter based on criteria
    for (i in 0 until entryArray.length()) {
      val entry = entryArray.getJSONObject(i)
      val resource = entry.getJSONObject("resource")

      val resourceType = resource.getString("resourceType")

      if (resourceType == "Medication") {
        val codingArray = resource.getJSONObject("code").getJSONArray("coding")
        for (j in 0 until codingArray.length()) {
          val coding = codingArray.getJSONObject(j)
          val display = coding.getString("display")
          displayList.add(display)
        }
      }
    }
    return displayList
  }

  fun getSearchingCondition(resource : String, resourceType : String, type : String) : Boolean {
    return when (resource) {
      "Allergies and Intolerances" -> resourceType == "AllergyIntolerance" && type == "allergy"
      "Medication" -> resourceType == "Medication"
      "Active Problems" -> resourceType == "Condition"
      "Immunizations" -> resourceType == "Immunization"
      "Results" -> resourceType == "Observation"
      "History of Past Illness" -> false // inside div
      "Plan of Treatment" -> false // inside div

      // titles have to change
      "procedure history" -> false
      "medical devices" -> false
      else -> false
    }
  }
}