package com.example.febafriends

data class Subcategory(
    val name: String,
    val bile: List<String>
){
    constructor(): this("",listOf())

}
