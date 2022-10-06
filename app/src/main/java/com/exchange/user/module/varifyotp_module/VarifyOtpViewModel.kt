package com.exchange.user.module.varifyotp_module

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.exchange.user.R
import com.exchange.user.module.base_module.ConstantCommand
import com.exchange.user.module.base_module.base_command.CartDataInt
import com.exchange.user.module.base_module.base_model.PostData
import com.exchange.user.module.base_module.base_view.BaseViewModel
import com.exchange.user.module.profile_module.model.ProfileModel
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
import java.util.concurrent.TimeUnit

class VarifyOtpViewModel (applicationprefrence: ApplicationSharePrefrence, schedulerProvider : SchedulerProvider,
                          applicationutility : ApplicationUtility,
                          applicationcontext : Context,
                          validations  : Validations,
                          applicationrequest : ApplicationRequest
) : BaseViewModel(applicationprefrence,schedulerProvider, applicationutility, applicationcontext , validations , applicationrequest){

    private lateinit var signInRequest: SignInRequest
    private var from: Int=0

    fun back(){
        (getNavigator() as VarifyOtpNavigator).onBackPress()
    }
    fun editPhoneNumber(){}
    fun resendOtp(){(getNavigator() as VarifyOtpNavigator).onResendOtp()}
    fun varifyOtp(){(getNavigator() as VarifyOtpNavigator).onVerify()}
    fun setSignInRequest(signInRequest: SignInRequest) {
          this.signInRequest = signInRequest
    }
    fun setFrom(from: Int) {
        this.from = from
    }
    fun getFrom():Int=from
    fun calculateTime(seconds: Long): String {
        val minute = TimeUnit.SECONDS.toMinutes(seconds) - TimeUnit.SECONDS.toHours(seconds) * 60
        val second = TimeUnit.SECONDS.toSeconds(seconds) - TimeUnit.SECONDS.toMinutes(seconds) * 60
        val min: String
        val sec: String
        min = if (minute < 10)
            "0$minute"
        else
            "" + minute
        if (second < 10)
            sec = "0$second"
        else
            sec = "" + second
        return "$min:$sec"
    }

    fun gotoVerify(otp: String, password: String) {
        when {
            getValidation().isFieldEmpty(otp) -> (getNavigator() as VarifyOtpNavigator).showFeedbackMessage("Enter valid otp")
            otp.length<4 -> (getNavigator() as VarifyOtpNavigator).showFeedbackMessage("Enter valid otp")
            else -> {
                signInRequest.data?.customerData?.OTP = otp
                if (from==ConstantCommand.FROM_FORGOT){

                    when {
                        getValidation().isFieldEmpty(password) ->
                            (getNavigator() as VarifyOtpNavigator).showFeedbackMessage("Enter valid password")
                        getValidation().isValidPassword(password,8).not() ->
                            (getNavigator() as VarifyOtpNavigator).showFeedbackMessage("Nice try but you need a minimum of 8 characters.")
                        else ->{
                            signInRequest.data?.password =password
                            resetPasswordApi(signInRequest)
                        }
                    }
                }else{
                    if (from==ConstantCommand.FROM_FORGOT){
                        (getNavigator() as VarifyOtpNavigator).openResetPassword(signInRequest)
                    }
                    else if (from==ConstantCommand.FROM_CREATE_ACCOUNT){
                        signInRequest.data?.checkOTP  =1
                        signInRequest.data?.customerData?.mobileOptIn=1
                        createnewUser(signInRequest)
                    }
                    else if (from ==ConstantCommand.GOOGLE_LOGIN){
                        signInRequest.data?.checkOTP  =1
                        signInRequest.checkOTP = 1
                        googleLogin(signInRequest)
                    }
                    else if (from ==ConstantCommand.FACEBOOK_LOGIN){
                        signInRequest.data?.checkOTP  =1
                        signInRequest.checkOTP = 1
                        facebookLogin(signInRequest)
                    }

                }
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
                            (getNavigator() as VarifyOtpNavigator).openSigninScreen()
                        }else{
                            responce.message?.let {
                                (getNavigator() as VarifyOtpNavigator).showFeedbackMessage(it)}
                        }
                    },
                        {
                                throwable -> throwable.message?.let {(getNavigator() as VarifyOtpNavigator).showFeedbackMessage(it)}
                        }
                    ))

        }else {
            (getNavigator() as VarifyOtpNavigator).showFeedbackMessage(getContext().getString(R.string.warning_no_internet))
        }
    }

    private fun facebookLogin(signInRequest: SignInRequest) {
        if (getApplicationUtility().isInternetConnected()) {
            val requestdata = BuilderManager.encodeString(GsonBuilder().create().toJson(signInRequest))
            getCompositeDisposable().add(
                getApplicationRequest().doFBLogin(PostData(requestdata)).doOnSuccess {  }
                    .subscribeOn(getScheduleProvider().io())
                    .observeOn(getScheduleProvider().ui())
                    .subscribe({ responce->
                        if (responce.serviceStatus.equals("S",true)){
                            if (responce.data!=null){
                                responce.data?.let {
                                    val profiledata : ProfileModel = Gson().fromJson(BuilderManager.decodeString(it), ProfileModel::class.java)
                                    CartDataInt.profiledata = profiledata
                                    saveData(profiledata)
                                    (getNavigator() as VarifyOtpNavigator).moveToNext()
                                }
                            }
                            responce.message?.let {
                                Toast.makeText(getContext(),it, Toast.LENGTH_LONG).show()
                            }

                            (getNavigator() as VarifyOtpNavigator).openSigninScreen()
                        }else{
                            responce.message?.let {
                                (getNavigator() as VarifyOtpNavigator).showFeedbackMessage(it)}
                        }
                    },
                        {
                                throwable -> throwable.message?.let {(getNavigator() as VarifyOtpNavigator).showFeedbackMessage(it)}
                        }
                    ))

        }else {
            (getNavigator() as VarifyOtpNavigator).showFeedbackMessage(getContext().getString(R.string.warning_no_internet))
        }
    }

    private fun googleLogin(signInRequest: SignInRequest) {
        if (getApplicationUtility().isInternetConnected()) {
            val requestdata = BuilderManager.encodeString(GsonBuilder().create().toJson(signInRequest))
            getCompositeDisposable().add(
                getApplicationRequest().doGoogleLogin(PostData(requestdata)).doOnSuccess {  }
                    .subscribeOn(getScheduleProvider().io())
                    .observeOn(getScheduleProvider().ui())
                    .subscribe({ responce->
                        if (responce.serviceStatus.equals("S",true)){
                            if (responce.data!=null){
                                responce.data?.let {
                                    val profiledata : ProfileModel = Gson().fromJson(BuilderManager.decodeString(it), ProfileModel::class.java)
                                    CartDataInt.profiledata = profiledata
                                    saveData(profiledata)
                                    (getNavigator() as VarifyOtpNavigator).moveToNext()
                                }
                            }
                            responce.message?.let {
                                Toast.makeText(getContext(),it, Toast.LENGTH_LONG).show()
                            }

                            (getNavigator() as VarifyOtpNavigator).openSigninScreen()
                        }else{
                            responce.message?.let {
                                (getNavigator() as VarifyOtpNavigator).showFeedbackMessage(it)}
                        }
                    },
                        {
                                throwable -> throwable.message?.let {(getNavigator() as VarifyOtpNavigator).showFeedbackMessage(it)}
                        }
                    ))

        }else {
            (getNavigator() as VarifyOtpNavigator).showFeedbackMessage(getContext().getString(R.string.warning_no_internet))
        }
    }

    private fun createnewUser(signInRequest: SignInRequest) {
        if (getApplicationUtility().isInternetConnected()) {
            val requestdata = BuilderManager.encodeString(GsonBuilder().create().toJson(signInRequest))
            getCompositeDisposable().add(
                getApplicationRequest().newUser(PostData(requestdata)).doOnSuccess {  }
                    .subscribeOn(getScheduleProvider().io())
                    .observeOn(getScheduleProvider().ui())
                    .subscribe({ responce->
                        if (responce.serviceStatus.equals("S",true)){
                            responce.message?.let {
                                Toast.makeText(getContext(),it, Toast.LENGTH_LONG).show()
                            }
                            (getNavigator() as VarifyOtpNavigator).openSigninScreen()
                        }else{
                            responce.message?.let {
                                (getNavigator() as VarifyOtpNavigator).showFeedbackMessage(it)}
                        }
                    },
                        {
                                throwable -> throwable.message?.let {(getNavigator() as VarifyOtpNavigator).showFeedbackMessage(it)}
                        }
                    ))

        }else {
            (getNavigator() as VarifyOtpNavigator).showFeedbackMessage(getContext().getString(R.string.warning_no_internet))
        }
    }


    private fun saveData(it: ProfileModel) {
        if( it.email.isNullOrEmpty()){
            it.username?.let {
                if (getValidation().isValidEmail(it)) {
                    getApplicationSharePref().setEmail(it)
                }
            }

        }else{
            it.email?.let {
                if (getValidation().isValidEmail(it)) {
                    getApplicationSharePref().setEmail(it)
                }
            }
        }
        val username = StringBuilder()
        it.fName?.let {
            username.append(it)
        }
        it.lName?.let {
            username.append(" ${it}")
        }

        it.tel?.let {
            getApplicationSharePref().setPhoneNumber(it)
        }
        it.id?.let {
            getApplicationSharePref().setUserID(it.toString())
        }
        getApplicationSharePref().setUserName(username.toString())
        getApplicationSharePref().setReward((it.loyaltyPoints ?: "0").toDouble())


        val profilegson = GsonBuilder().setPrettyPrinting().serializeNulls().create()
        val jsonprofileString = profilegson.toJson(it)
        getApplicationSharePref().setProfile(jsonprofileString)

    }

    fun goresendOtp() {
        if (getApplicationUtility().isInternetConnected()) {
            when (from) {
                ConstantCommand.FROM_FORGOT -> {
                    sendRequestOTP(signInRequest)
                }
                ConstantCommand.FROM_CREATE_ACCOUNT -> {
                    sendSignupOTP(signInRequest)
                }
                ConstantCommand.GOOGLE_LOGIN -> {
                    socialSignup(signInRequest.data?.customerData?.tel?:"",signInRequest)
                }
                ConstantCommand.FACEBOOK_LOGIN -> {
                    socialSignup(signInRequest.data?.customerData?.tel?:"",signInRequest)
                }
            }
        }else {
            (getNavigator() as VarifyOtpNavigator).showFeedbackMessage(getContext().getString(R.string.warning_no_internet))
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
                                    signInRequest.data?.customerData?.tel = profiledata.username?:profiledata.tel?:""
                                    signInRequest.data?.username = profiledata.username?:""
                                    (getNavigator() as VarifyOtpNavigator).onresendSucess()
                                    responce.message?.let {
                                        (getNavigator() as VarifyOtpNavigator).showFeedbackMessage(it)
                                    }
                                }
                            }else{
                                (getNavigator() as VarifyOtpNavigator).showFeedbackMessage("Something went wrong!")
                            }
                        }else{
                            responce.message?.let {
                                (getNavigator() as VarifyOtpNavigator).showFeedbackMessage(it)}
                        }
                    },
                        {
                                throwable -> throwable.message?.let {(getNavigator() as VarifyOtpNavigator).showFeedbackMessage(it)}
                        }
                    ))

        }else {
            (getNavigator() as VarifyOtpNavigator).showFeedbackMessage(getContext().getString(R.string.warning_no_internet))
        }
    }


    private fun sendSignupOTP(loginrequest: SignInRequest) {
        if(getApplicationUtility().isInternetConnected()){
            val requestdata = BuilderManager.encodeString(GsonBuilder().create().toJson(loginrequest))
            Log.e("sendSignupOTP-",GsonBuilder().create().toJson(requestdata))
            getCompositeDisposable().add(
                getApplicationRequest().sendSignupOTP(PostData(requestdata)).doOnSuccess { }
                    .subscribeOn(getScheduleProvider().io())
                    .observeOn(getScheduleProvider().ui())
                    .subscribe({ responce ->
                        if (responce.serviceStatus.equals("S", true)) {
                            (getNavigator() as VarifyOtpNavigator).onresendSucess()
                            responce.message?.let {
                                (getNavigator() as VarifyOtpNavigator).showFeedbackMessage(it)
                            }
                        } else {
                            responce.message?.let {
                                (getNavigator() as VarifyOtpNavigator).showFeedbackMessage(it)
                            }
                        }
                    },
                        { throwable ->
                            throwable.message?.let {
                                (getNavigator() as VarifyOtpNavigator).showFeedbackMessage(
                                    it
                                )
                            }
                        }
                    ))

        }
        else{ (getNavigator() as VarifyOtpNavigator).showFeedbackMessage(getContext().getString(R.string.warning_no_internet))}

    }

    fun socialSignup(value: String,requestSigin: SignInRequest) {
        if (getApplicationUtility().isInternetConnected()) {
            val custmordata =  CustomerData()
            custmordata.tel = value
            custmordata.eMail =requestSigin.data?.customerFBData?.FBEmail
            requestSigin.data?.username = requestSigin.data?.customerFBData?.FBEmail
            requestSigin.data?.customerData = custmordata
            val requestdata = BuilderManager.encodeString(GsonBuilder().create().toJson(requestSigin))
            Log.e("social Signup",GsonBuilder().create().toJson(requestSigin))
            getCompositeDisposable().add(
                getApplicationRequest().sendSignupOTP(PostData(requestdata)).doOnSuccess { }
                    .subscribeOn(getScheduleProvider().io())
                    .observeOn(getScheduleProvider().ui())
                    .subscribe({ responce ->
                        if (responce.serviceStatus.equals("S", true)) {
                            responce.message?.let {
                                (getNavigator() as VarifyOtpNavigator).showFeedbackMessage(it)
                            }
                        } else {
                            responce.message?.let {
                                (getNavigator() as VarifyOtpNavigator).showFeedbackMessage(it)
                            }
                        }
                    },
                        { throwable ->
                            throwable.message?.let {
                                (getNavigator() as VarifyOtpNavigator).showFeedbackMessage(
                                    it
                                )
                            }
                        }
                    ))
        }
        else{ (getNavigator() as VarifyOtpNavigator).showFeedbackMessage(getContext().getString(R.string.warning_no_internet))}

    }
}