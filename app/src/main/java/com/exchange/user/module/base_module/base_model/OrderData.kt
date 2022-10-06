package com.exchange.user.module.base_module.base_model

import com.exchange.user.module.cart_module.model.card_model.CartData
import com.google.gson.annotations.SerializedName

class OrderData {
    @SerializedName("orderData")
    private var mOrderData: CartData? = null

    fun getmOrderData(): CartData? {
        return mOrderData
    }

    fun setmOrderData(mOrderData: CartData) {
        this.mOrderData = mOrderData
    }
}