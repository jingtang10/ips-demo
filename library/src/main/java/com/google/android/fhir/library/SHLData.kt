package com.google.android.fhir.library

import java.io.Serializable

data class SHLData(
  var fullLink: String,
  var shl: String,
  var manifestUrl: String,
  var key: String,
  var label: String,
  var flag: String,
  var passcode: String,
  var exp: String,
  var v: String,
  var ipsDoc: IPSDocument
) : Serializable {

  constructor() : this("", "", "", "", "", "", "", "", "", IPSDocument())
  constructor(fullLink : String) : this(fullLink, "",  "", "", "", "", "", "", "", IPSDocument())
  constructor(doc : IPSDocument) : this("", "",  "", "", "", "", "", "", "", doc)

}
