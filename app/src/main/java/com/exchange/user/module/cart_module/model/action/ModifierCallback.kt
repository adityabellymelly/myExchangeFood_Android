package com.exchange.user.module.cart_module.model.action

import com.exchange.user.module.cart_module.model.card_model.AddOnOptionModifier

interface ModifierCallback {
    fun onAddModufier(addOnOptionModifier: AddOnOptionModifier)
}