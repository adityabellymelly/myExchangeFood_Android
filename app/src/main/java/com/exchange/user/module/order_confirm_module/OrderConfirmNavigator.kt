package com.exchange.user.module.order_confirm_module

import com.exchange.user.module.base_module.base_view.CommonActivityOrFragmentView
import com.exchange.user.module.profile_module.model.OrderHistory

interface OrderConfirmNavigator :CommonActivityOrFragmentView{
fun onBacPress()
    fun callToReasturant()
    fun startNewOrder()
    fun onLastResonce(lastorderresponce: OrderHistory)
}