package com.exchange.user.module.base_module.base_command

import com.exchange.user.module.base_module.ValidCoupon
import com.exchange.user.module.base_module.base_model.OrderDataModel
import com.exchange.user.module.cart_module.model.card_model.CartData
import com.exchange.user.module.cart_module.model.suggestiondata.Suggestion
import com.exchange.user.module.profile_module.model.ProfileModel
import com.exchange.user.module.restaurent_module.model.data_model.reastaurent_mode.RestrurentModel
import com.exchange.user.module.restaurent_module.model.menu_model.MenuModel

interface CartDataInt {
    companion object {
        var orderdata = OrderDataModel()
        var cartdata = CartData()
        var restrurent = RestrurentModel()
        var suggestion = Suggestion()
        var menudata = MenuModel()
        var profiledata =  ProfileModel()
        var validcoupon = ValidCoupon()
    }
}