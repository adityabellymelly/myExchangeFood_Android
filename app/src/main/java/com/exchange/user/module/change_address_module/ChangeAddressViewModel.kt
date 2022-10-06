package com.exchange.user.module.change_address_module

import android.content.Context
import com.exchange.user.module.base_module.base_command.CartDataInt
import com.exchange.user.module.base_module.base_view.BaseViewModel
import com.exchange.user.module.cart_module.model.card_model.DeliveryInfo
import com.exchange.user.module.profile_module.model.AddressBook
import com.exchange.user.module.utility_module.ApplicationSharePrefrence
import com.exchange.user.module.utility_module.ApplicationUtility
import com.exchange.user.module.utility_module.Validations
import com.exchange.user.module.web_module.ApplicationRequest
import com.exchange.user.rx_module.SchedulerProvider
import com.google.gson.Gson

class ChangeAddressViewModel(applicationprefrence: ApplicationSharePrefrence, schedulerProvider : SchedulerProvider,
                             applicationutility : ApplicationUtility,
                             applicationcontext : Context,
                             validations  : Validations,
                             applicationrequest : ApplicationRequest
) : BaseViewModel(applicationprefrence,schedulerProvider, applicationutility, applicationcontext , validations , applicationrequest) {


    fun addNewAddrss(){
        (getNavigator() as ChangeAddressNavigator).addnewAddress()
    }

    fun back(){
        (getNavigator() as ChangeAddressNavigator).onBack()
    }

    fun saveAddress(get: AddressBook) {
        val deliveryinfo = DeliveryInfo()
        deliveryinfo.addressId = get.custAddressBookId
        deliveryinfo.addr1 = get.addr1
        deliveryinfo.addr2 = get.addr2
        deliveryinfo.name = get.fName
        deliveryinfo.telephone = get.telephone
        deliveryinfo.instructions = get.instructions
        deliveryinfo.city = get.city
        deliveryinfo.state = get.state
        deliveryinfo.zip = get.zip
        deliveryinfo.latitude = get.latitude
        deliveryinfo.longitude = get.longitude
        deliveryinfo.custAddressName = get.custAddressName
        CartDataInt.cartdata.deliveryInfo = deliveryinfo
        getApplicationSharePref().setAddress(Gson().toJson(deliveryinfo))
    }

}