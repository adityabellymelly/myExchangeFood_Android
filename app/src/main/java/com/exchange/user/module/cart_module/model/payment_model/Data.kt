package com.exchange.user.module.cart_module.model.payment_model

import com.google.gson.annotations.SerializedName

class Data {
    @SerializedName("locationId")
    var locationId: Long? = null
    @SerializedName("tempOrderData")
    var tempOrderData: TempOrderData? = null
    @SerializedName("AAFESPayData")
    var aaFESPayData:AAFESPayData? = null
    class AAFESPayData(val traceId : String)
}