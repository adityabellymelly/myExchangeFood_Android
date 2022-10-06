package com.exchange.user.module.edit_profile_module

import android.content.Context
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

class EditProfileViewModel (applicationprefrence: ApplicationSharePrefrence, schedulerProvider : SchedulerProvider,
                            applicationutility : ApplicationUtility,
                            applicationcontext : Context,
                            validations  : Validations,
                            applicationrequest : ApplicationRequest
) : BaseViewModel(applicationprefrence,schedulerProvider, applicationutility, applicationcontext , validations , applicationrequest){

    fun onBack(){
        (getNavigator() as EditProfileNavigator).onBackPress()
    }

    fun save(){
        (getNavigator() as EditProfileNavigator).updateProfile()
    }

    fun getProfileData(): ProfileModel? {
        getApplicationSharePref().getProfile()?.let {
            val profileData: ProfileModel = Gson().fromJson(it, ProfileModel::class.java)
            return profileData
        }
        return  null
     }

    fun getUserID(): String?=getApplicationSharePref().getUserID()
    fun saveAndUpdate(firstName:String, lastName:String, userName:String, phoneNo:String) {
        when{
            getValidation().isFieldEmpty(firstName) -> {
                (getNavigator() as EditProfileNavigator).showError(ConstantCommand.USERNAME_ERROR,"Invalid First Name")
            }
            getValidation().isFieldEmpty(lastName) -> {
                (getNavigator() as EditProfileNavigator).showError(ConstantCommand.PASSWORD_ERROR,
                    "Invalid Last Name")
            }
            getValidation().isEmailValid(userName).not() -> {
                (getNavigator() as EditProfileNavigator).showError(ConstantCommand.EMAIL_ERROR,"Invalid Username")

            }
            getValidation().isFieldEmpty(phoneNo) -> {
                (getNavigator() as EditProfileNavigator).showError(ConstantCommand.M0BILE_ERROR,"Invalid Phone no.")

            }
            else ->{
                (getNavigator() as EditProfileNavigator).showError(ConstantCommand.NORMAL_LOGIN,"")
                val loginrequest = SignInRequest()
                val logindata = SignInRequest.Data()
                logindata.username = userName
                logindata.customerData = CustomerData()
                logindata.customerData?.tel =phoneNo
                logindata.customerData?.fName =firstName
                logindata.customerData?.mName =""
                logindata.customerData?.lName =lastName
                logindata.customerData?.cell =""
                logindata.customerData?.eMail =userName
                logindata.customerData?.custId = getApplicationSharePref().getUserID()?.toLong()
                logindata.customerData?.mobileOptIn = 1
                logindata.customerData?.optIn="F"
                if (CartDataInt.orderdata.tId.isNullOrEmpty().not()) {
                    loginrequest.tId = CartDataInt.orderdata.tId ?: ""
                    loginrequest.data = logindata
                    updateData(loginrequest)
                } else {
                    logindata.locname = ConstantCommand.locName
                    loginrequest.data = logindata
                    getToken(loginrequest)
                }


            }
        }

    }

    private fun updateData(logInRequest: SignInRequest) {

        if(getApplicationUtility().isInternetConnected()){
            (getNavigator() as EditProfileNavigator).showProgress(true)
            val requestData = BuilderManager.encodeString(GsonBuilder().create().toJson(logInRequest))
            getCompositeDisposable().add(
                getApplicationRequest().updateUserProfile(PostData(requestData)).doOnSuccess { }
                    .subscribeOn(getScheduleProvider().io())
                    .observeOn(getScheduleProvider().ui())
                    .subscribe({ responce ->
                        (getNavigator() as EditProfileNavigator).showProgress(false)
                        if (responce.serviceStatus.equals("S", true)) {
                            val profileData = CartDataInt.profiledata
                            profileData.tel = logInRequest.data?.customerData?.tel
                            profileData.email = logInRequest.data?.customerData?.eMail
                            profileData.fName = logInRequest.data?.customerData?.fName
                            profileData.lName = logInRequest.data?.customerData?.lName
                            CartDataInt.profiledata = profileData
                            saveData(profileData)
                            responce.message?.let {
                                (getNavigator() as EditProfileNavigator).showFeedbackMessage(it)
                            }
                           onBack()
                        } else {
                            responce.message?.let {
                                (getNavigator() as EditProfileNavigator).showFeedbackMessage(it)
                            }
                        }
                    },
                        { throwable ->
                            (getNavigator() as EditProfileNavigator).showProgress(false)
                            throwable.message?.let {
                                (getNavigator() as EditProfileNavigator).showFeedbackMessage(
                                    it
                                )
                            }
                        }
                    ))

        }
        else{
         (getNavigator() as EditProfileNavigator).showFeedbackMessage(getContext().getString(R.string.warning_no_internet))}

    }

    private fun saveData(profileModel: ProfileModel) {
        if (profileModel.email.isNullOrEmpty()) {
            profileModel.username?.let {
                if (getValidation().isValidEmail(it)) {
                    getApplicationSharePref().setEmail(it)
                }
            }

        } else {
            profileModel.email?.let {
                if (getValidation().isValidEmail(it)) {
                    getApplicationSharePref().setEmail(it)
                }
            }
        }
        val username = StringBuilder()
        profileModel.fName?.let {
            username.append(it)
        }
        profileModel.lName?.let {
            username.append(" $it")
        }
        profileModel.tel?.let {
            getApplicationSharePref().setPhoneNumber(it)
        }
        profileModel.id?.let {
            getApplicationSharePref().setUserID(it.toString())
        }
        getApplicationSharePref().setUserName(username.toString())
        getApplicationSharePref().setReward((profileModel.loyaltyPoints ?: "0").toDouble())
        getApplicationSharePref().setProfile(GsonBuilder().setPrettyPrinting().serializeNulls().create().toJson(profileModel))

    }


    private fun getToken(signinrequest: SignInRequest) {
        if(getApplicationUtility().isInternetConnected()){
            (getNavigator() as EditProfileNavigator).showProgress(true)
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

                        (getNavigator() as EditProfileNavigator).showProgress(false)

                        if (responce.serviceStatus.equals("S",true)){
                            if (responce.data!=null){
                                responce.data?.let {
                                    val tokendata : TokenResponce = Gson().fromJson(
                                        BuilderManager.decodeString(it),
                                        TokenResponce::class.java)
                                    tokendata.tId?.let {
                                        CartDataInt.orderdata.tId=tokendata.tId
                                        CartDataInt.orderdata.chainId = tokendata.chainId
                                        signinrequest.tId = tokendata.tId?:""
                                        updateData(signinrequest)
                                    }
                                }
                            }else{
                                (getNavigator() as EditProfileNavigator).showFeedbackMessage("Something went wrong!")
                            }

                        }else{
                            responce.message?.let {
                                (getNavigator() as EditProfileNavigator).showFeedbackMessage(it)}
                        }
                    },
                        { throwable : Throwable? ->

                            (getNavigator() as EditProfileNavigator).showProgress(false)

                            throwable?.message?.let {
                            (getNavigator() as EditProfileNavigator).showFeedbackMessage(it)
                        } }

                    )
            )

        }
        else{ (getNavigator() as EditProfileNavigator).showFeedbackMessage(getContext().getString(R.string.warning_no_internet))}
    }

}