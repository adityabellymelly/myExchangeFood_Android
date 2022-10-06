package com.exchange.user.module.cart_module.model.payment_model

import com.google.gson.annotations.SerializedName

class UniqueIdentifier {
    @SerializedName("ErrorInfo")
    var errorInfo: Any? = null
    @SerializedName("OrderId")
    var orderId: Any? = null
    @SerializedName("OrderNumber")
    var orderNumber: Any? = null
    @SerializedName("TraceId")
    var traceId: String? = null

}
