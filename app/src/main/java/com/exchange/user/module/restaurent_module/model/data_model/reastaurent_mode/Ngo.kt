package com.exchange.user.module.restaurent_module.model.data_model.reastaurent_mode

import com.google.gson.annotations.SerializedName

class Ngo{

    @SerializedName("Address")
    var address: String? = null
    @SerializedName("DonateCode")
    var donateCode: String? = null
    @SerializedName("DonateValue")
    var donateValue: String = ""
    @SerializedName("Name")
    var name: String? = null
    @SerializedName("Photo")
    var photo: String? = null
    @SerializedName("RecId")
    var recId: Long = -1
    var isSelected: Boolean = false
    @SerializedName("Show")
    var show: String = "0"



}