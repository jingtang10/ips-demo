package com.google.android.fhir.library

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable
import org.hl7.fhir.r4.model.Bundle
import org.hl7.fhir.r4.model.Patient

data class IPSDocument(
  var document : Bundle?,
  var titles : ArrayList<Title>?,
  var patient : Patient?
) : Serializable {
  constructor(parcel: Parcel) : this(
    parcel.readParcelable(Bundle::class.java.classLoader),
    parcel.createTypedArrayList(Title),
    parcel.readParcelable(Patient::class.java.classLoader)
  ) {
  }

  constructor() : this(Bundle(), ArrayList<Title>(), Patient())

  constructor(bundle : Bundle) : this(bundle, ArrayList<Title>(), Patient())

  fun writeToParcel(parcel: Parcel, flags: Int) {
    // parcel.writeParcelable(document, flags)
    // parcel.writeTypedList(titles)
    // parcel.writeParcelable(patient, flags)
  }

  fun describeContents(): Int {
    return 0
  }

  companion object CREATOR : Parcelable.Creator<IPSDocument> {
    override fun createFromParcel(parcel: Parcel): IPSDocument {
      return IPSDocument(parcel)
    }

    override fun newArray(size: Int): Array<IPSDocument?> {
      return arrayOfNulls(size)
    }
  }
}
