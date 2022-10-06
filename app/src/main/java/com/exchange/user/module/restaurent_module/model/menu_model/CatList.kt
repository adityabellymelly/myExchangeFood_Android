package com.exchange.user.module.restaurent_module.model.menu_model

import com.google.gson.annotations.SerializedName

class CatList {

    @SerializedName("CatId")
    var catId: Long? = null
    @SerializedName("CatImage")
    var catImage: String? = null
    @SerializedName("CatName")
    var catName: String? = null
    @SerializedName("CatType")
    var catType: String? = null
    @SerializedName("CloseTime")
    var closeTime: String? = null
    @SerializedName("Desc")
    var desc: String? = null
    @SerializedName("ItemList")
    var itemList: ArrayList<ItemList> = ArrayList()
    @SerializedName("NewCatImage")
    var newCatImage: Any? = null
    @SerializedName("OpenTime")
    var openTime: String? = null
    @SerializedName("P1")
    var p1: P1? = null
    @SerializedName("P2")
    var p2: P1? = null
    @SerializedName("P3")
    var p3: P1? = null
    @SerializedName("P4")
    var p4: P1? = null
    @SerializedName("P5")
    var p5: P1? = null
    @SerializedName("P6")
    var p6: P1? = null
    @SerializedName("isShowItemImages")
    var isShowItemImages:String? = null
}
