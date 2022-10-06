package com.exchange.user.module.profile_module.model

import com.exchange.user.module.restaurent_module.model.data_model.reastaurent_mode.Coupon
import com.google.gson.annotations.SerializedName

class ProfileModel {

    @SerializedName("AddressBook")
    var addressBook: ArrayList<AddressBook>? = ArrayList()
    @SerializedName("CCInfo")
    var ccInfo: List<Any>? = null
    @SerializedName("Cell")
    var cell: String? = null
    @SerializedName("Coupons")
    var coupons : ArrayList<Coupon> =  ArrayList()
    @SerializedName("Email")
    var email: String? = null
    @SerializedName("FName")
    var fName: String? = null
    @SerializedName("GlobalUserId")
    var GlobalUserId: Long? = null
    @SerializedName("id")
    var id: Long? = null
    @SerializedName("isExistsIMenuAccount")
    var isExistsIMenuAccount: Long? = null
    @SerializedName("isVerifiedPhone")
    var isVerifiedPhone: Any? = null
    @SerializedName("LName")
    var lName: String? = null
    @SerializedName("LoyaltyPoints")
    var loyaltyPoints: String? = null
    @SerializedName("MName")
    var mName: String? = null
    @SerializedName("MobileOptIn")
    var mobileOptIn: String? = null
    @SerializedName("OrderHistory")
    var orderHistory: ArrayList<OrderHistory> = ArrayList()
    @SerializedName("TaxExempt")
    var taxExempt: String? = null
    @SerializedName("Tel")
    var tel: String? = null
    @SerializedName("userid")
    var userid: String? = null
    @SerializedName("username")
    var username: String? = null

}