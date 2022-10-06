package com.exchange.user.module.web_module

import com.exchange.user.module.base_module.base_model.PostData
import javax.inject.Inject

class ApplicationRequest
@Inject
constructor(private var api : ApiInterface,
            private var api2: ApiInterface2,
            private var api3: ApiInterface3){


    fun location(location :String)=api.location(location,"1.9")
    fun country(country :String)=api.country(country)
    fun aboutUs(deliveryProgram:String)=api.aboutUs(deliveryProgram)

   //  ------------- API 2 -----------


    fun deleteUseriOS(postdata: PostData)= api2.deleteUseriOS(postdata)
    fun getToken(postdata: PostData)=api2.getToken(postdata)
    fun getLocationDetail(postdata: PostData)=api2.getLocationDetail(postdata)
    fun getMenu(postdata: PostData)=api2.getMenu(postdata)
    fun checkGoogleAccountExists(postdata: PostData)=api2.checkGoogleAccountExists(postdata)
    fun CheckAccountExists(postdata: PostData)=api2.CheckAccountExists(postdata)
    fun doLogin(postdata: PostData)=api2.doLogin(postdata)
    fun sendForgetPasswordOTP(postdata: PostData)=api2.sendForgetPasswordOTP(postdata)
    fun resetPasswordOTP(postdata: PostData)=api2.resetPasswordOTP(postdata)
    fun sendSignupOTP(postdata: PostData)=api2.sendSignupOTP(postdata)
    fun updateUserProfile(postdata: PostData)=api2.updateUserProfile(postdata)
    fun updatePassword(postdata: PostData)=api2.updatePassword(postdata)
    fun newUser(postdata: PostData)=api2.newUser(postdata)
    fun doGoogleLogin(postdata: PostData)=api2.doGoogleLogin(postdata)
    fun doFBLogin(postdata: PostData)=api2.doFBLogin(postdata)
    fun getUserCoupons(postdata: PostData)=api2.getUserCoupons(postdata)
    fun updateCoupon(postdata: PostData)=api2.updateCoupon(postdata)
    fun getLastOrder(postdata: PostData)=api2.getLastOrder(postdata)
    fun startOrder(postdata: PostData)=api2.startOrder(postdata)
    fun DoLoginByCustId(postdata: PostData)=api2.DoLoginByCustId(postdata)
    fun setUserAddress(postdata: PostData)=api2.setUserAddress(postdata)
    fun getUniqueIdentifier(postdata: PostData)=api2.getUniqueIdentifier(postdata)
    fun domergeGoogleData(postdata: PostData)=api2.domergeGoogleData(postdata)
    fun getCustOrderHistory(postdata: PostData)=api2.getCustOrderHistory(postdata)
    fun getOrderInfo(postdata: PostData)=api2.getOrderInfo(postdata)
    fun savefeedBack(postdata: PostData)=api2.savefeedBack(postdata)


    //  ------------- API 3 -----------

    fun sendServiceFeedback(postdata: PostData) =  api3.sendServiceFeedback(postdata)
    fun getOrderDeliveryStatusForCustomer(postdata: PostData) = api3.getOrderDeliveryStatusForCustomer(postdata)
}