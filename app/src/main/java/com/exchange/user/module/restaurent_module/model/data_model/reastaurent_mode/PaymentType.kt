package com.exchange.user.module.restaurent_module.model.data_model.reastaurent_mode

import com.google.gson.annotations.SerializedName

class PaymentType {
    @SerializedName("Desc")
    var desc: String? = null
    @SerializedName("Icon")
    var icon: String? = null
    @SerializedName("Id")
    var id: Long? = null
    @SerializedName("Name")
    var name: String? = null
    @SerializedName("Type")
    var type: String? = null
    var isSelected:Boolean = false

}
