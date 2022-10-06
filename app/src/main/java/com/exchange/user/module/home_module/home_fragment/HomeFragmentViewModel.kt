package com.exchange.user.module.home_module.home_fragment

import android.content.Context
import com.exchange.user.module.base_module.base_view.BaseViewModel
import com.exchange.user.module.country_module.model.Country
import com.exchange.user.module.country_module.model.location_model.RegionList
import com.exchange.user.module.utility_module.ApplicationSharePrefrence
import com.exchange.user.module.utility_module.ApplicationUtility
import com.exchange.user.module.utility_module.Validations
import com.exchange.user.module.web_module.ApplicationRequest
import com.exchange.user.rx_module.SchedulerProvider
import com.google.gson.Gson

class HomeFragmentViewModel(
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


    fun changeLocation() {
        (getNavigator() as HomeFragmentNavigator).openCountryScreen()
    }

    fun getLocation(): String {
            val locationname = StringBuilder()
            locationname.append(getRegion())
            getSelectedCountry()?.let {
                locationname.append(",${it.name}")
            }
            return locationname.toString()
    }

    private fun getSelectedCountry(): Country? {
        val countrydata = getApplicationSharePref().getSelectCountry()
        return if (countrydata.isNullOrEmpty()) {
            null
        } else {
            Gson().fromJson(countrydata, Country::class.java)
        }
    }

    private fun getRegion(): String{
       val region = getApplicationSharePref().getLocalRestrurent()
        if (region.isNullOrEmpty().not()) {
            val regiondata: RegionList = Gson().fromJson(region,RegionList::class.java)
            (getNavigator() as HomeFragmentNavigator).updateRestrauent(regiondata.LocList)
            return regiondata.RegionName
        }
        return ""
    }
}