package com.exchange.user.module.order_confirm_module

import android.content.Context
import com.exchange.user.R
import com.exchange.user.module.base_module.base_command.CartDataInt
import com.exchange.user.module.base_module.base_model.PostData
import com.exchange.user.module.base_module.base_model.ResponceModel
import com.exchange.user.module.base_module.base_view.BaseViewModel
import com.exchange.user.module.profile_module.model.OrderHistory
import com.exchange.user.module.restaurent_module.model.api_model.TokenRequest
import com.exchange.user.module.restaurent_module.model.data_model.reastaurent_mode.Address
import com.exchange.user.module.utility_module.ApplicationSharePrefrence
import com.exchange.user.module.utility_module.ApplicationUtility
import com.exchange.user.module.utility_module.BuilderManager
import com.exchange.user.module.utility_module.Validations
import com.exchange.user.module.web_module.ApplicationRequest
import com.exchange.user.rx_module.SchedulerProvider
import com.google.gson.Gson
import com.google.gson.GsonBuilder

class OrderConfirmViewModel  (
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
){

    fun onBack(){
        (getNavigator() as OrderConfirmNavigator).onBacPress()
    }

    fun calltoResturant(){
        (getNavigator() as OrderConfirmNavigator).callToReasturant()
    }
    fun startNewOrder(){
        (getNavigator() as OrderConfirmNavigator).startNewOrder()
    }

    fun clearPrefrence(){
        getApplicationSharePref().setCart(null)
        getApplicationSharePref().setResturent(null)
        getApplicationSharePref().setValidCoupon(null)
        getApplicationSharePref().setMenuData(null)
    }


    fun saveData(lastorderresponce: OrderHistory) {
        CartDataInt.profiledata.orderHistory.add(0, lastorderresponce)
        val jsonrestrurant: String = GsonBuilder().setPrettyPrinting().serializeNulls().create().toJson(CartDataInt.profiledata)
        getApplicationSharePref().setProfile(jsonrestrurant)

    }

    fun  getLastOrder(){
        if (getApplicationUtility().isInternetConnected()) {
            val token = TokenRequest()
            token.tId = CartDataInt.orderdata.tId
            val data = com.exchange.user.module.restaurent_module.model.api_model.Data()
            data.custId= CartDataInt.profiledata.id
            token.data = data
            val request = BuilderManager.encodeString(GsonBuilder().create().toJson(token))
            getCompositeDisposable().add(
                getApplicationRequest().getLastOrder(PostData(request)).doOnSuccess { }
                    .subscribeOn(getScheduleProvider().io())
                    .observeOn(getScheduleProvider().ui())
                    .subscribe({ responce: ResponceModel ->

                        if (responce.serviceStatus.equals("S", true)) {
                            if (responce.data != null) {
                                responce.data?.let {
                                    val lastorderresponce: OrderHistory = Gson().fromJson(BuilderManager.decodeString(it),OrderHistory::class.java)
                                    (getNavigator() as OrderConfirmNavigator).onLastResonce(lastorderresponce)
                                    saveData(lastorderresponce)
                                }
                            } else {
                                (getNavigator() as OrderConfirmNavigator).showFeedbackMessage("Something went wrong!")
                            }
                        } else {
                            responce.message?.let {
                                (getNavigator() as OrderConfirmNavigator).showFeedbackMessage(it)
                            }
                        }
                    },
                        { throwable: Throwable? ->
                            throwable?.message?.let {
                                (getNavigator() as OrderConfirmNavigator).showFeedbackMessage(
                                    it
                                )
                            }
                        }

                    ))


        } else {
            (getNavigator() as OrderConfirmNavigator).showFeedbackMessage(getContext().getString(R.string.warning_no_internet))
        }


    }

    fun getLocationAddress(address: Address?): String {
        val redress = StringBuilder()
        if (address!= null) {
            if (!address.addressLine1.isNullOrEmpty()) {
                redress.append("${address.addressLine1},")
            }
            if (!address.addressLine2.isNullOrEmpty()) {
                redress.append("${address.addressLine2}")
            }
        }
        return redress.toString()
    }
}