package com.google.android.fhir.library

import java.io.Serializable
import org.hl7.fhir.r4.model.Bundle
import org.hl7.fhir.r4.model.Patient

data class IPSDocument(
  var document : Bundle,
  var titles : ArrayList<Title>,
  var patient : Patient
) : Serializable {

  constructor() : this(Bundle(), ArrayList<Title>(), Patient())
  constructor(bundle : Bundle) : this(bundle, ArrayList<Title>(), Patient())


}
