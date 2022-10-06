package com.exchange.user.module.utility_module

interface ApplicationSharePrefrence {

    fun clearPrefrences()
    fun getPhoneNumber(): String?
    fun setPhoneNumber(PhoneNumber: String)
    fun getUserID(): String?
    fun setUserID(userid: String)
    fun getUserName(): String?
    fun setUserName(username: String)
    fun getEmail(): String?
    fun setEmail(email: String)
    fun setFirst(first: String)
    fun getfirst(): String?
    fun setAddress(address: String)
    fun getAddress(): String?
    fun getLocalRestrurent(): String?
    fun setLocalRestrurent(scandata: String?)
    fun setCountry(country: String?)
    fun getCountry(): String?
    fun setSelectCountry(country: String?)
    fun getSelectCountry(): String?
    fun setReward(rewardPoints: Double)
    fun getReward(): String?
    fun setProfile(profile: String)
    fun getProfile(): String?

    fun getCart(): String?

    fun setCart(cart: String?)

    fun setResturent(resturent: String?)

    fun getResturent(): String?

    fun setFCMToken(token: String?)

    fun getFCMToken(): String?
    fun setValidCoupon(countryCode: String?)
    fun getValidCoupon():String?
    fun setMenuData(state: String?)
    fun getMenuData(): String?
}