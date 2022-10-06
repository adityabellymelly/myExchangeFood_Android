package com.exchange.user.module.cart_module.model.action

interface CartUpdate {
    fun onIncresses()
    fun onDecresses()
    fun remove(adapterPosition: Int)
}