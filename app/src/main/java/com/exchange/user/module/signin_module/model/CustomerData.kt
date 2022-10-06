package com.exchange.user.module.signin_module.model

import com.google.gson.annotations.SerializedName

class CustomerData {
    @SerializedName("tel")
    var tel: String = ""
    @SerializedName("OTP")
    var OTP: String = ""
    @SerializedName("optIn")
    var optIn: String? = null
    @SerializedName("fName")
    var fName: String? = null
    @SerializedName("mName")
    var mName: String?= null
    @SerializedName("lName")
    var lName: String? = null
    @SerializedName("eMail")
    var eMail: String?= null
    @SerializedName("cell")
    var cell: String?= null
    @SerializedName("mobileOptIn")
    var mobileOptIn: Int?= null
    @SerializedName("custId")
    var custId: Long? = null

}