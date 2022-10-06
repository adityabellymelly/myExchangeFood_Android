package com.exchange.user.module.cart_module.model.action

import com.exchange.user.module.cart_module.model.card_model.ItemsAddonList

interface OnAddOnOption {
    fun onAddOnOption(addonlist: ItemsAddonList)
    fun onAddRemoveOption(addonlist: ItemsAddonList)
}