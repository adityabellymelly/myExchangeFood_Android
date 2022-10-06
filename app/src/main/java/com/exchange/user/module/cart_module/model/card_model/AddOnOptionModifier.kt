package com.exchange.user.module.cart_module.model.card_model

import com.google.gson.annotations.SerializedName

class AddOnOptionModifier {
    @SerializedName("Factor")
    var factor: Double? = null
    @SerializedName("Label")
    var label: String? = null
    @SerializedName("Text")
    var text: String? = null
    var isSelected : Boolean = false

}
