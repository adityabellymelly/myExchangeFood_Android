package com.exchange.user.module.signin_module

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.provider.Settings
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
import com.exchange.user.module.signin_module.model.CustomerFBData
import com.exchange.user.module.signin_module.model.SignInRequest
import com.exchange.user.module.utility_module.ApplicationSharePrefrence
import com.exchange.user.module.utility_module.ApplicationUtility
import com.exchange.user.module.utility_module.BuilderManager
import com.exchange.user.module.utility_module.Validations
import com.exchange.user.module.web_module.ApplicationRequest
import com.exchange.user.rx_module.SchedulerProvider
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.gson.Gson
import com.google.gson.GsonBuilder

class SignIntViewModel(
    applicationprefrence: ApplicationSharePrefrence, schedulerProvider: SchedulerProvider,
    applicationutility: ApplicationUtility,
    applicationcontext: Context,
    validations: Validations,
    applicationrequest: ApplicationRequest
) : BaseViewModel(
    applicationprefrence,
    schedulerProvider,
    applicationutility,
    applicationcontext,
    validations,
    applicationrequest
) {

    fun faceBookLogin() {
        (getNavigator() as SignInNavigator).faceBookLogin()
    }
    fun googleLogin() {
        (getNavigator() as SignInNavigator).googleLogin()
    }
    fun forgotPassword() {
        (getNavigator() as SignInNavigator).forgotPassword()
    }

    fun signIn() {
        (getNavigator() as SignInNavigator).signIn()
    }

    fun createAccount() {
        (getNavigator() as SignInNavigator).createAccount()
    }

    @SuppressLint("HardwareIds")
    fun sigInUser(email: String, password: String) {
            if (getValidation().isPhoneNumberValid(password) && getValidation().isEmailValid(email).not()) {
            (getNavigator() as SignInNavigator).showError(
                ConstantCommand.EMAIL_ERROR,
                getContext().getString(R.string.invalid_email)
            )
            } else if (getValidation().isFieldEmpty(password)) {
            (getNavigator() as SignInNavigator).showError(
                ConstantCommand.PASSWORD_ERROR,
                getContext().getString(R.string.invalid_password)
            )
        } else {
            (getNavigator() as SignInNavigator).showError(ConstantCommand.NO_ERROR, "")
            val loginrequest = SignInRequest()
            val logindata = SignInRequest.Data()
            logindata.password = password
            logindata.username = email
            logindata.deviceId = Settings.Secure.getString(getContext().contentResolver, Settings.Secure.ANDROID_ID)
            logindata.FCMToken= getApplicationSharePref().getFCMToken()
            loginrequest.data = logindata
            if (CartDataInt.orderdata.tId.isNullOrEmpty().not()) {
                loginrequest.tId = CartDataInt.orderdata.tId ?: ""
                Login(loginrequest)
            } else {
                getToken(loginrequest, ConstantCommand.NORMAL_LOGIN)
            }
        }

    }

    private fun Login(loginrequest: SignInRequest) {
        if (getApplicationUtility().isInternetConnected()) {
            val requestdata =
                BuilderManager.encodeString(GsonBuilder().create().toJson(loginrequest))
            getCompositeDisposable().add(
                getApplicationRequest().doLogin(PostData(requestdata)).doOnSuccess { }
                    .subscribeOn(getScheduleProvider().io())
                    .observeOn(getScheduleProvider().ui())
                    .subscribe({ responce ->
                        if (responce.serviceStatus.equals("S", true)) {
                            if (responce.data != null) {
                                responce.data?.let {
                                    val profiledata: ProfileModel = Gson().fromJson(
                                        BuilderManager.decodeString(it),
                                        ProfileModel::class.java
                                    )
                                    CartDataInt.profiledata = profiledata
                                    saveData(profiledata)
                                    (getNavigator() as SignInNavigator).moveToNext()
                                }
                            } else {
                                (getNavigator() as SignInNavigator).showFeedbackMessage("Something went wrong!")
                            }

                        } else {
                            responce.message?.let {
                                (getNavigator() as SignInNavigator).showFeedbackMessage(it)
                            }
                        }
                    },
                        { throwable ->
                            throwable.message?.let {
                                (getNavigator() as SignInNavigator).showFeedbackMessage(
                                    it
                                )
                            }
                        }
                    ))

        } else {
            (getNavigator() as SignInNavigator).showFeedbackMessage(getContext().getString(R.string.warning_no_internet))
        }
    }

    @SuppressLint("HardwareIds")
    fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            account?.let {
                if (getApplicationUtility().isInternetConnected()) {
                    val request = SignInRequest()
                    val datavalue = SignInRequest.Data()
                    val customedata = CustomerFBData()
                    customedata.FBEmail = it.email ?: it.displayName?.toString()?.trim().plus("@gmail.com")
                    customedata.FName = it.displayName?.toString()
                    customedata.LName = ""
                    customedata.FId = it.id ?: "0"
                    datavalue.customerFBData = customedata
                    datavalue.deviceId = Settings.Secure.getString(getContext().contentResolver, Settings.Secure.ANDROID_ID)
                    datavalue.FCMToken= getApplicationSharePref().getFCMToken()
                    request.data = datavalue
                    if (CartDataInt.orderdata.tId.isNullOrEmpty().not()) {
                        request.tId = CartDataInt.orderdata.tId ?: ""
                        socialLogin(request, ConstantCommand.GOOGLE_LOGIN)
                    } else {
                        getToken(request, ConstantCommand.GOOGLE_LOGIN)
                    }

                } else {
                    (getNavigator() as SignInNavigator).showFeedbackMessage(getContext().getString(R.string.warning_no_internet))
                }
            }
            (getNavigator() as SignInNavigator).signOutGoogleLogin()
        } catch (e: ApiException) {
            e.printStackTrace()
        }
    }

    @SuppressLint("DefaultLocale", "HardwareIds")
    fun getProfileData() {
        try {
            val request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken()
            ) { data, response ->
                try {
                    Log.e("facebook responce", response.toString())
                    data?.let {
                        if (getApplicationUtility().isInternetConnected()) {
                            val request = SignInRequest()
                            val datavalue = SignInRequest.Data()
                            val emailaddress = StringBuilder()
                            if (it.has("email")) {
                                emailaddress.append(it.getString("email"))
                            } else {
                                emailaddress.append(
                                    "${(it.getString("name").replace(
                                        " ",
                                        ""
                                    )).toLowerCase()}@gmail.com"
                                )
                            }
                            val customedata = CustomerFBData()
                            customedata.FBEmail = emailaddress.toString()
                            customedata.FName = it.getString("name")
                            customedata.LName = ""
                            customedata.FId = it.getString("id")
                            datavalue.customerFBData = customedata
                            datavalue.deviceId = Settings.Secure.getString(getContext().contentResolver, Settings.Secure.ANDROID_ID)
                            datavalue.FCMToken= getApplicationSharePref().getFCMToken()
                            request.data = datavalue

                            if (CartDataInt.orderdata.tId.isNullOrEmpty().not()) {
                                request.tId = CartDataInt.orderdata.tId ?: ""
                                socialLogin(request, ConstantCommand.FACEBOOK_LOGIN)
                            } else {
                                getToken(request, ConstantCommand.FACEBOOK_LOGIN)
                            }

                        } else {
                            (getNavigator() as SignInNavigator).showFeedbackMessage(
                                getContext().getString(
                                    R.string.warning_no_internet
                                )
                            )
                        }
                        (getNavigator() as SignInNavigator).signOutFacebookLogin()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            val parameters = Bundle()
            //parameters.putString("fields", "id,name,link,email")
            parameters.putString("fields", "id,name,link,birthday,gender,email,picture.type(large)")
            request.parameters = parameters
            request.executeAsync()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun socialLogin(request: SignInRequest, from: Int) {
        if (getApplicationUtility().isInternetConnected()) {
            val requestdata = BuilderManager.encodeString(GsonBuilder().create().toJson(request))
            Log.e("socialLogin request", GsonBuilder().create().toJson(request))
            if (from == ConstantCommand.GOOGLE_LOGIN)
                getCompositeDisposable().add(
                    getApplicationRequest().checkGoogleAccountExists(PostData(requestdata)).doOnSuccess { }
                        .subscribeOn(getScheduleProvider().io())
                        .observeOn(getScheduleProvider().ui())
                        .subscribe({ responce ->
                            if (responce.serviceStatus.equals("S", true)) {
                                if (responce.data != null) {
                                    responce.data?.let { it ->
                                        val profiledata: ProfileModel = Gson().fromJson(
                                            BuilderManager.decodeString(it),
                                            ProfileModel::class.java
                                        )
                                        profiledata.isExistsIMenuAccount?.let {
                                            if (it == 2L) {
                                                CartDataInt.profiledata = profiledata
                                                saveData(profiledata)
                                                (getNavigator() as SignInNavigator).moveToNext()
                                            }
                                            else if (it ==1L){
                                                request.data?.username = request.data?.customerFBData?.FBEmail
                                                (getNavigator() as SignInNavigator).openConfirmation(request,from)
                                            }
                                            else {
                                                // account does not extst
                                                (getNavigator() as SignInNavigator).takeMobileNo(
                                                    from,
                                                    request
                                                )
                                            }
                                        }

                                    }
                                } else {
                                    (getNavigator() as SignInNavigator).showFeedbackMessage("Something went wrong!")
                                }

                            } else {
                                responce.message?.let {
                                    (getNavigator() as SignInNavigator).showFeedbackMessage(it)
                                }
                            }
                        },
                            { throwable ->
                                throwable.message?.let {
                                    (getNavigator() as SignInNavigator).showFeedbackMessage(
                                        it
                                    )
                                }
                            }
                        ))
            else
                getCompositeDisposable().add(
                    getApplicationRequest().CheckAccountExists(PostData(requestdata)).doOnSuccess { }
                        .subscribeOn(getScheduleProvider().io())
                        .observeOn(getScheduleProvider().ui())
                        .subscribe({ responce ->
                            if (responce.serviceStatus.equals("S", true)) {
                                if (responce.data != null) {
                                    responce.data?.let { it ->
                                        val profiledata: ProfileModel = Gson().fromJson(
                                            BuilderManager.decodeString(it),
                                            ProfileModel::class.java
                                        )
                                        profiledata.isExistsIMenuAccount?.let {
                                            if (it == 2L) {
                                                CartDataInt.profiledata = profiledata
                                                saveData(profiledata)
                                                (getNavigator() as SignInNavigator).moveToNext()
                                            } else {
                                                // account does not extst
                                                (getNavigator() as SignInNavigator).takeMobileNo(
                                                    from,
                                                    request
                                                )
                                            }
                                        }

                                    }
                                } else {
                                    (getNavigator() as SignInNavigator).showFeedbackMessage("Something went wrong!")
                                }

                            } else {
                                responce.message?.let {
                                    (getNavigator() as SignInNavigator).showFeedbackMessage(it)
                                }
                            }
                        },
                            { throwable ->
                                throwable.message?.let {
                                    (getNavigator() as SignInNavigator).showFeedbackMessage(
                                        it
                                    )
                                }
                            }
                        ))

        } else {
            (getNavigator() as SignInNavigator).showFeedbackMessage(getContext().getString(R.string.warning_no_internet))
        }

    }
    private fun saveData(it: ProfileModel) {
        if (it.email.isNullOrEmpty()) {
            it.username?.let {
                if (getValidation().isValidEmail(it)) {
                    getApplicationSharePref().setEmail(it)
                }
            }

        } else {
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
            username.append(" $it")
        }
        it.tel?.let {
            getApplicationSharePref().setPhoneNumber(it)
        }
        it.id?.let {
            getApplicationSharePref().setUserID(it.toString())
        }
        getApplicationSharePref().setUserName(username.toString())
        getApplicationSharePref().setReward((it.loyaltyPoints ?: "0").toDouble())
        getApplicationSharePref().setProfile(GsonBuilder().setPrettyPrinting().serializeNulls().create().toJson(it))

    }

    private fun getToken(signInRequest: SignInRequest, from: Int) {
        if (getApplicationUtility().isInternetConnected()) {
            showProgress()
            val token = TokenRequest()
            token.appCode = ConstantCommand.APP_TOKEN
            token.mobUrl = ConstantCommand.URl.plus("=").plus(ConstantCommand.urlName)
            val data = com.exchange.user.module.restaurent_module.model.api_model.Data()
            data.mobUrl = ConstantCommand.urlName
            data.traceId = ""
            token.data = data
            val request = BuilderManager.encodeString(GsonBuilder().create().toJson(token))
            Log.e("Token request", GsonBuilder().create().toJson(token))
            getCompositeDisposable().add(
                getApplicationRequest().getToken(PostData(request)).doOnSuccess { }
                    .subscribeOn(getScheduleProvider().io())
                    .observeOn(getScheduleProvider().ui())
                    .subscribe({ responce: ResponceModel ->
                        hideProgress()
                        if (responce.serviceStatus.equals("S", true)) {
                            if (responce.data != null) {
                                responce.data?.let {
                                    val tokendata: TokenResponce = Gson().fromJson(
                                        BuilderManager.decodeString(it),
                                        TokenResponce::class.java
                                    )
                                    tokendata.tId?.let {
                                        CartDataInt.orderdata.tId = tokendata.tId
                                        CartDataInt.orderdata.chainId = tokendata.chainId
                                        signInRequest.tId = tokendata.tId ?: ""
                                        if (from == ConstantCommand.NORMAL_LOGIN)
                                            Login(signInRequest)
                                        else
                                            socialLogin(signInRequest, from)
                                    }
                                }
                            } else {
                                (getNavigator() as SignInNavigator).showFeedbackMessage("Something went wrong!")
                            }

                        } else {
                            responce.message?.let {
                                (getNavigator() as SignInNavigator).showFeedbackMessage(it)
                            }
                        }
                    },
                        { throwable: Throwable? ->
                            hideProgress()
                            throwable?.message?.let {
                                (getNavigator() as SignInNavigator).showFeedbackMessage(it)
                            }
                        }

                    )
            )

        } else {
            (getNavigator() as SignInNavigator).showFeedbackMessage(getContext().getString(R.string.warning_no_internet))
        }
    }

    fun socialSignup(from: Int, value: String, requestSigin: SignInRequest) {
        if (getApplicationUtility().isInternetConnected()) {
            val custmordata = CustomerData()
            custmordata.tel = value
            custmordata.eMail = requestSigin.data?.customerFBData?.FBEmail
            requestSigin.data?.username = requestSigin.data?.customerFBData?.FBEmail
            requestSigin.data?.customerData = custmordata
            val requestdata =
                BuilderManager.encodeString(GsonBuilder().create().toJson(requestSigin))
            Log.e("social Signup", GsonBuilder().create().toJson(requestSigin))
           // (getNavigator() as SignInNavigator).openVerifyScreen(requestSigin, from)
            getCompositeDisposable().add(
                getApplicationRequest().sendSignupOTP(PostData(requestdata)).doOnSuccess { }
                    .subscribeOn(getScheduleProvider().io())
                    .observeOn(getScheduleProvider().ui())
                    .subscribe({ responce ->
                        if (responce.serviceStatus.equals("S", true)) {
                            responce.message?.let {
                                (getNavigator() as SignInNavigator).showFeedbackMessage(it)
                            }
                            (getNavigator() as SignInNavigator).openVerifyScreen(requestSigin, from)

                        } else {
                            responce.message?.let {
                                (getNavigator() as SignInNavigator).showFeedbackMessage(it)
                            }
                        }
                    },
                        { throwable ->
                            throwable.message?.let {
                                (getNavigator() as SignInNavigator).showFeedbackMessage(
                                    it
                                )
                            }
                        }
                    ))
        } else {
            (getNavigator() as SignInNavigator).showFeedbackMessage(getContext().getString(R.string.warning_no_internet))
        }

    }

    fun mergeAccount(password: String, request: SignInRequest,from: Int) {
        if (getApplicationUtility().isInternetConnected()) {
            request.data?.password = password
            val requestdata = BuilderManager.encodeString(GsonBuilder().create().toJson(request))
            Log.e("mearge acc", GsonBuilder().create().toJson(request))
            getCompositeDisposable().add(
                getApplicationRequest().domergeGoogleData(PostData(requestdata)).doOnSuccess { }
                    .subscribeOn(getScheduleProvider().io())
                    .observeOn(getScheduleProvider().ui())
                    .subscribe({ responce ->
                        if (responce.serviceStatus.equals("S", true)) {
                            request.data?.password = null
                            request.data?.username = null
                            socialLogin(request,from)
                        } else {
                            responce.message?.let {
                                (getNavigator() as SignInNavigator).showFeedbackMessage(it)
                            }
                        }
                    },
                        { throwable ->
                            throwable.message?.let {
                                (getNavigator() as SignInNavigator).showFeedbackMessage(
                                    it
                                )
                            }
                        }
                    ))
        } else {
            (getNavigator() as SignInNavigator).showFeedbackMessage(getContext().getString(R.string.warning_no_internet))
        }
    }

    fun saveFcmToken(token: String) {
       getApplicationSharePref().setFCMToken(token)
    }

}