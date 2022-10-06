package com.exchange.user.module.restaurent_module.model.data_model.reastaurent_mode

import com.google.gson.annotations.SerializedName

class Discount {
    @SerializedName("Coupon")
    var coupon: String? = null
    @SerializedName("Description")
    var description: String? = null
    @SerializedName("Disc")
    var disc:Double = 0.0
    @SerializedName("Discount")
    var discount: String? = null
    @SerializedName("EndDate")
    var endDate: String? = null
    @SerializedName("Id")
    var id: Long? = null
    @SerializedName("Lookup")
    var lookup: Lookup? = null
    @SerializedName("MaxCap")
    var maxCap:Double = 0.0
    @SerializedName("MinAmt")
    var minAmt: Double = 0.0
    @SerializedName("PaymentTypes")
    var paymentTypes: List<DiscountPaymentType>? = null
    @SerializedName("PreTax")
    var preTax: String? = null
    @SerializedName("Service")
    var service: Service? = null
    @SerializedName("StartDate")
    var startDate: String? = null
    @SerializedName("Type")
    var type: String? = null

}
