package com.exchange.user.module.resetpassword_module

import android.content.Context
import android.widget.Toast
import com.exchange.user.R
import com.exchange.user.module.base_module.base_model.PostData
import com.exchange.user.module.base_module.base_view.BaseViewModel
import com.exchange.user.module.signin_module.model.SignInRequest
import com.exchange.user.module.utility_module.ApplicationSharePrefrence
import com.exchange.user.module.utility_module.ApplicationUtility
import com.exchange.user.module.utility_module.BuilderManager
import com.exchange.user.module.utility_module.Validations
import com.exchange.user.module.web_module.ApplicationRequest
import com.exchange.user.rx_module.SchedulerProvider
import com.google.gson.GsonBuilder

class ResetPasswordViewModel (applicationprefrence: ApplicationSharePrefrence, schedulerProvider : SchedulerProvider,
                              applicationutility : ApplicationUtility,
                              applicationcontext : Context,
                              validations  : Validations,
                              applicationrequest : ApplicationRequest
) : BaseViewModel(applicationprefrence,schedulerProvider, applicationutility, applicationcontext , validations , applicationrequest){

    private lateinit var signInRequest: SignInRequest
    fun setSignInRequest(signInRequest: SignInRequest) {
        this.signInRequest = signInRequest
    }

    fun back(){ (getNavigator() as ResetPasswordNavigator).onBackPress() }
    fun resetPassword(){(getNavigator() as ResetPasswordNavigator).resetPassword()}
    fun appliyReset(password: String) {
        when {
            getValidation().isFieldEmpty(password) ->
                (getNavigator() as ResetPasswordNavigator).showFeedbackMessage("Enter valid password")
            getValidation().isValidPassword(password,8).not() ->
                (getNavigator() as ResetPasswordNavigator).showFeedbackMessage("Nice try but you need a minimum of 4 characters.")
            else ->{
                signInRequest.data?.password =password
                resetPasswordApi(signInRequest)
            }
        }
    }

    private fun resetPasswordApi(signInRequest: SignInRequest) {
        if (getApplicationUtility().isInternetConnected()) {
            val requestdata = BuilderManager.encodeString(GsonBuilder().create().toJson(signInRequest))
            getCompositeDisposable().add(
                getApplicationRequest().resetPasswordOTP(PostData(requestdata)).doOnSuccess {  }
                    .subscribeOn(getScheduleProvider().io())
                    .observeOn(getScheduleProvider().ui())
                    .subscribe({ responce->
                        if (responce.serviceStatus.equals("S",true)){
                            responce.message?.let {
                                Toast.makeText(getContext(),it,Toast.LENGTH_LONG).show()
                            }
                            (getNavigator() as ResetPasswordNavigator).openSigninScreen()
                        }else{
                            responce.message?.let {
                                (getNavigator() as ResetPasswordNavigator).showFeedbackMessage(it)}
                        }
                    },
                        {
                                throwable -> throwable.message?.let {(getNavigator() as ResetPasswordNavigator).showFeedbackMessage(it)}
                        }
                    ))

        }else {
            (getNavigator() as ResetPasswordNavigator).showFeedbackMessage(getContext().getString(R.string.warning_no_internet))
        }
    }
}