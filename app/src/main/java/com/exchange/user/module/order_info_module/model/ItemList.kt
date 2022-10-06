package com.exchange.user.module.order_info_module.model

import com.google.gson.annotations.SerializedName

class ItemList {
    @SerializedName("Amt")
    var amt: Long? = null
    @SerializedName("CatId")
    var catId: Long? = null
    @SerializedName("CatName")
    var catName: String? = null
    @SerializedName("Description")
    var description: String? = null
    @SerializedName("DiscountAmt")
    var discountAmt: Any? = null
    @SerializedName("Id")
    var id: Long? = null
    @SerializedName("ItemAddOnList")
    var itemAddOnList: List<OrderItemsAddOnList>? = null
    @SerializedName("ItemModList")
    var itemModList: List<Any>? = null
    @SerializedName("Name")
    var name: String? = null
    @SerializedName("NameOfThePerson")
    var nameOfThePerson: String? = null
    @SerializedName("PortionId")
    var portionId: String? = null
    @SerializedName("Price")
    var price: Double? = null
    @SerializedName("Qty")
    var qty: String? = null
    @SerializedName("SpecialInstructions")
    var specialInstructions: String? = null
    @SerializedName("UnitPrice")
    var unitPrice: String = "0.0"
    @SerializedName("PortionName")
    var portionName: String =""

}