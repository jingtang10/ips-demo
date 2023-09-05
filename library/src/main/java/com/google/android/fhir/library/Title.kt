package com.google.android.fhir.library

import java.io.Serializable
import org.hl7.fhir.r4.model.Resource

data class Title(
  var name: String?,
  var dataEntries: ArrayList<Resource>
) : Serializable {

  constructor() : this("", ArrayList<Resource>())
  constructor(name: String?) : this(name, ArrayList())

}
