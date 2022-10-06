package com.exchange.user.module.restaurent_module.model.menu_model   //.model.menu_model

import com.google.gson.annotations.SerializedName

class SpecialOffer {

    @SerializedName("AllowCoupon")
    var allowCoupon: String? = null
    @SerializedName("AllowDiscount")
    var allowDiscount: String? = null
    @SerializedName("Amt1")
    var amt1: Double? = null
    @SerializedName("Amt2")
    var amt2: Double?  = null
    @SerializedName("Amt3")
    var amt3: Double? = null
    @SerializedName("DiscountAmt1")
    var discountAmt1: Double? = null
    @SerializedName("DiscountAmt2")
    var discountAmt2: Any? = null
    @SerializedName("DiscountAmt3")
    var discountAmt3: Any? = null
    @SerializedName("DiscountType")
    var discountType: Long? = null
    @SerializedName("Id")
    var id: Long? = null
    @SerializedName("MenuCategory1")
    var menuCategory1: Any? = null
    @SerializedName("MenuCategory2")
    var menuCategory2: Any? = null
    @SerializedName("MenuCategory3")
    var menuCategory3: Any? = null
    @SerializedName("MenuItem1")
    var menuItem1: Any? = null
    @SerializedName("MenuItem2")
    var menuItem2: Any? = null
    @SerializedName("MenuItem3")
    var menuItem3: Any? = null
    @SerializedName("Name")
    var name: String? = null
    @SerializedName("PreTax")
    var preTax: String? = null

}
