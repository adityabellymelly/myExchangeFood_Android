package com.exchange.user.module.web_module

import com.exchange.user.module.base_module.ConstantCommand
import com.exchange.user.module.base_module.base_model.PostData
import com.exchange.user.module.base_module.base_model.ResponceModel
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiInterface2 {
    @POST(ConstantCommand.GET_NEW_TOKEN)
    fun getToken(@Body postData : PostData):Single<ResponceModel>

    @POST(ConstantCommand.FET_LOCATION_DETAIL)
    fun getLocationDetail(@Body postData : PostData):Single<ResponceModel>

    @POST(ConstantCommand.MENU_DETAIL)
    fun getMenu(@Body postData : PostData):Single<ResponceModel>

    @POST(ConstantCommand.CHECK__ACCOUNT_EXIST)
    fun CheckAccountExists(@Body postData : PostData):Single<ResponceModel>

    @POST(ConstantCommand.CHECK_GOOGLE_ACCOUNT_EXIST)
    fun checkGoogleAccountExists(@Body postData : PostData):Single<ResponceModel>

    @POST(ConstantCommand.DO_LOGIN)
    fun doLogin(@Body postData : PostData):Single<ResponceModel>

    @POST(ConstantCommand.SEND_FORGOTPASSWORD_OTP)
    fun sendForgetPasswordOTP(@Body postData : PostData):Single<ResponceModel>

    @POST(ConstantCommand.RESET_PASSWORD_OTP)
    fun resetPasswordOTP(@Body postData : PostData):Single<ResponceModel>

    @POST(ConstantCommand.SEND_SIGNUP_OTP)
    fun sendSignupOTP(@Body postData : PostData):Single<ResponceModel>

    @POST(ConstantCommand.UPDATE_USER_PROFILE)
    fun updateUserProfile(@Body postData : PostData):Single<ResponceModel>

    @POST(ConstantCommand.CHANGE_PASSWORD)
    fun updatePassword(@Body postData : PostData):Single<ResponceModel>

    @POST(ConstantCommand.NEWUSER)
    fun newUser(@Body postData : PostData):Single<ResponceModel>

    @POST(ConstantCommand.DOGOOGLE_LOGIN)
    fun doGoogleLogin(@Body postData : PostData):Single<ResponceModel>

    @POST(ConstantCommand.DOFB_LOGIN)
    fun doFBLogin(@Body postData : PostData):Single<ResponceModel>

    @POST(ConstantCommand.USER_COUPON)
    fun getUserCoupons(@Body postData : PostData):Single<ResponceModel>

    @POST(ConstantCommand.UPDATECOUPON)
    fun updateCoupon(@Body postData : PostData): Single<ResponceModel>

    @POST(ConstantCommand.LAST_ORDER)
    fun getLastOrder(@Body postData : PostData):Single<ResponceModel>

    @POST(ConstantCommand.START_ORDER)
    fun startOrder(@Body postData : PostData):Single<ResponceModel>

    @POST(ConstantCommand.DO_LOGIN_BY_CUSTID)
    fun DoLoginByCustId(@Body postData : PostData):Single<ResponceModel>

    @POST(ConstantCommand.SET_USER_ADDRESS)
    fun setUserAddress(@Body postData : PostData):Single<ResponceModel>

    @POST(ConstantCommand.GET_UNIQUE_IDENTIFIER)
    fun getUniqueIdentifier(@Body postData : PostData):Single<ResponceModel>

    @POST(ConstantCommand.DOMARGE_GOOGLE_DATA)
    fun domergeGoogleData(@Body postData : PostData):Single<ResponceModel>
    @POST(ConstantCommand.GET_CUST_ORDERHISTORY)
    fun getCustOrderHistory(@Body postData : PostData):Single<ResponceModel>

    @POST(ConstantCommand.GET_ORDER_INF)
    fun getOrderInfo(@Body postData : PostData): Single<ResponceModel>
    @POST(ConstantCommand.SAVE_FEEDBACK)
    fun savefeedBack(@Body postData : PostData): Single<ResponceModel>


    @POST(ConstantCommand.DELETE_USER_IOS)
    fun deleteUseriOS(@Body postData : PostData): Single<ResponceModel>


}