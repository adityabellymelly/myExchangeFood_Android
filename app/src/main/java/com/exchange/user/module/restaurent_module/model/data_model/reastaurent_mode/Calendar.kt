package com.exchange.user.module.restaurent_module.model.data_model.reastaurent_mode

import com.google.gson.annotations.SerializedName

class Calendar {

    @SerializedName("OpenOn")
    var openOn: OpenOn? = null
    @SerializedName("OpenOrCloseNextYearMask")
    var openOrCloseNextYearMask: String? = null
    @SerializedName("OpenOrCloseYearMask")
    var openOrCloseYearMask: String? = null

}
