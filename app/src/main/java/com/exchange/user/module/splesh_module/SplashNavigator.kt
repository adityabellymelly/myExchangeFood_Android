package com.exchange.user.module.splesh_module

import com.exchange.user.module.base_module.base_command.BasicNavigator

interface SplashNavigator : BasicNavigator {

    fun openDiscoverActivity()
    fun openHomeActivity()
    fun openOrderHistory()
}