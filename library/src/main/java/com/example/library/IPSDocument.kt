package com.example.library

import org.hl7.fhir.r4.model.Bundle
import org.hl7.fhir.r4.model.Patient

data class IPSDocument(
  var document : Bundle,
  var titles : ArrayList<Title>,
  var patient : Patient
) {
  constructor() : this(Bundle(), ArrayList<Title>(), Patient())
}
