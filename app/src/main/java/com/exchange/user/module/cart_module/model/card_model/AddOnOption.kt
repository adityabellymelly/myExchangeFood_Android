package com.exchange.user.module.cart_module.model.card_model

import com.google.gson.annotations.SerializedName

class AddOnOption {

    @SerializedName("AddOnOptionModifier1")
    var addOnOptionModifier1: AddOnOptionModifier? = null
    @SerializedName("AddOnOptionModifier2")
    var addOnOptionModifier2: AddOnOptionModifier? = null
    @SerializedName("Amt")
    var amt: Double  = 0.0
    @SerializedName("Dflt")
    var dflt: Any? = null
    @SerializedName("DisplayType")
    var displayType: Any? = null
    @SerializedName("Id")
    var id: Long? = null
    @SerializedName("isSelected")
    var isSelected: Boolean? = null
    @SerializedName("PortionId")
    var portionId: Long? = null
    @SerializedName("Qty")
    var qty: Int? = null
    @SerializedName("UnitPrice")
    var unitPrice: Double? = null
    @SerializedName("Name")
    var name: String? = null

}
