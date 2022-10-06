package com.exchange.user.module.restaurent_module.model.menu_model

import com.google.gson.annotations.SerializedName

class AddOnOption {

    @SerializedName("Dflt")
    var dflt: Any? = null
    @SerializedName("Id")
    var id: Long? = null
    @SerializedName("Name")
    var name: String? = null
    @SerializedName("P1")
    var p1: Double = 0.0
    @SerializedName("P2")
    var p2:Double = 0.0
    @SerializedName("P3")
    var p3:Double = 0.0
    @SerializedName("P4")
    var p4:Double = 0.0
    @SerializedName("P5")
    var p5: Double = 0.0
    @SerializedName("P6")
    var p6:Double = 0.0

    var isSelected=false

}
