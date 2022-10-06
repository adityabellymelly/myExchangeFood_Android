package com.exchange.user.module.restaurent_module.model.api_model

import com.google.gson.annotations.SerializedName

class TokenRequest {

    @SerializedName("AppCode")
    var appCode: String? = null
    @SerializedName("data")
    var data: Data? = null
    @SerializedName("mobUrl")
    var mobUrl: String? = null

    @SerializedName("tId")
    var tId: String? = null

}
