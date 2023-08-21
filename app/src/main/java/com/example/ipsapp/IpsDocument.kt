package com.example.ipsapp

import org.hl7.fhir.r4.model.Bundle
import org.hl7.fhir.r4.model.Patient

data class IpsDocument(
  var document : Bundle,
  var titles : ArrayList<Title>,
  var patient : Patient
) {
  constructor() : this(Bundle(), ArrayList<Title>(), Patient())
}
