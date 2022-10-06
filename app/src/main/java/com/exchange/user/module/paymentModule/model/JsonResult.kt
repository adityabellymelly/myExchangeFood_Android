package com.exchange.user.module.paymentModule.model

import com.google.gson.annotations.SerializedName

class JsonResult {

    @SerializedName("amount")
    var amount: String? = null
    @SerializedName("authorizationCode")
    var authorizationCode: String? = null
    @SerializedName("lastFourCardNumber")
    var lastFourCardNumber: String? = null
    @SerializedName("locationId")
    var locationId: String? = null
    @SerializedName("menuId")
    var menuId: String? = null
    @SerializedName("payResult")
    var payResult: String? = null
    @SerializedName("responseCode")
    var responseCode: String? = null
    @SerializedName("RspDescriptionField")
    var rspDescriptionField: String? = null
    @SerializedName("sessionId")
    var sessionId: String? = null
    @SerializedName("traceId")
    var traceId: String? = null

}
