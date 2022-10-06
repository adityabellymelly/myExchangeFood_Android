package com.exchange.user.module.profile_module

import android.content.Context
import com.exchange.user.R
import com.exchange.user.module.base_module.ConstantCommand
import com.exchange.user.module.base_module.base_command.CartDataInt
import com.exchange.user.module.base_module.base_model.PostData
import com.exchange.user.module.base_module.base_model.ResponceModel
import com.exchange.user.module.base_module.base_view.BaseViewModel
import com.exchange.user.module.cart_module.model.card_model.DeliveryInfo
import com.exchange.user.module.country_module.model.Country
import com.exchange.user.module.country_module.model.location_model.RegionList
import com.exchange.user.module.profile_module.model.DeleteAccount
import com.exchange.user.module.profile_module.model.DeleteData
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

class ProfileFragmentViewModel (
    applicationprefrence: ApplicationSharePrefrence, schedulerProvider: SchedulerProvider,
    applicationutility: ApplicationUtility, applicationcontext: Context, validations: Validations, applicationrequest: ApplicationRequest) : BaseViewModel(
    applicationprefrence,
    schedulerProvider,
    applicationutility,
    applicationcontext,
    validations,
    applicationrequest
) {

    fun changeLocation(){
        (getNavigator() as ProfileFragmentNavigator).changeLocation()
    }
    fun openOptions(){
        (getNavigator() as ProfileFragmentNavigator).openOptions()
    }
    fun orderHistory(){(getNavigator() as ProfileFragmentNavigator).onOrderhistory()}
    fun logOut(){
        if (getApplicationSharePref().getUserID()==null){
            (getNavigator() as ProfileFragmentNavigator).openSiginActivity()
        }else{
            getApplicationSharePref().clearPrefrences()
            getApplicationSharePref().setFirst("first")
            (getNavigator() as ProfileFragmentNavigator).updateUI()
        }
    }
    fun editProfile(){
        (getNavigator() as ProfileFragmentNavigator).openEditActivity()
    }
    fun changePassword(){
        (getNavigator() as ProfileFragmentNavigator).openChangePassword()
    }

    fun goToSignIn(){
        (getNavigator() as ProfileFragmentNavigator).openSiginActivity()
    }
    fun inviteFriend(){(
            getNavigator() as ProfileFragmentNavigator).inviteFriend() }

    fun getUserID(): String?=getApplicationSharePref().getUserID()
    fun getUserName(): String?  = getApplicationSharePref().getUserName()
    fun getPhoneNumber(): String?  = getApplicationSharePref().getPhoneNumber()
    fun getEmail(): String?  = getApplicationSharePref().getEmail()

    fun delete(){
        if (CartDataInt.orderdata.tId.isNullOrEmpty().not()){
            deleteAccount()
        }else{

            val request = SignInRequest()
            val dataValue = SignInRequest.Data()
            dataValue.custId = getApplicationSharePref().getUserID()?.toLong()
            request.data  = dataValue
            getTokenforDelete(request)
        }

    }

    private fun getTokenforDelete(signInRequest: SignInRequest) {
        if (getApplicationUtility().isInternetConnected()) {
            showProgress()
            val token = TokenRequest()
            token.appCode = ConstantCommand.APP_TOKEN
            token.mobUrl = ConstantCommand.URl.plus("=").plus(ConstantCommand.urlName)
            val data = com.exchange.user.module.restaurent_module.model.api_model.Data()
            data.mobUrl = ConstantCommand.urlName
            data.traceId = ""
            token.data = data
            getCompositeDisposable().add(
                getApplicationRequest().getToken(PostData(BuilderManager.encodeString(GsonBuilder().create().toJson(token)))).doOnSuccess { }
                    .subscribeOn(getScheduleProvider().io())
                    .observeOn(getScheduleProvider().ui())
                    .subscribe({ responce: ResponceModel ->
                        hideProgress()
                        if (responce.serviceStatus.equals("S", true)) {
                            if (responce.data != null) {
                                responce.data?.let { it ->
                                    val tokendata: TokenResponce = Gson().fromJson(
                                        BuilderManager.decodeString(it),
                                        TokenResponce::class.java
                                    )
                                    CartDataInt.orderdata.chainId =tokendata.chainId
                                    tokendata.tId?.let {
                                        signInRequest.tId = it
                                        CartDataInt.orderdata.tId =it
                                        deleteAccount()
                                    }
                                }
                            } else {
                                (getNavigator() as ProfileFragmentNavigator).showFeedbackMessage("Something went wrong!")
                            }

                        } else {
                            responce.message?.let {
                                (getNavigator() as ProfileFragmentNavigator).showFeedbackMessage(it)
                            }
                        }
                    },
                        { throwable: Throwable? ->
                            hideProgress()
                            throwable?.message?.let {
                                (getNavigator() as ProfileFragmentNavigator).showFeedbackMessage(it)
                            }
                        }

                    )
            )

        } else {
            (getNavigator() as ProfileFragmentNavigator).showFeedbackMessage(getContext().getString(R.string.warning_no_internet))
        }
    }

    fun deleteAccount() {
        if (getApplicationUtility().isInternetConnected()) {
            val deleteAccount = DeleteAccount()
            deleteAccount.tId = CartDataInt.orderdata.tId
            val data = DeleteData();
            data.UserId = CartDataInt.profiledata.userid?:""
            data.chainid = ConstantCommand.chainId //CartDataInt.orderdata.chainId
            deleteAccount.data = data
            val request = BuilderManager.encodeString(GsonBuilder().create().toJson(deleteAccount))
            getCompositeDisposable().add(
                getApplicationRequest().deleteUseriOS(PostData(request)).doOnSuccess { }
                    .subscribeOn(getScheduleProvider().io())
                    .observeOn(getScheduleProvider().ui())
                    .subscribe({ responce: ResponceModel ->
                        hideProgress()
                        if (responce.serviceStatus.equals("S", true)) {
                            getApplicationSharePref().clearPrefrences()
                            getApplicationSharePref().setFirst("first")
                            (getNavigator() as ProfileFragmentNavigator).updateUI()
                        }else {
                            responce.message?.let {
                                (getNavigator() as ProfileFragmentNavigator).showFeedbackMessage(it)
                            }
                        }
                    },
                        { throwable: Throwable? ->
                            hideProgress()
                            throwable?.message?.let {
                                (getNavigator() as ProfileFragmentNavigator).showFeedbackMessage(it)
                            }
                        }

                    )
            )

        }else {
            (getNavigator() as ProfileFragmentNavigator).
            showFeedbackMessage(getContext().getString(R.string.warning_no_internet))
        }

    }




    fun getLocation(): String {
        val locationName = StringBuilder()
        locationName.append(getRegion())
        getSelectedCountry()?.let {
            locationName.append(",${it.name}")
        }
        return locationName.toString()
    }

    private fun getSelectedCountry(): Country? {
        val countryData = getApplicationSharePref().getSelectCountry()
        return if (countryData.isNullOrEmpty()) {
            null
        } else {

            Gson().fromJson(countryData, Country::class.java)
        }
    }

    private fun getRegion(): String{
        val region = getApplicationSharePref().getLocalRestrurent()
        if (region.isNullOrEmpty().not()) {
            val regionData: RegionList = Gson().fromJson(region,RegionList::class.java)
            return regionData.RegionName
        }
        return ""
    }


    fun getAddress(): String {
        getApplicationSharePref().getAddress()?.let {
            val deliveryinfo: DeliveryInfo = Gson().fromJson(it, DeliveryInfo::class.java)
            val restaddress = StringBuilder()
            if (!deliveryinfo.addr1.isNullOrEmpty()) {
                restaddress.append("${deliveryinfo.addr1},")
            }
            if (!deliveryinfo.addr2.isNullOrEmpty()) {
                restaddress.append("${deliveryinfo.addr2},")
            }
            if (!deliveryinfo.city.isNullOrEmpty()) {
                restaddress.append("${deliveryinfo.city},")
            }

            if (!deliveryinfo.state.isNullOrEmpty()) {
                restaddress.append("${deliveryinfo.state},")
            }
            if (!deliveryinfo.zip.isNullOrEmpty()) {
                restaddress.append("${deliveryinfo.zip},")
            }
            return restaddress.toString()
        }

        return ""
    }
    fun getReward(): String?  = getApplicationSharePref().getReward()

}