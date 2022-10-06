package com.exchange.user.module.choose_order_module

import android.content.Context
import com.exchange.user.module.base_module.base_view.BaseViewModel
import com.exchange.user.module.utility_module.ApplicationSharePrefrence
import com.exchange.user.module.utility_module.ApplicationUtility
import com.exchange.user.module.utility_module.Validations
import com.exchange.user.module.web_module.ApplicationRequest
import com.exchange.user.rx_module.SchedulerProvider

class ChooseOrderViewModel (applicationprefrence: ApplicationSharePrefrence, schedulerProvider : SchedulerProvider,
                            applicationutility : ApplicationUtility,
                            applicationcontext : Context,
                            validations  : Validations,
                            applicationrequest : ApplicationRequest
): BaseViewModel(applicationprefrence,schedulerProvider, applicationutility, applicationcontext , validations , applicationrequest){


    fun getStarted(){
        (getNavigator() as ChooseOrderNavigator).openCountryActivity()
    }
    fun privacyPolicy(){
        (getNavigator() as ChooseOrderNavigator).openPolicy()
    }
    fun termsCondition(){
        (getNavigator() as ChooseOrderNavigator).openTermsCondition()
    }
}