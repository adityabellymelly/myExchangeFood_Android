package com.exchange.user.module.createaccount_module

import android.content.Context
import android.util.Log
import com.exchange.user.R
import com.exchange.user.module.base_module.ConstantCommand
import com.exchange.user.module.base_module.base_command.CartDataInt
import com.exchange.user.module.base_module.base_model.PostData
import com.exchange.user.module.base_module.base_model.ResponceModel
import com.exchange.user.module.base_module.base_view.BaseViewModel
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

class CreateAccountViewModel(applicationprefrence: ApplicationSharePrefrence, schedulerProvider : SchedulerProvider,
                             applicationutility : ApplicationUtility,
                             applicationcontext : Context,
                             validations  : Validations,
                             applicationrequest : ApplicationRequest
) : BaseViewModel(applicationprefrence,schedulerProvider, applicationutility, applicationcontext , validations , applicationrequest){


    fun back(){
        (getNavigator() as CreateAccountNavigator).onBackPress()
    }
    fun continoue(){
        (getNavigator() as CreateAccountNavigator).continous()
    }

    fun create(username: String,email: String,mobile: String,password: String) {
        when {
            getValidation().isFieldEmpty(username) -> {
                (getNavigator() as CreateAccountNavigator).showError(ConstantCommand.USERNAME_ERROR,getContext().getString(R.string.invalid_name))

            }
            getValidation().isEmailValid(email).not() -> {
                (getNavigator() as CreateAccountNavigator).showError(ConstantCommand.EMAIL_ERROR,getContext().getString(
                    R.string.invalid_email_test))
            }
            getValidation().isPhoneNumberValid(mobile).not() -> {
                (getNavigator() as CreateAccountNavigator).showError(ConstantCommand.M0BILE_ERROR,getContext().getString(R.string.invalid_mobile))

            }
            getValidation().isFieldEmpty(password) -> {
                (getNavigator() as CreateAccountNavigator).showError(ConstantCommand.PASSWORD_ERROR,getContext().getString(R.string.invalid_password))

            }
            getValidation().isValidPassword(password,8).not() -> {
                (getNavigator() as CreateAccountNavigator).showError(ConstantCommand.PASSWORD_ERROR,getContext().getString(R.string.invalid_min_char_password))

            }
            else -> {
                (getNavigator() as CreateAccountNavigator).showError(ConstantCommand.NO_ERROR,"")
                movetoCreate(username,email,mobile,password)
            }
        }

    }

    private fun movetoCreate(username: String, email: String, mobile: String, password: String) {
        val loginrequest = SignInRequest()
        val logindata = SignInRequest.Data()
        logindata.username = email
        logindata.password = password
        logindata.customerData = CustomerData()
        logindata.customerData?.tel =mobile
        logindata.customerData?.fName =username
        logindata.customerData?.mName =""
        logindata.customerData?.lName =""
        logindata.customerData?.cell =""
        logindata.customerData?.eMail =email
        if (CartDataInt.orderdata.tId.isNullOrEmpty().not()) {
            logindata.locname = CartDataInt.restrurent.name
            loginrequest.tId = CartDataInt.orderdata.tId ?: ""
            loginrequest.data = logindata
            sendSignupOTP(loginrequest)
        } else {
            logindata.locname = ConstantCommand.locName
            loginrequest.data = logindata
            getToken(loginrequest)
        }

    }

    private fun sendSignupOTP(loginrequest: SignInRequest) {
        if(getApplicationUtility().isInternetConnected()){
            val requestData = BuilderManager.encodeString(GsonBuilder().create().toJson(loginrequest))
                getCompositeDisposable().add(
                    getApplicationRequest().sendSignupOTP(PostData(requestData)).doOnSuccess { }
                        .subscribeOn(getScheduleProvider().io())
                        .observeOn(getScheduleProvider().ui())
                        .subscribe({ responce ->
                            if (responce.serviceStatus.equals("S", true)) {
                                (getNavigator() as CreateAccountNavigator).openVerifyScreen(loginrequest)
                                responce.message?.let {
                                    (getNavigator() as CreateAccountNavigator).showFeedbackMessage(it)
                                }
                            } else {
                                responce.message?.let {
                                    (getNavigator() as CreateAccountNavigator).showFeedbackMessage(it)
                                }
                            }
                        },
                            { throwable ->
                                throwable.message?.let {
                                    (getNavigator() as CreateAccountNavigator).showFeedbackMessage(
                                        it
                                    )
                                }
                            }
                        ))

        }
        else{ (getNavigator() as CreateAccountNavigator).showFeedbackMessage(getContext().getString(R.string.warning_no_internet))}

    }


    private fun getToken(signinrequest: SignInRequest) {
        if(getApplicationUtility().isInternetConnected()){
            val token =  TokenRequest()
            token.appCode=ConstantCommand.APP_TOKEN
            token.mobUrl =ConstantCommand.URl.plus("=").plus(ConstantCommand.urlName)
            val data = com.exchange.user.module.restaurent_module.model.api_model.Data()
            data.mobUrl=ConstantCommand.urlName
            data.traceId = ""
            token.data=data
            val request = BuilderManager.encodeString(GsonBuilder().create().toJson(token))
            Log.e("Token request", GsonBuilder().create().toJson(token))
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
                                        sendSignupOTP(signinrequest)

                                    }
                                }
                            }else{
                                (getNavigator() as CreateAccountNavigator).showFeedbackMessage("Something went wrong!")
                            }

                        }else{
                            responce.message?.let {
                                (getNavigator() as CreateAccountNavigator).showFeedbackMessage(it)}
                        }
                    },
                        { throwable : Throwable? -> throwable?.message?.let {
                            (getNavigator() as CreateAccountNavigator).showFeedbackMessage(it)
                        } }

                    )
            )

        }
        else{ (getNavigator() as CreateAccountNavigator).showFeedbackMessage(getContext().getString(R.string.warning_no_internet))}
    }
}