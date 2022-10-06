package com.exchange.user.module.restaurent_module.model.data_model.reastaurent_mode

import com.google.gson.annotations.SerializedName

class Alert {

    @SerializedName("EndDate")
    var endDate: String? = null
    @SerializedName("StartDate")
    var startDate: String? = null
    @SerializedName("Text")
    var text: String? = null

}
