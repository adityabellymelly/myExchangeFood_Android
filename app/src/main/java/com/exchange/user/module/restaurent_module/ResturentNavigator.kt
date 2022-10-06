package com.exchange.user.module.restaurent_module

import com.exchange.user.module.base_module.base_view.CommonActivityOrFragmentView
import com.exchange.user.module.restaurent_module.model.data_model.reastaurent_mode.RestrurentModel
import com.exchange.user.module.restaurent_module.model.menu_model.MenuModel

interface ResturentNavigator :CommonActivityOrFragmentView {
    fun onLocation(resturentata: RestrurentModel)
    fun onUpdateMenu(menudata: MenuModel)
    fun onClosedSearch()
    fun onOpenSearch()
    fun onBack()
    fun moveToCart()
    fun showProgress(b: Boolean)
    fun oncartProgress(b:Boolean)
    fun onCoupon()
    fun moreAbout(b: Boolean)
    fun viewDiscount()
}