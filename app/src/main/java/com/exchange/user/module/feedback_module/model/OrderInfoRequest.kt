package com.exchange.user.module.feedback_module.model

import com.google.gson.annotations.SerializedName

class OrderInfoRequest {

    @SerializedName("data")
    var data: OrderInfoData? = null
    @SerializedName("tId")
    var tId: String? = null

    @SerializedName("dt")
    var dt: Long? = null
    @SerializedName("OrderId")
    var OrderId: Long? = null
    @SerializedName("tid")
    var tid: String? = null

}