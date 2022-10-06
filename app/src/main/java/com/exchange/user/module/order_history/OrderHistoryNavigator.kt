package com.exchange.user.module.order_history

import com.exchange.user.module.base_module.base_view.CommonActivityOrFragmentView
import com.exchange.user.module.profile_module.model.ProfileModel

interface OrderHistoryNavigator :CommonActivityOrFragmentView {

    fun onBack()
    fun updateHistory(profiledata: ProfileModel)
    fun openSiginActivity()
}