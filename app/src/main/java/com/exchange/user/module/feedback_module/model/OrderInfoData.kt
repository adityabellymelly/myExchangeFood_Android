package com.exchange.user.module.feedback_module.model

import com.google.gson.annotations.SerializedName

class OrderInfoData {

    @SerializedName("orderId")
    var orderId: Long? = null
    @SerializedName("isApp")
    var isApp: String? = "1"



}