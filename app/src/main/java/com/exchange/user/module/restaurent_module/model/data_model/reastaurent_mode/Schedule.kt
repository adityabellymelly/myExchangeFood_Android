package com.exchange.user.module.restaurent_module.model.data_model.reastaurent_mode

import com.google.gson.annotations.SerializedName

class Schedule {

    @SerializedName("CT1")
    var cT1: String? = null
    @SerializedName("CT2")
    var cT2: String? = null
    @SerializedName("Closed")
    var closed: String? = null
    @SerializedName("Day")
    var day: String? = null
    @SerializedName("FirstDel")
    var firstDel: String? = null
    @SerializedName("FirstOrd")
    var firstOrd: String? = null
    @SerializedName("LastDel")
    var lastDel: String? = null
    @SerializedName("LastOrd")
    var lastOrd: String? = null
    @SerializedName("OT1")
    var oT1: String? = null
    @SerializedName("OT2")
    var oT2: String? = null

}
