package com.exchange.user.module.cart_module.model.order_responce

import com.google.gson.annotations.SerializedName

class ErrorInfo {
    @SerializedName("Text")
    var text: String? = null
}