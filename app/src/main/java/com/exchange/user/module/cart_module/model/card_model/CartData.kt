package com.exchange.user.module.cart_module.model.card_model

import com.google.gson.annotations.SerializedName

class CartData {
    @SerializedName("CCInfo") var ccInfo: CCInfo = CCInfo()
    @SerializedName("CouponAmt") var couponAmt: Double? = null
    @SerializedName("CouponType") var couponType: Any? = null
    @SerializedName("CreatedOn") var createdOn: String? = null
    @SerializedName("CustCouponId") var custCouponId: Any? = null
    @SerializedName("CustId") var custId: Long? = null
    @SerializedName("DeliveryInfo") var deliveryInfo: DeliveryInfo? = null
    @SerializedName("Discount1Amt") var discount1Amt: Double? = null
    @SerializedName("Discount1Type") var discount1Type: Long? = null
    @SerializedName("DiscountAmt") var discountAmt:Double? = null
    @SerializedName("DiscountType") var discountType: Long? = null
    @SerializedName("DueOn") var dueOn: String? = null
    @SerializedName("isPrinterMsgOk") var isPrinterMsgOk: String? = null
    @SerializedName("LocationId") var locationId: Long? = null
    @SerializedName("MenuId") var menuId: Long? = null
    @SerializedName("MercuryPaymentId") var mercuryPaymentId: Any? = null
    @SerializedName("PaymentTypeId") var paymentTypeId: Long? = null
    @SerializedName("PaypalPayerId") var paypalPayerId: Any? = null
    @SerializedName("PaypalSecureToken") var paypalSecureToken: Any? = null
    @SerializedName("PlaceOrder") var placeOrder: String? = null
    @SerializedName("PreTaxAmt") var preTaxAmt: Double = 0.0
    @SerializedName("RestChainCouponId") var restChainCouponId: Any? = null
    @SerializedName("ServiceId") var serviceId: Long? = null
    @SerializedName("SrvcFee") var srvcFee: Double = 0.0
    @SerializedName("Status") var status: Long? = null
    @SerializedName("TaxAmt") var taxAmt: Double = 0.0
    @SerializedName("TimeSelection") var timeSelection: Int? = null
    @SerializedName("TipAmt") var tipAmt: Long = 0
    @SerializedName("TotalAmt") var totalAmt: Double = 0.0
    @SerializedName("SpecialInstructions") var SpecialInstructions : String = ""
    @SerializedName("HAInfo") var HAInfo : String? = null
    @SerializedName("DelzoneInd") var delzoneInd : Long? = null
    @SerializedName("OldOrderId") var oldOrderId:String? = null
    @SerializedName("utm_source") var utmSource: String = ""
    @SerializedName("ItemList") var itemList: ArrayList<ItemList> = ArrayList()
    @SerializedName("RestChainId") var restChainId : Long? = null
    @SerializedName("AAFESPayTraceId") var aafesPayTraceId: String? = null
    @SerializedName("isAAFESPayGateway") var isAAFESPayGateway: String? = null
    @SerializedName("carmake") var carmake: String = ""
    @SerializedName("carmodel") var carmodel: String = ""
    @SerializedName("carcolor") var carcolor: String =""
    @SerializedName("carplatenumber") var carplatenumber: String = ""
    @SerializedName("OrderFrom") var OrderFrom: String = "Android"
}
