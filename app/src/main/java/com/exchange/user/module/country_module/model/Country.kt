package com.exchange.user.module.country_module.model

data class Country(
    val name : String,
    var image : String,
    var link : String,
    var isSelected : Boolean = false
)