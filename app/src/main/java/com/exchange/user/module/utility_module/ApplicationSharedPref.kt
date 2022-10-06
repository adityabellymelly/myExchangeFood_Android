package com.exchange.user.module.utility_module

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.exchange.user.module.base_module.ConstantCommand
import java.util.*
import javax.inject.Inject

class ApplicationSharedPref
@Inject
constructor( context: Context) : ApplicationSharePrefrence {
    private val sharedpreferences: SharedPreferences = context.getSharedPreferences(ConstantCommand.PREF_NAME, Context.MODE_PRIVATE)

    fun getPreferences(): SharedPreferences {
        return sharedpreferences
    }

    override fun clearPrefrences() {
        getPreferences().edit().clear().apply()
    }


    override fun getPhoneNumber(): String? {
        return getPreferences().getString(ConstantCommand.PHONE_NO, null)
    }

    override fun setPhoneNumber(PhoneNumber: String) {
        getPreferences().edit().putString(ConstantCommand.PHONE_NO, PhoneNumber).apply()
    }

    override fun getUserID(): String? {
        return getPreferences().getString(ConstantCommand.USER_IDShare, null)
    }

    override fun setUserID(userid: String) {
        getPreferences().edit().putString(ConstantCommand.USER_IDShare, userid).apply()
    }

    override fun getUserName(): String? {
        return getPreferences().getString(ConstantCommand.USER_NAME, null)
    }

    override fun setUserName(username: String) {
        getPreferences().edit().putString(ConstantCommand.USER_NAME, username).apply()
    }



    override fun getEmail(): String? {
        return getPreferences().getString(ConstantCommand.EMAIL, null)
    }

    override fun setEmail(email: String) {
        getPreferences().edit().putString(ConstantCommand.EMAIL, email).apply()
    }



    override fun setFirst(first: String) {
        getPreferences().edit().putString(ConstantCommand.FIRST, first).apply()
    }

    override fun getfirst(): String? {
        return getPreferences().getString(ConstantCommand.FIRST,"")
    }

    override fun setAddress(address: String) {
        getPreferences().edit().putString(ConstantCommand.ADDRESS,address).apply()
    }

    override fun getAddress(): String? {
        return getPreferences().getString(ConstantCommand.ADDRESS, null)
    }

    override fun getLocalRestrurent(): String?{
        return getPreferences().getString(ConstantCommand.LOCALDATA,null)
    }

    override fun setLocalRestrurent(scandata : String? ){
        getPreferences().edit().putString(ConstantCommand.LOCALDATA,scandata).apply()
    }


    override fun setCountry(country: String?) {
        getPreferences().edit().putString(ConstantCommand.COUNTRY,country).apply()
    }
    override fun getCountry(): String? {
        return getPreferences().getString(ConstantCommand.COUNTRY,null)
    }


    override fun setSelectCountry(country: String?) {
        getPreferences().edit().putString(ConstantCommand.COUNTRY_SELECTED,country).apply()
    }

    override fun getSelectCountry(): String? {
        return getPreferences().getString(ConstantCommand.COUNTRY_SELECTED,null)
    }

    override fun setReward(rewardPoints: Double) {
        getPreferences().edit().putString(ConstantCommand.REWARDPOINT,rewardPoints.toString()).apply()
    }

    override fun getReward(): String? {
        return getPreferences().getString(ConstantCommand.REWARDPOINT,"0.0")
    }


    override fun setProfile(profile: String) {
        getPreferences().edit().putString(ConstantCommand.PROFILE,profile).apply()
    }

    override fun getProfile() : String?  {
        return getPreferences().getString(ConstantCommand.PROFILE,null)
    }

    override fun getCart(): String? {
        return getPreferences().getString(ConstantCommand.CART,null)
    }

    override fun setCart(cart: String?){
        getPreferences().edit().putString(ConstantCommand.CART,cart).apply()
    }

    override fun setResturent(resturent: String?){
        getPreferences().edit().putString(ConstantCommand.RESTRAURENT,resturent).apply()
    }

    override fun getResturent(): String? {
        return getPreferences().getString(ConstantCommand.RESTRAURENT,null)
    }

    override fun setFCMToken(token: String?) {
        getPreferences().edit().putString(ConstantCommand.FCM_TOKEN,token).apply()
    }

    override fun getFCMToken() : String? {

        return getPreferences().getString(ConstantCommand.FCM_TOKEN,null)
    }

    override fun setValidCoupon(countryCode: String?) {
        getPreferences().edit().putString(ConstantCommand.VALID_COUPON,countryCode).apply()
    }

    override fun getValidCoupon(): String? {
        return getPreferences().getString(ConstantCommand.VALID_COUPON,null)
    }

    override fun setMenuData(state: String?) {
        getPreferences().edit().putString(ConstantCommand.MENUDATA,state).apply()
    }

    override fun getMenuData() : String?  {
        return getPreferences().getString(ConstantCommand.MENUDATA, null)
    }

}