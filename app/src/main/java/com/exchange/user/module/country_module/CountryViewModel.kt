package com.exchange.user.module.country_module

import android.content.Context
import com.exchange.user.R
import com.exchange.user.module.base_module.ConstantCommand
import com.exchange.user.module.base_module.base_view.BaseViewModel
import com.exchange.user.module.country_module.model.Country
import com.exchange.user.module.country_module.model.CountryModel
import com.exchange.user.module.country_module.model.location_model.LocationModel
import com.exchange.user.module.country_module.model.location_model.RegionList
import com.exchange.user.module.restaurent_module.model.api_model.TokenResponce
import com.exchange.user.module.utility_module.ApplicationSharePrefrence
import com.exchange.user.module.utility_module.ApplicationUtility
import com.exchange.user.module.utility_module.BuilderManager
import com.exchange.user.module.utility_module.Validations
import com.exchange.user.module.web_module.ApplicationRequest
import com.exchange.user.rx_module.SchedulerProvider
import com.google.gson.Gson

class CountryViewModel(applicationprefrence: ApplicationSharePrefrence, schedulerProvider : SchedulerProvider,
                       applicationutility : ApplicationUtility,
                       applicationcontext : Context,
                       validations  : Validations,
                       applicationrequest : ApplicationRequest
) : BaseViewModel(applicationprefrence,schedulerProvider, applicationutility, applicationcontext , validations , applicationrequest){

    fun onCheck(){
        (getNavigator() as CountryNavigator).onCheck()
    }

    fun getCountry(){
        val countrydatasf = getApplicationSharePref().getCountry()
        if (countrydatasf.isNullOrEmpty().not()){
            val countrymodel: CountryModel = Gson().fromJson(countrydatasf, CountryModel::class.java)
            (getNavigator() as CountryNavigator).onCountrys(countrymodel)
            if(getApplicationUtility().isInternetConnected()){
                getCompositeDisposable().add(
                    getApplicationRequest().country("countrylist.json").doOnSuccess {  }
                        .subscribeOn(getScheduleProvider().io())
                        .observeOn(getScheduleProvider().ui())
                        .subscribe({responce: CountryModel ->
                            getCountryData(responce)
                        },
                            { throwable : Throwable? ->
                                throwable?.message?.let {(getNavigator() as CountryNavigator).showFeedbackMessage(it)} }

                        )
                )
            }
            else{ (getNavigator() as CountryNavigator).showFeedbackMessage(getContext().getString(R.string.warning_no_internet))}

        }else{

            if(getApplicationUtility().isInternetConnected()){
                showProgress()
                getCompositeDisposable().add(
                    getApplicationRequest().country("countrylist.json").doOnSuccess {  }
                        .subscribeOn(getScheduleProvider().io())
                        .observeOn(getScheduleProvider().ui())
                        .subscribe({responce: CountryModel ->
                            hideProgress()
                            getCountryData(responce)
                        },
                            { throwable : Throwable? ->
                                hideProgress()
                                throwable?.message?.let {(getNavigator() as CountryNavigator).showFeedbackMessage(it)} }

                        )
                )
            }
            else{ (getNavigator() as CountryNavigator).showFeedbackMessage(getContext().getString(R.string.warning_no_internet))}



        }
    }

    private fun getCountryData(countrydata: CountryModel) {

        countrydata.country.add(Country("Admin","","",false))
        val countrydatasf = getApplicationSharePref().getCountry()
        if (countrydatasf.isNullOrEmpty().not()){
            val countrymodel : CountryModel = Gson().fromJson(countrydatasf, CountryModel::class.java)
            val selected = countrymodel.country.find { it.isSelected }
            selected?.let { selectedcontry->
                countrydata.country.forEach {item ->
                    if (item.name==selectedcontry.name){
                        item.isSelected=true
                    }
                }
//                val sortCounty =  countrydata.country.sortedBy { it.name }
//                countrydata.country.clear()
//                countrydata.country.addAll(sortCounty)

                val json: String? = Gson().toJson(countrydata)
                getApplicationSharePref().setCountry(json)
                (getNavigator() as CountryNavigator).onCountrys(countrydata)
            }

        }else{
//            val sortCounty =  countrydata.country.sortedBy { it.name }
//            countrydata.country.clear()
//            countrydata.country.addAll(sortCounty)
            val json: String? = Gson().toJson(countrydata)
            getApplicationSharePref().setCountry(json)
            (getNavigator() as CountryNavigator).onCountrys(countrydata)
        }
    }

    fun saveSelected(get: Country, countrydata: ArrayList<Country>) {
        val countrymodel = CountryModel()
        val sortCounty =  countrydata.sortedBy { it.name }
        countrydata.clear()
        countrydata.addAll(sortCounty)
        countrymodel.country = countrydata
        getApplicationSharePref().setCountry(Gson().toJson(countrymodel))
        getApplicationSharePref().setSelectCountry(Gson().toJson(get))
        getLocation(get.link)

    }

    fun saveSelectedLocation(region: RegionList) {
        val locationname = region.LocList.sortedBy { it.LocName }
        region.LocList.clear()
        region.LocList.addAll(locationname)
        val jsonregion: String? = Gson().toJson(region)
        getApplicationSharePref().setLocalRestrurent(jsonregion)
        (getNavigator() as CountryNavigator).onSaveLocation()
    }

    private fun getLocation(location :String){

        if (location.isEmpty()){
            val response : LocationModel = Gson().fromJson(
                BuilderManager.decodeString(ConstantCommand.TEST_LOCATION),
                LocationModel::class.java)
            val sortedRegin = response.RegionList.sortedBy { it.RegionName }
            response.RegionList.clear()
            response.RegionList.addAll(sortedRegin)
            (getNavigator() as CountryNavigator).updateLocation(response)

        }else {
            if (getApplicationUtility().isInternetConnected()) {
                showProgress()
                getCompositeDisposable().add(
                    getApplicationRequest().location(location).doOnSuccess { }
                        .subscribeOn(getScheduleProvider().io())
                        .observeOn(getScheduleProvider().ui())
                        .subscribe({ response: LocationModel ->
                            hideProgress()
                            val sortedRegin = response.RegionList.sortedBy { it.RegionName }
                            response.RegionList.clear()
                            response.RegionList.addAll(sortedRegin)

                            (getNavigator() as CountryNavigator).updateLocation(response)
                        },
                            { throwable: Throwable? ->
                                hideProgress()
                                throwable?.message?.let {
                                    (getNavigator() as CountryNavigator).showFeedbackMessage(
                                        it
                                    )
                                }
                            }))
            } else {
                (getNavigator() as CountryNavigator).showFeedbackMessage(getContext().getString(R.string.warning_no_internet))
            }
        }
    }



}