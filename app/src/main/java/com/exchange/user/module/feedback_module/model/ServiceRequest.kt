package com.exchange.user.module.feedback_module.model

import com.google.gson.annotations.SerializedName

data class ServiceRequest (
    @SerializedName("tid")
    var tid: String? = null,
    @SerializedName("feedback1Cnt" )
    var feedback1Cnt : String? = null,
    @SerializedName("feedback2Cnt" )
    var feedback2Cnt : String? = null,
    @SerializedName("feedback3Cnt" )
    var feedback3Cnt : String? = null,
    @SerializedName("feedback4Cnt" )
    var feedback4Cnt : String? = null,
    @SerializedName("OrderId"      )
    var OrderId: String? = null,
    @SerializedName("dt")
    var dt: Long?    = null

)