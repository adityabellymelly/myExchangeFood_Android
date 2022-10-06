package com.exchange.user.module.change_password_module

import android.content.Context
import com.exchange.user.R
import com.exchange.user.module.base_module.ConstantCommand
import com.exchange.user.module.base_module.base_command.CartDataInt
import com.exchange.user.module.base_module.base_model.PostData
import com.exchange.user.module.base_module.base_model.ResponceModel
import com.exchange.user.module.base_module.base_view.BaseViewModel
import com.exchange.user.module.change_password_module.model.ChangePasswordRequest
import com.exchange.user.module.edit_profile_module.EditProfileNavigator
import com.exchange.user.module.restaurent_module.model.api_model.TokenRequest
import com.exchange.user.module.restaurent_module.model.api_model.TokenResponce
import com.exchange.user.module.signin_module.model.SignInRequest
import com.exchange.user.module.utility_module.ApplicationSharePrefrence
import com.exchange.user.module.utility_module.ApplicationUtility
import com.exchange.user.module.utility_module.BuilderManager
import com.exchange.user.module.utility_module.Validations
import com.exchange.user.module.web_module.ApplicationRequest
import com.exchange.user.rx_module.SchedulerProvider
import com.google.gson.Gson
import com.google.gson.GsonBuilder

class ChangePasswordViewModel(applicationprefrence: ApplicationSharePrefrence, schedulerProvider : SchedulerProvider,
                              applicationutility : ApplicationUtility,
                              applicationcontext : Context,
                              validations  : Validations,
                              applicationrequest : ApplicationRequest
) : BaseViewModel(applicationprefrence,schedulerProvider, applicationutility, applicationcontext , validations , applicationrequest){

    fun onBack(){
        (getNavigator() as ChangePasswordNavigation).onBackPress()
    }

    fun save(){
        (getNavigator() as ChangePasswordNavigation).ChangePassword()
    }
    fun changePassword(currentPassword: String,newPassword:String,repeatPassword:String) {
        when {
            getValidation().isFieldEmpty(currentPassword) -> {
                (getNavigator() as ChangePasswordNavigation).showError(
                    ConstantCommand.USERNAME_ERROR,
                    "Invalid Current Password"
                )
            }
            getValidation().isFieldEmpty(newPassword) -> {
                (getNavigator() as ChangePasswordNavigation).showError(ConstantCommand.PASSWORD_ERROR,"Invalid New Password")

            }
            getValidation().isValidPassword(newPassword,8).not() -> {
                (getNavigator() as ChangePasswordNavigation).showError(ConstantCommand.PASSWORD_ERROR,getContext().getString(
                    R.string.invalid_min_char_password))

            }
            getValidation().matchTwoStrings(newPassword,repeatPassword).not()->{
                (getNavigator() as ChangePasswordNavigation).showError(ConstantCommand.M0BILE_ERROR,"That's not going to work. New Password and Confirm Password do not match.")

            }
            else->{
                (getNavigator() as ChangePasswordNavigation).showError(ConstantCommand.LOC_REQ_CODE,"")
                val logInRequest = ChangePasswordRequest()
                val logInData = ChangePasswordRequest.PasswordData()
                logInData.username = ""
                logInData.password = newPassword
                logInData.oldpassword = currentPassword
                logInRequest.dataPassword = logInData
                if (CartDataInt.orderdata.tId.isNullOrEmpty().not()) {
                    logInRequest.tId = CartDataInt.orderdata.tId ?: ""
                    moveToChangePassword(logInRequest)
                } else {
                    getToken(logInRequest)
                }
            }

        }
    }

    private fun getToken(signInRequest: ChangePasswordRequest) {
        if(getApplicationUtility().isInternetConnected()){
            (getNavigator() as ChangePasswordNavigation).showProgress(true)
            val token =  TokenRequest()
            token.appCode=ConstantCommand.APP_TOKEN
            token.mobUrl =ConstantCommand.URl.plus("=").plus(ConstantCommand.urlName)
            val data = com.exchange.user.module.restaurent_module.model.api_model.Data()
            data.mobUrl=ConstantCommand.urlName
            data.traceId = ""
            token.data=data
            val request = BuilderManager.encodeString(GsonBuilder().create().toJson(token))
            getCompositeDisposable().add(
                getApplicationRequest().getToken(PostData(request)).doOnSuccess {  }
                    .subscribeOn(getScheduleProvider().io())
                    .observeOn(getScheduleProvider().ui())
                    .subscribe({responce: ResponceModel ->

                        (getNavigator() as ChangePasswordNavigation).showProgress(false)

                        if (responce.serviceStatus.equals("S",true)){
                            if (responce.data!=null){
                                responce.data?.let {
                                    val tokendata : TokenResponce = Gson().fromJson(
                                        BuilderManager.decodeString(it),
                                        TokenResponce::class.java)
                                    tokendata.tId?.let {
                                        CartDataInt.orderdata.tId=tokendata.tId
                                        CartDataInt.orderdata.chainId = tokendata.chainId
                                        signInRequest.tId = tokendata.tId?:""
                                        moveToChangePassword(signInRequest)
                                    }
                                }
                            }else{
                                (getNavigator() as ChangePasswordNavigation).showFeedbackMessage("Something went wrong!")
                            }

                        }else{
                            responce.message?.let {
                                (getNavigator() as ChangePasswordNavigation).showFeedbackMessage(it)}
                        }
                    },
                        { throwable : Throwable? ->

                            (getNavigator() as ChangePasswordNavigation).showProgress(false)

                            throwable?.message?.let {
                                (getNavigator() as ChangePasswordNavigation).showFeedbackMessage(it)
                            } }

                    )
            )

        }
        else{ (getNavigator() as ChangePasswordNavigation).showFeedbackMessage(getContext().getString(R.string.warning_no_internet))}
    }

    private fun moveToChangePassword(logInRequest: ChangePasswordRequest) {
        if(getApplicationUtility().isInternetConnected()){
            (getNavigator() as ChangePasswordNavigation).showProgress(true)
            val requestData = BuilderManager.encodeString(GsonBuilder().create().toJson(logInRequest))
            getCompositeDisposable().add(
                getApplicationRequest().updatePassword(PostData(requestData)).doOnSuccess { }
                    .subscribeOn(getScheduleProvider().io())
                    .observeOn(getScheduleProvider().ui())
                    .subscribe({ responce ->
                        (getNavigator() as ChangePasswordNavigation).showProgress(false)
                        if (responce.serviceStatus.equals("S", true)) {
                            responce.message?.let {
                                (getNavigator() as ChangePasswordNavigation).showFeedbackMessage(it)
                            }
                            onBack()
                        } else {
                            responce.message?.let {
                                (getNavigator() as ChangePasswordNavigation).showFeedbackMessage(it)
                            }
                        }
                    },
                        { throwable ->
                            (getNavigator() as ChangePasswordNavigation).showProgress(false)
                            throwable.message?.let {
                                (getNavigator() as ChangePasswordNavigation).showFeedbackMessage(
                                    it
                                )
                            }
                        }
                    ))

        }
        else{
            (getNavigator() as ChangePasswordNavigation).showFeedbackMessage(getContext().getString(R.string.warning_no_internet))}


    }

}