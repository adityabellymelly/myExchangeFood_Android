package com.exchange.user.module.cart_module.model.action

import com.exchange.user.module.cart_module.model.card_model.ItemList


interface AddItemsToCart {
    fun onAddItemToCart(items: ItemList)
    fun onAddItemToCartPopUp(items:ItemList)
    fun onsetItemToCart(items:ItemList)
    fun onRemoveItemToCart(items:ItemList)
    fun openImage(img: String?, name: String?)
}