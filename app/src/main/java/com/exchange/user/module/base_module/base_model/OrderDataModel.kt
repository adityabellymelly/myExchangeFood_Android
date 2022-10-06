package com.exchange.user.module.base_module.base_model

import com.google.gson.annotations.SerializedName

class OrderDataModel {
    @SerializedName("data")
    var data: OrderData? = null
    @SerializedName("tId")
    var tId: String? = null

    @SerializedName("chainId")
    var chainId: Long? = null
}