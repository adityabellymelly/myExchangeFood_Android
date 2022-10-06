package com.exchange.user.module.change_address_module

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.exchange.user.BR
import com.exchange.user.R
import com.exchange.user.databinding.ActivityChangeAddressBinding
import com.exchange.user.module.base_module.BaseActivity
import com.exchange.user.module.base_module.ConstantCommand
import com.exchange.user.module.base_module.base_command.CartDataInt
import com.exchange.user.module.base_module.base_view.ViewModelProviderFactory
import com.exchange.user.module.cart_module.model.card_model.DeliveryInfo
import com.exchange.user.module.change_address_module.adapter.AddressListAdapter
import com.exchange.user.module.edit_address_module.EditAddressActivity
import com.exchange.user.module.profile_module.model.AddressBook
import com.google.gson.GsonBuilder
import javax.inject.Inject

class ChangeAddressActivity : BaseActivity<ActivityChangeAddressBinding,ChangeAddressViewModel>(),ChangeAddressNavigator,
    AddressListAdapter.AddressAction {
    @Inject
    lateinit var factory: ViewModelProviderFactory
    private lateinit var changeaddressviewModel: ChangeAddressViewModel
    private lateinit var activitychangeadressBinding : ActivityChangeAddressBinding
    @Inject
    lateinit var addressListAdapter : AddressListAdapter

    override fun getLayoutId(): Int {
        return R.layout.activity_change_address
    }
    override fun getViewModel(): ChangeAddressViewModel {
        changeaddressviewModel = ViewModelProvider(this, factory).get(ChangeAddressViewModel::class.java)
        return changeaddressviewModel
    }
    override fun getBindingVariable(): Int {
        return BR.changeaddressViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitychangeadressBinding = getViewDataBinding()
        changeaddressviewModel.setNavigator(this)
        addressListAdapter.setListner(this)
        activitychangeadressBinding.addressrecycle.adapter = addressListAdapter
        initData()
    }

    override fun onStart() {
        super.onStart()
        initData()
    }

    override fun initData() {
        if (CartDataInt.profiledata.addressBook.isNullOrEmpty().not()){
            activitychangeadressBinding.message.visibility =  View.GONE
            activitychangeadressBinding.addressrecycle.visibility =  View.VISIBLE
            CartDataInt.profiledata.addressBook?.let {addressListAdapter.addItems(it)}
        }else{
            activitychangeadressBinding.message.visibility =  View.VISIBLE
            activitychangeadressBinding.addressrecycle.visibility =  View.GONE
        }

    }

    override fun addnewAddress() {
       startActivity(Intent(this, EditAddressActivity::class.java))
    }

    override fun onBack() {
        onBackPressed()
    }

    override fun showFeedbackMessage(message: String) {
        showBaseToast(activitychangeadressBinding.root,message)
    }
    override fun onEditAddress(get: AddressBook, adapterPosition: Int) {
        val deliveryinfo = DeliveryInfo()
        deliveryinfo.addressId = get.custAddressBookId
        deliveryinfo.addr1 = get.addr1
        deliveryinfo.addr2 = get.addr2
        deliveryinfo.name = get.fName
        deliveryinfo.fName = get.fName
        deliveryinfo.lName = get.lName
        deliveryinfo.mName = get.mName
        deliveryinfo.telephone = get.telephone
        deliveryinfo.instructions = get.instructions
        deliveryinfo.city = get.city
        deliveryinfo.state = get.state
        deliveryinfo.zip = get.zip
        deliveryinfo.latitude = get.latitude
        deliveryinfo.longitude = get.longitude
        startActivity(Intent(applicationContext, EditAddressActivity::class.java)
            .putExtra(ConstantCommand.DATA, GsonBuilder().setPrettyPrinting().create().toJson(deliveryinfo))
        )
    }


    override fun onSelectAddress(get: AddressBook) {
        changeaddressviewModel.saveAddress(get)
        finish()
    }

}