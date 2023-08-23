package com.google.android.fhir.library

import android.os.Parcel
import android.os.Parcelable
import org.hl7.fhir.r4.model.Bundle
import org.hl7.fhir.r4.model.Patient

data class IPSDocument(
  var document : Bundle?,
  var titles : ArrayList<Title>?,
  var patient : Patient?
) : Parcelable {
  constructor(parcel: Parcel) : this(
    parcel.readParcelable(Bundle::class.java.classLoader),
    parcel.createTypedArrayList(Title),
    parcel.readParcelable(Patient::class.java.classLoader)
  ) {
  }

  constructor() : this(Bundle(), ArrayList<Title>(), Patient())

  override fun writeToParcel(parcel: Parcel, flags: Int) {

  }

  override fun describeContents(): Int {
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
