package com.exchange.user.module.restaurent_module.model.data_model.reastaurent_mode

import com.google.gson.annotations.SerializedName

class TimeZone {
    @SerializedName("HoursDifference")
    var hoursDifference: Long = 0
    @SerializedName("MinutesDifference")
    var minutesDifference: Long = 0
    @SerializedName("Name")
    var name: String? = null

}
