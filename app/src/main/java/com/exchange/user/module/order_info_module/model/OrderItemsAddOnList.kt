package com.exchange.user.module.order_info_module.model

import com.google.gson.annotations.SerializedName

class OrderItemsAddOnList {
    @SerializedName("AddOnOptions")
    var addOnOptions: List<AddOnOption>? = null
    @SerializedName("ItemAddOnId")
    var itemAddOnId: Long? = null
    @SerializedName("Name")
    var name: String? = null

}