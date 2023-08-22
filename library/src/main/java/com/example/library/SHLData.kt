package com.example.library

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class SHLData(
                  var fullLink : String?,
                  var shl : String?,
                  var qrBitmap : Bitmap?,
                  var manifestUrl : String?,
                  var key : String?,
                  var label : String?,
                  var flag : String?,
                  var passcode : String?,
                  var exp : String?,
                  var v : String?,
                  var ipsDoc : IPSDocument?) : Serializable, Parcelable {
  constructor(parcel: Parcel) : this(
    parcel.readString(),
    parcel.readString(),
    parcel.readParcelable(Bitmap::class.java.classLoader),
    parcel.readString(),
    parcel.readString(),
    parcel.readString(),
    parcel.readString(),
    parcel.readString(),
    parcel.readString(),
    parcel.readString(),
    parcel.readParcelable(IPSDocument::class.java.classLoader)
  ) {
  }

  constructor() : this("", "", Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888), "", "", "", "", "", "", "", IPSDocument())
  constructor(fullLink : String, bitmap : Bitmap) : this(fullLink, "", bitmap, "", "", "", "", "", "", "", IPSDocument())
  constructor(fullLink: String) : this(fullLink, "", Bitmap.createBitmap(100, 100,Bitmap.Config.ARGB_8888), "", "", "", "", "", "", "", IPSDocument())
  constructor(doc : IPSDocument) : this("","", Bitmap.createBitmap(100, 100,Bitmap.Config.ARGB_8888), "", "", "", "", "", "", "", doc)

  override fun writeToParcel(parcel: Parcel, flags: Int) {
    parcel.writeString(fullLink)
    parcel.writeString(shl)
    parcel.writeParcelable(qrBitmap, flags)
    parcel.writeString(manifestUrl)
    parcel.writeString(key)
    parcel.writeString(label)
    parcel.writeString(flag)
    parcel.writeString(passcode)
    parcel.writeString(exp)
    parcel.writeString(v)
  }

  override fun describeContents(): Int {
    return 0
  }

  companion object CREATOR : Parcelable.Creator<SHLData> {
    override fun createFromParcel(parcel: Parcel): SHLData {
      return SHLData(parcel)
    }

    override fun newArray(size: Int): Array<SHLData?> {
      return arrayOfNulls(size)
    }
  }

}
