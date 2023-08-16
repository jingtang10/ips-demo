package com.example.ipsapp.utils

import android.content.Context
import org.apache.commons.lang3.tuple.MutablePair
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

  fun getDataFromDoc(doc : JSONObject, title : String, map : MutableMap<String, MutablePair<List<String>, ArrayList<JSONObject>>>) : MutableMap<String, MutablePair<List<String>, ArrayList<JSONObject>>> {
    val entryArray = doc.getJSONArray("entry")

    val displayList = ArrayList<String>()

    val pair = MutablePair<List<String>, ArrayList<JSONObject>>()

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

      if (condition) {

        // gets Problems, Allergies, Medications, Results
        if (resource.has("code") && resource.getJSONObject("code").has("coding")) {
          val codingArray = resource.getJSONObject("code").getJSONArray("coding")
          println(codingArray)
          println("Map $map")
          if (pair.right != null && pair.right.isNotEmpty()) {
            val list = pair.right
            list.add(resource)
            pair.right = list
            println("list exists")
          } else {
            pair.right = arrayListOf<JSONObject>(resource)
          }
          println("pair right: ${pair.right}")
          for (j in 0 until codingArray.length()) {
            val coding = codingArray.getJSONObject(j)
            val display = coding.getString("display")
            displayList.add(display)
            break
          }
        }

        // getting immunization target diseases
        else if (title == "Immunizations") {
          val protocolAppliedArray = resource.getJSONArray("protocolApplied")

          for (i in 0 until protocolAppliedArray.length()) {
            val targetDiseaseArray = protocolAppliedArray.getJSONObject(i).getJSONArray("targetDisease")

            for (j in 0 until targetDiseaseArray.length()) {
              val codingArray = targetDiseaseArray.getJSONObject(j).getJSONArray("coding")

              for (k in 0 until codingArray.length()) {
                val display = codingArray.getJSONObject(k).getString("display")
                displayList.add(display)
              }
            }
          }
        }
      }
    }
    pair.left = displayList
    map[title] = pair
    return map
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

  fun readFileFromAssets(context: Context, filename: String): String {
    return context.assets.open(filename).bufferedReader().use {
      it.readText()
    }
  }
}