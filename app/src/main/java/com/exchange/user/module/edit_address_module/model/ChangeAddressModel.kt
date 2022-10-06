package com.exchange.user.module.edit_address_module.model

import com.exchange.user.module.profile_module.model.AddressBook
import com.google.gson.annotations.SerializedName

class ChangeAddressModel {
    @SerializedName("AddressBook")
    var addressBook: AddressBook? = null
    @SerializedName("custAddrId")
    var custAddrId: Long? = null
}