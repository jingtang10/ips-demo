package com.example.library

interface SHLDecoder {
  fun decodeSHLToDocument(shlData: SHLData) : IPSDocument
  fun storeDocument(doc : IPSDocument)
}