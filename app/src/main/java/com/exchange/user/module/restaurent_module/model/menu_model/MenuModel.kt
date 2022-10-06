package com.exchange.user.module.restaurent_module.model.menu_model

import com.google.gson.annotations.SerializedName

class MenuModel {
    @SerializedName("ASAP")
    var asap: String? = null
    @SerializedName("catList")
    var catList: List<CatList>? = null
    @SerializedName("Desc")
    var desc: String? = null
    @SerializedName("FO")
    var fo: String? = null
    @SerializedName("LT")
    var lt: String? = null
    @SerializedName("MenuId")
    var menuId: Long? = null
    @SerializedName("Name")
    var name: String? = null
    @SerializedName("imageRepository")
    var imageRepository : List<ImageRepository>? = null

}
