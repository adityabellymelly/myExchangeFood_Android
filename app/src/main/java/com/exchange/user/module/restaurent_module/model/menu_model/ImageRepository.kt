package com.exchange.user.module.restaurent_module.model.menu_model

import com.google.gson.annotations.SerializedName

class ImageRepository {

    @SerializedName("POSItemId")
    var POSItemId: String? = null

    @SerializedName("POSItemImage")
    var POSItemImage: String? = null

    @SerializedName("ItemDesc")
    var ItemDesc: String? = null

}