package com.exchange.user.module.order_info_module.model

import com.google.gson.annotations.SerializedName

class AddOnOption {

    @SerializedName("AddOnOptionModifier1")
    var addOnOptionModifier1: AddOnOptionModifier2? = null
    @SerializedName("AddOnOptionModifier2")
    var addOnOptionModifier2: AddOnOptionModifier2? = null
    @SerializedName("Amt")
    var amt: Long? = null
    @SerializedName("Dflt")
    var dflt: String? = null
    @SerializedName("Id")
    var id: Long? = null
    @SerializedName("isSelected")
    var isSelected: Boolean? = null
    @SerializedName("Name")
    var name: String? = null
    @SerializedName("PortionId")
    var portionId: String? = null
    @SerializedName("Price")
    var price: Double? = null
    @SerializedName("Qty")
    var qty: Int? = null
    @SerializedName("UnitPrice")
    var unitPrice:Double = 0.0

}