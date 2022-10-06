package com.exchange.user.module.cart_module.model.order_responce

import com.google.gson.annotations.SerializedName

class OrderResponce {
    @SerializedName("ErrorInfo")
    var errorInfo: List<ErrorInfo> = ArrayList()
    @SerializedName("OrderNumber")
    var orderNumber: String? = null
    @SerializedName("OrderId")
    var OrderId: String? = null

}