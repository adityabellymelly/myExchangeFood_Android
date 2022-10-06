package com.exchange.user.module.cart_module.model.card_model

import com.google.gson.annotations.SerializedName

class ItemList {

    @SerializedName("Id")
    var id: Long? = null
    @SerializedName("Name")
    var name: String? = null
    @SerializedName("PortionId")
    var portionId: Long? = null
    @SerializedName("UnitPrice")
    var unitPrice: Double = 0.0
    @SerializedName("Qty")
    var qty: Long = 0
    @SerializedName("Amt")
    var amt: Double = 0.0
    @SerializedName("SpecialInstructions")
    var specialInstructions: String? = null
    @SerializedName("havespicialoffer")
    var havespicialoffer:Boolean = false
    @SerializedName("havespicialofferAmount")
    var havespicialofferAmount: Double = 0.0
    @SerializedName("categoryId")
    var categoryId: Long? = null
    @SerializedName("ItemAddOnList")
    var itemAddOnList: ArrayList<ItemsAddonList> = ArrayList()
    @SerializedName("ItemModList")
    var itemModList: List<Any>? = null

    var minQ: Long = 1
    var maxQ: Long = 1
    var isTaxFree:Boolean = true

}
