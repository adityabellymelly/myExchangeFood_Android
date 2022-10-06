package com.exchange.user.module.cart_module.model.payment_model

import com.google.gson.annotations.SerializedName

class CardRequest{
    @SerializedName("data")
    var data: Data? = null
    @SerializedName("tId")
    var tId: String? = null

}