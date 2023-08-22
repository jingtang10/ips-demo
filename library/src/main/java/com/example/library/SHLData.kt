package com.example.library

import android.graphics.Bitmap
import org.w3c.dom.Document

data class SHLData(var shl : String,
                   var qrBitmap : Bitmap,
                   var manifestUrl : String,
                   var key : String,
                   var label : String,
                   var flag : String,
                   var passcode : String,
                   var exp : String,
                   var v : String,
                   var ipsDoc : IpsDocument) {
  constructor() : this("", Bitmap.createBitmap(0, 0,Bitmap.Config.ARGB_8888), "", "", "", "", "", "", "", IpsDocument())
  constructor(shl : String, bitmap : Bitmap) : this(shl, bitmap, "", "", "", "", "", "", "", IpsDocument())
  constructor(doc : IpsDocument) : this("", Bitmap.createBitmap(0, 0,Bitmap.Config.ARGB_8888), "", "", "", "", "", "", "", doc)
}
