package com.google.android.fhir.library

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class Title(
  var name: String?,
  var dataEntries: ArrayList<ParcelableResourceWrapper>
) : Serializable {
  constructor(parcel: Parcel) : this(
    parcel.readString(),
    parcel.createTypedArrayList(ParcelableResourceWrapper) ?: ArrayList()
  ) {
  }

  constructor() : this("", ArrayList<ParcelableResourceWrapper>())
  constructor(name: String?) : this(name, ArrayList())

  companion object CREATOR : Parcelable.Creator<Title> {
    override fun createFromParcel(parcel: Parcel): Title {
      return Title(parcel)
    }

    override fun newArray(size: Int): Array<Title?> {
      return arrayOfNulls(size)
    }
  }
}
