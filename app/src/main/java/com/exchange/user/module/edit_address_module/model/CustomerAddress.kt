package com.exchange.user.module.edit_address_module.model

import com.google.gson.annotations.SerializedName

class CustomerAddress {

    @SerializedName("addr1")
    var addr1: String? = null
    @SerializedName("addr2")
    var addr2: String? = null
    @SerializedName("addrName")
    var addrName: String? = null
    @SerializedName("city")
    var city: String? = null
    @SerializedName("custAddrId")
    var custAddrId: Long? = null
    @SerializedName("custId")
    var custId: Long? = null
    @SerializedName("fName")
    var fName: String? = null
    @SerializedName("instr")
    var instr: String? = null
    @SerializedName("isPrimary")
    var isPrimary: Long? = null
    @SerializedName("lName")
    var lName: String? = null
    @SerializedName("lat")
    var lat: Double? = null
    @SerializedName("lon")
    var lon: Double? = null
    @SerializedName("mName")
    var mName: String? = null
    @SerializedName("state")
    var state: String? = null
    @SerializedName("tel")
    var tel: String? = null
    @SerializedName("zip")
    var zip: String? = null
}