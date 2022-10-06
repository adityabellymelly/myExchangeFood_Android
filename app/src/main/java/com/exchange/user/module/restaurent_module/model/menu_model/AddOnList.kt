package com.exchange.user.module.restaurent_module.model.menu_model

import com.google.gson.annotations.SerializedName

class AddOnList {

    @SerializedName("AddOnOptionModifier1")
    var addOnOptionModifier1: AddOnOptionModifier1? = null
    @SerializedName("AddOnOptionModifier2")
    var addOnOptionModifier2: AddOnOptionModifier1? = null
    @SerializedName("AddOnOptions")
    var addOnOptions: List<AddOnOption>? = null
    @SerializedName("Desc")
    var desc: String? = null
    @SerializedName("DispType")
    var dispType: String? = null
    @SerializedName("DsplyPrice")
    var dsplyPrice: String? = null
    @SerializedName("Id")
    var id: Long? = null
    @SerializedName("ItemAddOnId")
    var itemAddOnId: Long? = null
    @SerializedName("Max")
    var max: Long? = null
    @SerializedName("Min")
    var min: Long? = null
    @SerializedName("Name")
    var name: String? = null
    @SerializedName("Reqd")
    var reqd: String? = null

}
