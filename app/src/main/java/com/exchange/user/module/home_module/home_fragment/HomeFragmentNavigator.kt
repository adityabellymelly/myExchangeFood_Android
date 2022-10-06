package com.exchange.user.module.home_module.home_fragment

import com.exchange.user.module.base_module.base_view.CommonActivityOrFragmentView
import com.exchange.user.module.country_module.model.location_model.LocList

interface HomeFragmentNavigator : CommonActivityOrFragmentView {

    fun openCountryScreen()
    fun updateRestrauent(locList: List<LocList>)
}