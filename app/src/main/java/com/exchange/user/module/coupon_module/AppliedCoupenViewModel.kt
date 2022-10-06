package com.exchange.user.module.coupon_module

import android.content.Context
import com.exchange.user.module.base_module.base_command.CartDataInt
import com.exchange.user.module.base_module.base_view.BaseViewModel
import com.exchange.user.module.utility_module.ApplicationSharePrefrence
import com.exchange.user.module.utility_module.ApplicationUtility
import com.exchange.user.module.utility_module.Validations
import com.exchange.user.module.web_module.ApplicationRequest
import com.exchange.user.rx_module.SchedulerProvider

class AppliedCoupenViewModel (
    applicationprefrence: ApplicationSharePrefrence, schedulerProvider: SchedulerProvider,
    applicationutility: ApplicationUtility,
    applicationcontext: Context,
    validations: Validations,
    applicationrequest: ApplicationRequest
) : BaseViewModel(
    applicationprefrence,
    schedulerProvider,
    applicationutility,
    applicationcontext,
    validations,
    applicationrequest
){

    fun onBack(){
        (getNavigator() as AppliedCoupenNavigation).onBacPress()
    }

    fun searchCoupon(){
        (getNavigator() as AppliedCoupenNavigation).searchCoupon()
    }

    fun findPromo(promo: String) {
        val coupon= CartDataInt.restrurent.coupons.find { it.couponCode?.trim().equals(promo.trim(),true) || it.couponCode.equals("Generic")  }
        if (coupon!=null){
            if (CartDataInt.profiledata.coupons.any { it.couponCode.equals(coupon.couponCode)}){
                (getNavigator() as AppliedCoupenNavigation).appliyPromo(coupon)
            }else{
                (getNavigator() as AppliedCoupenNavigation).message("Coupon code $promo is not available.")

            }
        }else{
            (getNavigator() as AppliedCoupenNavigation).message("Coupon code $promo is not available.")
        }
    }
}