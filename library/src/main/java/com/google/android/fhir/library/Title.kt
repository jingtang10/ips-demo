package com.google.android.fhir.library

import android.os.Parcel
import android.os.Parcelable

data class Title(
  var name: String?,
  var dataEntries: ArrayList<ParcelableResourceWrapper>
) : Parcelable {
  constructor(parcel: Parcel) : this(
    parcel.readString(),
    parcel.createTypedArrayList(ParcelableResourceWrapper) ?: ArrayList()
  ) {
  }

  constructor() : this("", ArrayList<ParcelableResourceWrapper>())

  override fun describeContents(): Int {
    TODO("Not yet implemented")
  }

  override fun writeToParcel(p0: Parcel, p1: Int) {
    TODO("Not yet implemented")
  }

  companion object CREATOR : Parcelable.Creator<Title> {
    override fun createFromParcel(parcel: Parcel): Title {
      return Title(parcel)
    }

    override fun newArray(size: Int): Array<Title?> {
      return arrayOfNulls(size)
    }
  }
}
