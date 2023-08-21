package com.example.ipsapp

data class Title(
  var name : String,
  var titles : ArrayList<Title>
) {
  constructor() : this("", ArrayList<Title>())
}
