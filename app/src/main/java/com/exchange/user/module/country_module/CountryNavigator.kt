package com.exchange.user.module.country_module

import com.exchange.user.module.base_module.base_view.CommonActivityOrFragmentView
import com.exchange.user.module.country_module.model.CountryModel
import com.exchange.user.module.country_module.model.location_model.LocationModel

interface CountryNavigator: CommonActivityOrFragmentView {
    fun onCountrys(countrymodel: CountryModel)
    fun updateLocation(responce: LocationModel)
    fun onSaveLocation()
    fun onBack()
    fun onCheck()
}