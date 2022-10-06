package com.exchange.user.module.restaurent_module.model.data_model.reastaurent_mode

import com.google.gson.annotations.SerializedName

class Menus {
    @SerializedName("ASAP")
    var asap: String? = null
    @SerializedName("CloseTime")
    var closeTime: String? = null
    @SerializedName("Desc")
    var desc: String? = null
    @SerializedName("DisplaySq")
    var displaySq: Long? = null
    @SerializedName("FO")
    var fo: String? = null
    @SerializedName("Id")
    var id: String? = null
    @SerializedName("LT")
    var lt: String? = null
    @SerializedName("Name")
    var name: String? = null
    @SerializedName("OpenOn")
    var openOn: OpenOn? = null
    @SerializedName("OpenTime")
    var openTime: String? = null

}
