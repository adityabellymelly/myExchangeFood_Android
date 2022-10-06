package com.exchange.user.module.profile_module.model

import com.google.gson.annotations.SerializedName

class OrderHistory {
    @SerializedName("CouponAmt")
    var couponAmt: String? = null
    @SerializedName("CouponCode")
    var couponCode: String? = null
    @SerializedName("CreatedOn")
    var createdOn: String? = null
    @SerializedName("DiscountAmt")
    var discountAmt: String? = null
    @SerializedName("Favorite")
    var favorite: String? = null
    @SerializedName("LocationId")
    var locationId: Long? = null
    @SerializedName("LocationName")
    var locationName: String? = null
    @SerializedName("OrderId")
    var orderId: Long? = null
    @SerializedName("OrderName")
    var orderName: String? = null
    @SerializedName("OrderNumber")
    var orderNumber: String? = null
    @SerializedName("PaymentType")
    var paymentType: String? = null
    @SerializedName("RestService")
    var restService: String? = null
    @SerializedName("Status")
    var status: String? = null
    @SerializedName("TotalAmt")
    var totalAmt: Double? = 0.0
    @SerializedName("locationAddress")
    var locationAddress: String = ""
}