package com.exchange.user.module.profile_module.model

import com.google.gson.annotations.SerializedName

class AddressBook {
    @SerializedName("Addr1")
    var addr1: String? = null
    @SerializedName("Addr2")
    var addr2: String? = null
    @SerializedName("City")
    var city: String? = null
    @SerializedName("CustAddressBookId")
    var custAddressBookId: Long? = null
    @SerializedName("CustAddressName")
    var custAddressName: String? = null
    @SerializedName("FName")
    var fName: String? = null
    @SerializedName("Instructions")
    var instructions: String? = null
    @SerializedName("IsPrimaryAddress")
    var isPrimaryAddress: String? = null
    @SerializedName("LName")
    var lName: String? = null
    @SerializedName("Latitude")
    var latitude: Double  = 0.0
    @SerializedName("Longitude")
    var longitude: Double =  0.0
    @SerializedName("MName")
    var mName: String? = null
    @SerializedName("State")
    var state: String? = null
    @SerializedName("Telephone")
    var telephone: String? = null
    @SerializedName("ZIP")
    var zip: String? = null

}