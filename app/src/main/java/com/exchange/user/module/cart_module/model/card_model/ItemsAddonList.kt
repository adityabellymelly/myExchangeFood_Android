package com.exchange.user.module.cart_module.model.card_model

import com.google.gson.annotations.SerializedName

class ItemsAddonList {

    @SerializedName("AddOnOptions")
    var addOnOptions: ArrayList<AddOnOption> = ArrayList()
    @SerializedName("ItemAddOnId")
    var itemAddOnId: Long? = null
    @SerializedName("Name")
    var name: String ? = null
}