package com.exchange.user.module.base_module.base_model

import com.google.gson.annotations.SerializedName

class ResponceModel {
    @SerializedName("data")
    var data: String? = null
    @SerializedName("message")
    var message: String? = null
    @SerializedName("serviceName")
    var serviceName: String? = null
    @SerializedName("serviceStatus")
    var serviceStatus: String = "F"

}

class ResponceModelSecond{
    @SerializedName("ServiceStatus")
    var serviceStatus: String? = null
    @SerializedName("data")
    var data: String? = null
    @SerializedName("Message")
    var message: String? = null



}