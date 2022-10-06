package com.exchange.user.module.forgot_password_module

import android.content.Context
import android.util.Log
import com.exchange.user.R
import com.exchange.user.module.base_module.ConstantCommand
import com.exchange.user.module.base_module.base_command.CartDataInt
import com.exchange.user.module.base_module.base_model.PostData
import com.exchange.user.module.base_module.base_model.ResponceModel
import com.exchange.user.module.base_module.base_view.BaseViewModel
import com.exchange.user.module.profile_module.model.ProfileModel
import com.exchange.user.module.restaurent_module.model.api_model.TokenRequest
import com.exchange.user.module.restaurent_module.model.api_model.TokenResponce
import com.exchange.user.module.signin_module.model.CustomerData
import com.exchange.user.module.signin_module.model.SignInRequest
import com.exchange.user.module.utility_module.ApplicationSharePrefrence
import com.exchange.user.module.utility_module.ApplicationUtility
import com.exchange.user.module.utility_module.BuilderManager
import com.exchange.user.module.utility_module.Validations
import com.exchange.user.module.web_module.ApplicationRequest
import com.exchange.user.rx_module.SchedulerProvider
import com.google.gson.Gson
import com.google.gson.GsonBuilder

class ForgotPasswordViewModel (applicationprefrence: ApplicationSharePrefrence, schedulerProvider : SchedulerProvider,
                               applicationutility : ApplicationUtility,
                               applicationcontext : Context,
                               validations  : Validations,
                               applicationrequest : ApplicationRequest
) : BaseViewModel(applicationprefrence,schedulerProvider, applicationutility, applicationcontext , validations , applicationrequest){


    fun back(){(getNavigator() as ForgotPasswordNavigator).onBackPress()}
    fun sendOTP(){
        (getNavigator() as ForgotPasswordNavigator).onSendOtp()
    }
    private fun getToken(signinrequest: SignInRequest, from:Int) {
        if(getApplicationUtility().isInternetConnected()){
            val token =  TokenRequest()
            token.appCode= ConstantCommand.APP_TOKEN
            token.mobUrl =ConstantCommand.URl.plus("=").plus(ConstantCommand.urlName)
            val data = com.exchange.user.module.restaurent_module.model.api_model.Data()
            data.mobUrl=ConstantCommand.urlName
            data.traceId = ""
            token.data=data
            val request = BuilderManager.encodeString(GsonBuilder().create().toJson(token))
            Log.e("Token request",GsonBuilder().create().toJson(token))
            getCompositeDisposable().add(
                getApplicationRequest().getToken(PostData(request)).doOnSuccess {  }
                    .subscribeOn(getScheduleProvider().io())
                    .observeOn(getScheduleProvider().ui())
                    .subscribe({responce: ResponceModel ->
                        if (responce.serviceStatus.equals("S",true)){
                            if (responce.data!=null){
                                responce.data?.let {
                                    val tokendata : TokenResponce = Gson().fromJson(BuilderManager.decodeString(it),TokenResponce::class.java)
                                    tokendata.tId?.let {
                                        CartDataInt.orderdata.tId=tokendata.tId
                                        CartDataInt.orderdata.chainId = tokendata.chainId
                                        signinrequest.tId = tokendata.tId?:""
                                         sendRequestOTP(signinrequest)
                                    }
                                }
                            }else{
                                (getNavigator() as ForgotPasswordNavigator).showFeedbackMessage("Something went wrong!")
                            }

                        }else{
                            responce.message?.let {
                                (getNavigator() as ForgotPasswordNavigator).showFeedbackMessage(it)}
                        }
                    },
                        { throwable : Throwable? -> throwable?.message?.let {
                            (getNavigator() as ForgotPasswordNavigator).showFeedbackMessage(it)
                        } }

                    )
            )

        }
        else{ (getNavigator() as ForgotPasswordNavigator).showFeedbackMessage(getContext().getString(
            R.string.warning_no_internet))}
    }

    fun sendOTPReq(emailaddress: String) {
        if (getValidation().isEmailValid(emailaddress).not() || getValidation().isFieldEmpty(emailaddress)){
            (getNavigator() as ForgotPasswordNavigator).showError(ConstantCommand.EMAIL_ERROR,getContext().getString(R.string.invalid_email_id))
        }else{
            val loginrequest = SignInRequest()
            val logindata = SignInRequest.Data()
            logindata.username = emailaddress
            logindata.customerData = CustomerData()

            if (CartDataInt.orderdata.tId.isNullOrEmpty().not()) {
                logindata.locname = CartDataInt.restrurent.name
                loginrequest.tId = CartDataInt.orderdata.tId ?: ""
                loginrequest.data = logindata
                sendRequestOTP(loginrequest)
            } else {
                logindata.locname = CartDataInt.restrurent.name
                loginrequest.data = logindata
                getToken(loginrequest,ConstantCommand.NORMAL_LOGIN)
            }

        }

    }

    private fun sendRequestOTP(loginrequest: SignInRequest) {
        if (getApplicationUtility().isInternetConnected()) {
            val requestdata = BuilderManager.encodeString(GsonBuilder().create().toJson(loginrequest))
            getCompositeDisposable().add(
                getApplicationRequest().sendForgetPasswordOTP(PostData(requestdata)).doOnSuccess {  }
                    .subscribeOn(getScheduleProvider().io())
                    .observeOn(getScheduleProvider().ui())
                    .subscribe({ responce->
                        if (responce.serviceStatus.equals("S",true)){
                            if (responce.data!=null){
                                responce.data?.let {
                                    val profiledata : ProfileModel = Gson().fromJson(BuilderManager.decodeString(it), ProfileModel::class.java)
                                    loginrequest.data?.customerData?.tel = profiledata.username?:profiledata.tel?:""
                                    loginrequest.data?.username = profiledata.username?:""
                                    (getNavigator() as ForgotPasswordNavigator).openOtpScreen(loginrequest)
                                }
                            }else{
                                (getNavigator() as ForgotPasswordNavigator).showFeedbackMessage("Something went wrong!")
                            }
                        }else{
                            responce.message?.let {
                                (getNavigator() as ForgotPasswordNavigator).showFeedbackMessage(it)}
                        }
                    },
                        {
                                throwable -> throwable.message?.let {(getNavigator() as ForgotPasswordNavigator).showFeedbackMessage(it)}
                        }
                    ))

        }else {
            (getNavigator() as ForgotPasswordNavigator).showFeedbackMessage(getContext().getString(R.string.warning_no_internet))
        }
    }

}