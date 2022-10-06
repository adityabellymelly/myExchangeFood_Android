package com.exchange.user.module.edit_address_module

import com.exchange.user.module.edit_address_module.model.ChangeAddressModel
import com.exchange.user.module.base_module.base_view.CommonActivityOrFragmentView

interface EditAdressNavigator :  CommonActivityOrFragmentView {
    fun onBackPress()
    fun saveAddress()
    fun showError(error:Int,msg: String)
    fun onChangeAddress(changeaddress: ChangeAddressModel)
}