package com.exchange.user.module.restaurent_module.model.menu_model

import com.google.gson.annotations.SerializedName

class ItemList {

    @SerializedName("AddOnList")
    var addOnList: ArrayList<AddOnList> = ArrayList()
    @SerializedName("Desc")
    var desc: String? = null
    @SerializedName("Icon1")
    var icon1: String? = null
    @SerializedName("Icon1Name")
    var icon1Name: String? = null
    @SerializedName("Icon2")
    var icon2: String? = null
    @SerializedName("Icon2Name")
    var icon2Name: String? = null
    @SerializedName("Icon3")
    var icon3: String? = null
    @SerializedName("Icon3Name")
    var icon3Name: String? = null
    @SerializedName("Icon4")
    var icon4: String? = null
    @SerializedName("Icon4Name")
    var icon4Name: String? = null
    @SerializedName("Id")
    var id: Long? = null
    @SerializedName("Img")
    var img: String? = null
    @SerializedName("ItemModList")
    var itemModList: List<ItemModList>? = null
    @SerializedName("MaxP")
    var maxP: Double? = null
    @SerializedName("MaxQ")
    var maxQ: Long? = null
    @SerializedName("MinP")
    var minP: Double? = null
    @SerializedName("MinQ")
    var minQ: Long? = null
    @SerializedName("Name")
    var name: String? = null
    @SerializedName("OpenOn")
    var openOn: OpenOn = OpenOn()
    @SerializedName("P1")
    var p1: Double = 0.0
    @SerializedName("P2")
    var p2: Double = 0.0
    @SerializedName("P3")
    var p3:Double = 0.0
    @SerializedName("P4")
    var p4:Double = 0.0
    @SerializedName("P5")
    var p5:Double = 0.0
    @SerializedName("P6")
    var p6:Double = 0.0
    @SerializedName("SpecialOffer")
    var specialOffer: SpecialOffer? = null
    @SerializedName("isShowforSuggestion")
    var isShowforSuggestion: String? = "True"
    @SerializedName("POSItemId")
    var POSItemId: String? = null
    @SerializedName("isTaxFree")
    var isTaxFree: Boolean? = true

}
