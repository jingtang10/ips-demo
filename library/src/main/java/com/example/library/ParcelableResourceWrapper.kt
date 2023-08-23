package com.example.library

import android.os.Parcel
import android.os.Parcelable
import java.io.*

data class ParcelableResourceWrapper(
  val resourceData: ByteArray
) : Parcelable {
  constructor(parcel: Parcel) : this(parcel.createByteArray() ?: byteArrayOf())

  override fun writeToParcel(parcel: Parcel, flags: Int) {
    parcel.writeByteArray(resourceData)
  }

  override fun describeContents(): Int {
    return 0
  }

  companion object CREATOR : Parcelable.Creator<ParcelableResourceWrapper> {
    override fun createFromParcel(parcel: Parcel): ParcelableResourceWrapper {
      return ParcelableResourceWrapper(parcel)
    }

    override fun newArray(size: Int): Array<ParcelableResourceWrapper?> {
      return arrayOfNulls(size)
    }
  }
}

