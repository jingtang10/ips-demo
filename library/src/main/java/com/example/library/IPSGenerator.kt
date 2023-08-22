package com.example.library

interface IPSGenerator {
  fun generateKey() : String
  fun generateSHL(data : SHLData) : SHLData
}