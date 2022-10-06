package com.exchange.user.module.change_address_module

import com.exchange.user.module.base_module.base_view.CommonActivityOrFragmentView

interface ChangeAddressNavigator : CommonActivityOrFragmentView {
    fun onBack()
    fun addnewAddress()
}