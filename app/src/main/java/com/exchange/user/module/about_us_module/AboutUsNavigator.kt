package com.exchange.user.module.about_us_module

import com.exchange.user.module.about_us_module.model.DeliveryProgramResponse
import com.exchange.user.module.base_module.base_view.CommonActivityOrFragmentView

interface AboutUsNavigator :CommonActivityOrFragmentView{

    fun openSiginActivity()
    fun updateAbout(response: DeliveryProgramResponse)
}