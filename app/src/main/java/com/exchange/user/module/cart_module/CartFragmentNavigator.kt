package com.exchange.user.module.cart_module

import com.exchange.user.module.base_module.base_view.CommonActivityOrFragmentView

interface CartFragmentNavigator : CommonActivityOrFragmentView {
    fun onReasturentMenu()
    fun applyCoupon()
    fun tip(tip: Int)
    fun cancelCoupon()
    fun cancelDiscount()
    fun addOrChangeAddress()
    fun submit()
    fun showOrderError(toString: String)
    fun openConfirmationScreen()
    fun onScheduleTime()
    fun openPayment(openLink: String)
    fun startOrderProgress(boolean: Boolean)
}