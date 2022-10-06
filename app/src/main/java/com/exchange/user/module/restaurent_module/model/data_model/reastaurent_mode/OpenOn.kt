package com.exchange.user.module.restaurent_module.model.data_model.reastaurent_mode

import com.google.gson.annotations.SerializedName

class OpenOn {

    @SerializedName("Fri")
    var fri: String? = null
    @SerializedName("Mon")
    var mon: String? = null
    @SerializedName("Sat")
    var sat: String? = null
    @SerializedName("Sun")
    var sun: String? = null
    @SerializedName("Thu")
    var thu: String? = null
    @SerializedName("Tue")
    var tue: String? = null
    @SerializedName("Wed")
    var wed: String? = null

}
