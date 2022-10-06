package com.exchange.user.module.order_info_module.model

import com.google.gson.annotations.SerializedName

class OrderInfoModel {
    var restService: String? = ""
    var locationName: String? = ""
    var locationTel: String?= ""
    var address: String?= ""
    var paymentType: String?= ""
    var questionList: ArrayList<QuestionList> = ArrayList()


    @SerializedName("QuickMsgList")
    var quickMsgList:ArrayList<Message>? = null




    @SerializedName("isDriverFeedbackSubmitted")
    var isDriverFeedbackSubmitted:String = "False"

    @SerializedName("DeliveryParcelImg")
    var deliveryParcelImg:String = ""

    @SerializedName("isOrderConfirmed")
    var isOrderConfirmed:String = "0"

    @SerializedName("URLName")
    var URLName:String = ""
    @SerializedName("CouponAmt")
    var couponAmt:String = "0.0"
    @SerializedName("CouponType")
    var couponType: String? = null
    @SerializedName("CreatedOn")
    var createdOn: String? = null
    @SerializedName("CustCouponId")
    var custCouponId: String? = null
    @SerializedName("Discount1Amt")
    var discount1Amt:String = "0.0"
    @SerializedName("Discount1Type")
    var discount1Type: String? = null
    @SerializedName("DiscountAmt")
    var discountAmt:String = "0.0"
    @SerializedName("DiscountType")
    var discountType: String? = null
    @SerializedName("isCouponPretax")
    var isCouponPretax: Boolean? = null
    @SerializedName("isDiscountPretax")
    var isDiscountPretax: Boolean? = null
    @SerializedName("isPaymentDiscountPretax")
    var isPaymentDiscountPretax: Boolean? = null
    @SerializedName("ItemList")
    var itemList: List<ItemList>? = null
    @SerializedName("LocationId")
    var locationId: String? = null
    @SerializedName("MenuId")
    var menuId: String? = null



    @SerializedName("RestAddr1")
    var restAddr1: String? = null
     @SerializedName("RestAddr2")
    var restAddr2: String? = null
    @SerializedName("RestHeader")
    var restHeader: String? = null
    @SerializedName("HeaderBackgroundImage")
    var headerBackgroundImage: String? = null
     @SerializedName("RestEmailId")
    var restEmailId: String = ""
    @SerializedName("RestTelephone")
    var restTelephone: String = ""



    @SerializedName("OrderId")
    var orderId: Long? = null
    @SerializedName("OrderNumber")
    var orderNumber: String? = null
    @SerializedName("PreTaxAmt")
    var preTaxAmt:String = "0.0"
    @SerializedName("RestChainCouponId")
    var restChainCouponId: Any? = null
    @SerializedName("ServiceId")
    var serviceId: String? = null
    @SerializedName("SpecialInstructions")
    var specialInstructions: String? = null
    @SerializedName("SrvcFee")
    var srvcFee: String = "0.0"
    @SerializedName("Status")
    var status: String? = null
    @SerializedName("TaxAmt")
    var taxAmt:  String = "0.0"
    @SerializedName("TipAmt")
    var tipAmt:String = "0.0"
    @SerializedName("TotalAmt")
    var totalAmt:String = "0.0"
    @SerializedName("isSubmittedFeedback")
    var isSubmittedFeedback:String = "0"

}