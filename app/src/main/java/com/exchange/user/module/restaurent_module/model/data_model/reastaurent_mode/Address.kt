package com.exchange.user.module.restaurent_module.model.data_model.reastaurent_mode

import com.google.gson.annotations.SerializedName

class Address {

    @SerializedName("AddressLine1")
    var addressLine1: String? = null
    @SerializedName("AddressLine2")
    var addressLine2: String? = null
    @SerializedName("City")
    var city: String? = null
    @SerializedName("Country")
    var country: String? = null
    @SerializedName("State")
    var state: String? = null
    @SerializedName("Zip")
    var zip: String? = null


}
