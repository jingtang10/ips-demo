package com.example.library

import android.graphics.Bitmap
import java.io.Serializable

data class SHLData(
                  var fullLink : String,
                  var shl : String,
                  var qrBitmap : Bitmap,
                  var manifestUrl : String,
                  var key : String,
                  var label : String,
                  var flag : String,
                  var passcode : String,
                  var exp : String,
                  var v : String,
                  var ipsDoc : IPSDocument) : Serializable {
  constructor() : this("","", Bitmap.createBitmap(0, 0,Bitmap.Config.ARGB_8888), "", "", "", "", "", "", "", IPSDocument())
  constructor(fullLink : String, bitmap : Bitmap) : this(fullLink, "", bitmap, "", "", "", "", "", "", "", IPSDocument())
  constructor(fullLink: String) : this(fullLink, "", Bitmap.createBitmap(0, 0,Bitmap.Config.ARGB_8888), "", "", "", "", "", "", "", IPSDocument())
  constructor(doc : IPSDocument) : this("","", Bitmap.createBitmap(0, 0,Bitmap.Config.ARGB_8888), "", "", "", "", "", "", "", doc)

}
