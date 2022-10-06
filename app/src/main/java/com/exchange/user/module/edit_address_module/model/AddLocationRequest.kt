package com.exchange.user.module.edit_address_module.model

import com.google.gson.annotations.SerializedName

class AddLocationRequest {
    @SerializedName("data")
    var data: DataAddress? = null
    @SerializedName("tId")
    var tId: String? = null
}