package com.exchange.user.module.cart_module.model.payment_model

import com.google.gson.annotations.SerializedName

class TempOrderData {
    @SerializedName("CustEmail")
    var custEmail: String? = null
    @SerializedName("CustId")
    var custId: Long? = null
    @SerializedName("OrderJSON")
    var orderJSON: String? = null
    @SerializedName("RestTimeStamp")
    var restTimeStamp: String? = null
}