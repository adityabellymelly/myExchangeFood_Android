package com.exchange.user.module.cart_module.model.card_model

import com.google.gson.annotations.SerializedName

class DeliveryInfo {
    @SerializedName("Addr1")
    var addr1: String? = null
    @SerializedName("Addr2")
    var addr2: String? = null
    @SerializedName("AddressId")
    var addressId: Long? = null
    @SerializedName("City")
    var city: String? = null
    @SerializedName("Instructions")
    var instructions: String? = null
    @SerializedName("Name")
    var name: String? = null
    @SerializedName("State")
    var state: String? = null
    @SerializedName("Telephone")
    var telephone: String? = null
    @SerializedName("Zip")
    var zip: String? = null
    @SerializedName("CustAddressName")
    var custAddressName : String? = null
    @SerializedName("Latitude")
    var latitude : Double = 0.0
    @SerializedName("Longitude")
    var longitude : Double = 0.0


    @SerializedName("FName")
    var fName : String? = null

    @SerializedName("LName")
    var lName : String? = null

    @SerializedName("MName")
    var mName : String? = null



}
