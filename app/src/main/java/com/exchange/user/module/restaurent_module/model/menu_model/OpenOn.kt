package com.exchange.user.module.restaurent_module.model.menu_model

import com.google.gson.annotations.SerializedName

class OpenOn {

    @SerializedName("Fri")
    var fri: String = "T"
    @SerializedName("Mon")
    var mon: String = "T"
    @SerializedName("Sat")
    var sat: String = "T"
    @SerializedName("Sun")
    var sun: String = "T"
    @SerializedName("Thu")
    var thu: String = "T"
    @SerializedName("Tue")
    var tue: String = "T"
    @SerializedName("Wed")
    var wed: String = "T"

}
