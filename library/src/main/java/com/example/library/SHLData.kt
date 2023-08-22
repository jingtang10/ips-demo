package com.example.library

import android.graphics.Bitmap

data class SHLData(var shl : String,
                   var qrBitmap : Bitmap,
                   var manifestUrl : String,
                   var key : String,
                   var label : String,
                   var flag : String,
                   var passcode : String,
                   var exp : String,
                   var v : String,
                   var ipsDoc : IPSDocument) {
  constructor() : this("", Bitmap.createBitmap(0, 0,Bitmap.Config.ARGB_8888), "", "", "", "", "", "", "", IPSDocument())
  constructor(shl : String, bitmap : Bitmap) : this(shl, bitmap, "", "", "", "", "", "", "", IPSDocument())
  constructor(doc : IPSDocument) : this("", Bitmap.createBitmap(0, 0, Bitmap.Config.ARGB_8888), "", "", "", "", "", "", "", doc)
}
