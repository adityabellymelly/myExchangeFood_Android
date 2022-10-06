package com.exchange.user.module.choose_order_module

import com.exchange.user.module.base_module.base_command.BasicNavigator

interface ChooseOrderNavigator: BasicNavigator {
    fun openCountryActivity()
    fun openPolicy()
    fun openTermsCondition()
}