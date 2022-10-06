package com.exchange.user.module.coupon_module

import com.exchange.user.module.base_module.base_view.CommonActivityOrFragmentView
import com.exchange.user.module.restaurent_module.model.data_model.reastaurent_mode.Coupon

interface AppliedCoupenNavigation :CommonActivityOrFragmentView {
    fun onBacPress()
    fun searchCoupon()
    fun message(s: String)
    fun appliyPromo(coupon: Coupon)
}