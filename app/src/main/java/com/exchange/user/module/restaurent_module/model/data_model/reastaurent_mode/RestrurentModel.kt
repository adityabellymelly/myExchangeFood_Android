package com.exchange.user.module.restaurent_module.model.data_model.reastaurent_mode

import com.google.gson.annotations.SerializedName

class RestrurentModel {

    var finalAddress :String = ""
    @SerializedName("Description")
    var description: String? = null
    @SerializedName("HeaderBackgroundImage")
    var headerBackgroundImage: String? = null
    @SerializedName("PaymentProviderAAFES")
    var paymentProviderAAFES: String? = null
    @SerializedName("Address")
    var address: Address? = null
    @SerializedName("Alert")
    var alert: ArrayList<AlertMessage>? = null
    @SerializedName("Calendar")
    var calendar: Calendar? = null
    @SerializedName("CancelURL")
    var cancelURL: String? = null
    @SerializedName("Coupons")
    var coupons : ArrayList<Coupon> =  ArrayList()
    @SerializedName("Cuisines")
    var cuisines: List<Cuisines>? = null
    @SerializedName("Discounts")
    var discounts: ArrayList<Discount> =  ArrayList()
    @SerializedName("Header")
    var header: String? = null
    @SerializedName("HeaderImg")
    var headerImg: String? = null
    @SerializedName("Id")
    var id: Long? = null
    @SerializedName("isMenuGrubLocation")
    var isMenuGrubLocation: String? = null
    @SerializedName("isUsePaytronix")
    var isUsePaytronix: String? = null
    @SerializedName("IsVault")
    var isVault: String? = null
    @SerializedName("Lat")
    var lat: Double? = null
    @SerializedName("locationCurrentDate")
    var locationCurrentDate: String? = null
    @SerializedName("AppLogoImage")
    var logoImg: String? = null
    @SerializedName("PortalBkImg")
    var portalBkImg: String? = null
    @SerializedName("FeaturedNgoId")
    var featuredNgoId:Long = 0
    @SerializedName("Lon")
    var lon: Double? = null
    @SerializedName("MediaCDNURL")
    var mediaCDNURL: String? = null
    @SerializedName("Menus")
    var menus: List<Menus>? = null
    @SerializedName("MiscMask")
    var miscMask: String? = null
    @SerializedName("Name")
    var name: String? = null
    @SerializedName("OpenCloseTime")
    var openCloseTime: String? = null
    @SerializedName("PaymentProviderMercury")
    var paymentProviderMercury: String? = null
    @SerializedName("PaymentProviderMiMenu")
    var paymentProviderMiMenu: String = "F"
    @SerializedName("PaymentProviderStripe")
    var paymentProviderStripe: String? = null
    @SerializedName("PaymentProviderPayUMoney")
    var paymentProviderPayUMoney: String? = null
    @SerializedName("PaymentProviderPaytm")
    var paymentProviderPaytm: String? = null
    @SerializedName("PaymentProviderUSAePay")
    var paymentProviderUSAePay: String? = null
    @SerializedName("PaymentTypes")
    var paymentTypes: ArrayList<PaymentType>? = null
    @SerializedName("Price")
    var price: Long? = null
    @SerializedName("Rating")
    var rating: Double? = null
    @SerializedName("RestChainId")
    var restChainId: Long? = null
    @SerializedName("RestImage")
    var restImage: String? = null
    @SerializedName("ReturnURL")
    var returnURL: String? = null
    @SerializedName("Schedule")
    var schedule: List<Schedule>? = null
    @SerializedName("Services")
    val services: ArrayList<Service>? = null
    @SerializedName("Tax")
    var tax: Double = 0.0
    @SerializedName("Tel")
    var tel: String? = null
    @SerializedName("Theme")
    var theme: String? = null
    @SerializedName("TimeZone")
    var timeZone: TimeZone? = null
    @SerializedName("ValidationAtSite")
    var validationAtSite: String? = null
    @SerializedName("WLCUrl")
    var wlcUrl: String? = null
    var urlname: String? = null
    @SerializedName("ngo")
    var ngo: ArrayList<Ngo> =  ArrayList()
    @SerializedName("PublishableKey")
    var publishableKey: String? = null

}
