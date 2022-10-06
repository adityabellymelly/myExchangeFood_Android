package com.exchange.user.module.edit_address_module

import android.os.Bundle
import android.widget.RadioButton
import androidx.lifecycle.ViewModelProvider
import com.exchange.user.R
import com.exchange.user.databinding.ActivityEditAddressBinding
import com.exchange.user.module.base_module.BaseActivity
import com.exchange.user.module.base_module.ConstantCommand
import com.exchange.user.module.base_module.base_view.ViewModelProviderFactory
import com.exchange.user.module.cart_module.model.card_model.DeliveryInfo
import com.exchange.user.module.edit_address_module.model.ChangeAddressModel
import com.google.gson.Gson
import javax.inject.Inject

class EditAddressActivity : BaseActivity<ActivityEditAddressBinding,EditAdressViewModel>(),EditAdressNavigator {
    @Inject
    lateinit var factory: ViewModelProviderFactory
    private lateinit var editAdressviewModel: EditAdressViewModel
    private lateinit var activityeditaddressBinding : ActivityEditAddressBinding
    private var addressId: Long = 0

    override fun getLayoutId(): Int {
        return R.layout.activity_edit_address
    }
    override fun getViewModel(): EditAdressViewModel {
        editAdressviewModel = ViewModelProvider(this, factory).get(EditAdressViewModel::class.java)
        return editAdressviewModel
    }
    override fun getBindingVariable(): Int {
        return androidx.databinding.library.baseAdapters.BR.editadressViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityeditaddressBinding = getViewDataBinding()
        editAdressviewModel.setNavigator(this)
        editAdressviewModel.initProgress(this)
        initData()
        activityeditaddressBinding.titeltext
    }

    override fun initData() {
        if (intent.extras !=null){
            activityeditaddressBinding.titeltext.text = getString(R.string.editAdress)
            val  data = Gson().fromJson(intent.getStringExtra(ConstantCommand.DATA), DeliveryInfo::class.java)
            data?.let {
                activityeditaddressBinding.firstname.setText(it.fName)
                activityeditaddressBinding.lastname.setText(it.lName)
                activityeditaddressBinding.mobileno.setText(it.telephone)
                activityeditaddressBinding.location.setText(it.city)
                activityeditaddressBinding.houseandareya.setText(it.addr2)
                activityeditaddressBinding.instructions.setText(it.instructions)
                val  splitValue = it.addr1?.split(",")
                if (splitValue.isNullOrEmpty().not()){
                     val building = splitValue?.get(0)?.split("Bldg No.")
                     if (building.isNullOrEmpty().not() && building?.size!! >1){
                         activityeditaddressBinding.buldingno.setText(building.get(1))
                     }
                     if (splitValue?.size!! >1){
                         val roomno  = splitValue[1].split("Room No.")
                         if (roomno.isNullOrEmpty().not() && roomno.size >1){
                             activityeditaddressBinding.roomno.setText(roomno.get(1))
                         }

                     }
                 }
                data.addressId?.let { addressId =it }
            }
        }
        else{
            activityeditaddressBinding.titeltext.text = getString(R.string.add_address)
            activityeditaddressBinding.firstname.setText(editAdressviewModel.getfirstname())
            activityeditaddressBinding.lastname.setText(editAdressviewModel.getlastname())
            activityeditaddressBinding.mobileno.setText(editAdressviewModel.getPhoneNumber())
            activityeditaddressBinding.location.setText(editAdressviewModel.getLocation())
        }

    }

    override fun showError(error: Int, msg: String)
    {
        if (error==ConstantCommand.LOCATION_ERROR){
            activityeditaddressBinding.locationtaxture.error = msg
        }else if (error==ConstantCommand.M0BILE_ERROR){
            activityeditaddressBinding.locationtaxture.error = ""
            activityeditaddressBinding.mobiletaxture.error = msg
        }else{
            activityeditaddressBinding.locationtaxture.error = ""
            activityeditaddressBinding.mobiletaxture.error = ""
        }
    }

    override fun onChangeAddress(changeaddress: ChangeAddressModel) {
        finish()
    }

    override fun saveAddress() {
        editAdressviewModel.saveaddress(activityeditaddressBinding.firstname.text.toString(),
            activityeditaddressBinding.lastname.text.toString(),
            activityeditaddressBinding.mobileno.text.toString(),
            activityeditaddressBinding.location.text.toString(),
            activityeditaddressBinding.houseandareya.text.toString(),
            activityeditaddressBinding.instructions.text.toString(),
            activityeditaddressBinding.buldingno.text.toString(),
            activityeditaddressBinding.roomno.text.toString(),
            addressId,
            findViewById<RadioButton>(activityeditaddressBinding.radiogrop.checkedRadioButtonId).text.toString())
    }

    override fun showFeedbackMessage(message: String) {
        showBaseToast(activityeditaddressBinding.root,message)
    }

    override fun onBackPress() {
        onBackPressed()
    }
}