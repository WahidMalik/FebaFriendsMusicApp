package com.example.febafriends

data class CategoryModel (
    val name : String,
    val songs : List<String>
){
    constructor(): this("",listOf())
}