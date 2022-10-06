package com.exchange.user.module.signin_module.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SignInRequest : Serializable {
    @SerializedName("tId")
    var tId: String = ""
    @SerializedName("data")
    var data : Data? = null
    class Data : Serializable{
        @SerializedName("customerFBData")
        var customerFBData:CustomerFBData?= null
        @SerializedName("username")
        var username: String? = null
        @SerializedName("password")
        var password: String? = null
        @SerializedName("oldpassword")
        var oldpassword: String? = null
        @SerializedName("customerData")
        var customerData:CustomerData?= null
        @SerializedName("locname")
        var locname: String? = null
        @SerializedName("checkOTP")
        var checkOTP: Int? = null
        @SerializedName("DeviceId")
        var deviceId: String? = null
        @SerializedName("DeviceType")
        var deviceType: String? = "1"
        @SerializedName("isapp")
        var isapp: String? = "1"
        @SerializedName("FCMToken")
        var FCMToken: String? = null
        @SerializedName("custId")
        var custId: Long? = null
    }
//
//    @SerializedName("data")
//    var dataPassword : PasswordData? = null
//
//    class PasswordData : Serializable{
//        @SerializedName("username")
//        var username: String? = null
//        @SerializedName("password")
//        var password: String? = null
//        @SerializedName("oldpassword")
//        var oldpassword: String? = null
//    }




    @SerializedName("checkOTP")
    var checkOTP: Int? = null

}

