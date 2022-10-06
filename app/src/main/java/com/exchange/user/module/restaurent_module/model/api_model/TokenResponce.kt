package com.exchange.user.module.restaurent_module.model.api_model

import com.google.gson.annotations.SerializedName

class TokenResponce {

    @SerializedName("chainId")
    var chainId: Long? = null
    @SerializedName("CurrentDateTime")
    var currentDateTime: String? = null
    @SerializedName("custInfo")
    var custInfo: Any? = null
    @SerializedName("gatewayInfo")
    var gatewayInfo: Any? = null
    @SerializedName("isRetainTraceId")
    var isRetainTraceId: String? = null
    @SerializedName("locationCurrentDate")
    var locationCurrentDate: String? = null
    @SerializedName("locationId")
    var locationId: Long? = null
    @SerializedName("orderNumber")
    var orderNumber: Any? = null
    @SerializedName("setOrderInfo")
    var setOrderInfo: Any? = null
    @SerializedName("tId")
    var tId: String? = null
    @SerializedName("WCLUrl")
    var wclUrl: String? = null
    @SerializedName("WLCDetails")
    var wlcDetails: WLCDetails? = null

}
