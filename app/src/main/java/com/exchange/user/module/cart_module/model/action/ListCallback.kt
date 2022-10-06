package com.exchange.user.module.cart_module.model.action

import com.exchange.user.module.cart_module.model.card_model.AddOnOption


interface ListCallback {


    fun OnCheckAdd(list: AddOnOption)
    fun OnCheckRemove(list: AddOnOption)
}