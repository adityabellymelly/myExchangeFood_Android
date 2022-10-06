package com.exchange.user.module.about_us_module

import android.content.Context
import com.exchange.user.R
import com.exchange.user.module.about_us_module.model.DeliveryProgramResponse
import com.exchange.user.module.base_module.base_view.BaseViewModel
import com.exchange.user.module.utility_module.ApplicationSharePrefrence
import com.exchange.user.module.utility_module.ApplicationUtility
import com.exchange.user.module.utility_module.Validations
import com.exchange.user.module.web_module.ApplicationRequest
import com.exchange.user.rx_module.SchedulerProvider

class AboutUsViewModel(
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


    fun  goToSignIn(){
        (getNavigator() as AboutUsNavigator).openSiginActivity()
    }
    fun getUserID(): String?=getApplicationSharePref().getUserID()
    fun getAboutUS() {
        if (getApplicationUtility().isInternetConnected()) {
            getCompositeDisposable().add(
                getApplicationRequest().aboutUs("deliveryprogram.json").doOnSuccess { }
                    .subscribeOn(getScheduleProvider().io())
                    .observeOn(getScheduleProvider().ui())
                    .subscribe({ response: DeliveryProgramResponse ->
                        (getNavigator() as AboutUsNavigator).updateAbout(response)
                    },
                        { throwable: Throwable? ->
                            throwable?.message?.let {
                                (getNavigator() as AboutUsNavigator).showFeedbackMessage(
                                    it
                                )
                            }
                        }))
        } else {
            (getNavigator() as AboutUsNavigator).showFeedbackMessage(getContext().getString(R.string.warning_no_internet))
        }
    }

}