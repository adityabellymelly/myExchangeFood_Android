package com.exchange.user.module.restaurent_module.model.data_model.reastaurent_mode

import com.google.gson.annotations.SerializedName

class Coupon {


    @SerializedName("Active")
    var active: String? = null
    @SerializedName("CouponCode")
    var couponCode: String? = null
    @SerializedName("CouponImage")
    var couponImage: String? = null
    @SerializedName("CustCouponId")
    var custCouponId: Long? = null
    @SerializedName("Id")
    var id: Long? = null
    @SerializedName("Description")
    var description: String? = null
    @SerializedName("Discount")
    var discount: Double = 0.0
    @SerializedName("EndDate")
    var expDate: String? = null
    @SerializedName("StartDate")
    var issueDate: String? = null
    @SerializedName("MinOrderAmt")
    var minOrderAmt: Double = 0.0
    @SerializedName("MaxCap")
    var maxCap: Double = 0.0
    @SerializedName("Name")
    var name: String? = null
    @SerializedName("PaymentType")
    var paymentType: Any? = null
    @SerializedName("PreTax")
    var preTax: String? = null
    @SerializedName("Service")
    var service: Any? = null
    @SerializedName("Status")
    var status: String? = null

    @SerializedName("CType")
    var CType: String? = null

    @SerializedName("Type")
    var type: String? = null
    @SerializedName("UsageCountLeft")
    var usageCountLeft: Long? = null
    @SerializedName("UseDate")
    var useDate: String? = null

}
