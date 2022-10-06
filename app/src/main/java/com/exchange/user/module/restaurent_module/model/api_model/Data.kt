package com.exchange.user.module.restaurent_module.model.api_model

import com.google.gson.annotations.SerializedName

class Data {
    @SerializedName("mobUrl")
    var mobUrl: String? = null
    @SerializedName("PaypalPayerID")
    var paypalPayerID: Any? = null
    @SerializedName("PaypalToken")
    var paypalToken: Any? = null
    @SerializedName("pid")
    var pid: Any? = null
    @SerializedName("TempOrderId")
    var tempOrderId: Any? = null
    @SerializedName("TraceId")
    var traceId: String? = null

    @SerializedName("locationId")
    var locationId: Long? = null

    @SerializedName("menuId")
    var menuId: Long? = null


    @SerializedName("custId")
    var custId: Long? = null
}
