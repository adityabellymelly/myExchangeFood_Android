package com.exchange.user.module.country_module

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Window
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.exchange.user.R
import com.exchange.user.databinding.ActivityCountryBinding
import com.exchange.user.databinding.DialogAlertSecurityBinding
import com.exchange.user.module.base_module.BaseActivity
import com.exchange.user.module.base_module.ConstantCommand
import com.exchange.user.module.base_module.base_view.ViewModelProviderFactory
import com.exchange.user.module.country_module.adapter.CountyAdapter
import com.exchange.user.module.country_module.model.Country
import com.exchange.user.module.country_module.model.CountryModel
import com.exchange.user.module.country_module.model.location_model.LocationModel
import com.exchange.user.module.country_module.model.location_model.RegionList
import com.exchange.user.module.home_module.HomeActivity
import com.exchange.user.module.utility_module.BuilderManager
import com.exchange.user.module.utility_module.dialog_commands.LocationDailogListner
import javax.inject.Inject

class CountryActivity : BaseActivity<ActivityCountryBinding, CountryViewModel>(), CountryNavigator,
    CountyAdapter.CountryChooice, LocationDailogListner {
    @Inject
    lateinit var factory: ViewModelProviderFactory
    private lateinit var countryviewModel: CountryViewModel
    private lateinit var activitycountryBinding : ActivityCountryBinding
    @Inject
    lateinit var countyAdapter: CountyAdapter
    lateinit var from: String

    override fun getLayoutId(): Int {
        return R.layout.activity_country
    }
    override fun getViewModel(): CountryViewModel {
        countryviewModel = ViewModelProvider(this, factory).get(CountryViewModel::class.java)
        return countryviewModel
    }
    override fun getBindingVariable(): Int {
        return androidx.databinding.library.baseAdapters.BR.countryViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitycountryBinding = getViewDataBinding()
        countryviewModel.setNavigator(this)
        countryviewModel.initProgress(this)
        countyAdapter.setListner(this)
        initData()
    }
    override fun initData() {
        intent.extras?.let {
            from = it.getString(ConstantCommand.DATA).toString()
        }
        activitycountryBinding.conuntyrec.adapter = countyAdapter
        countryviewModel.getCountry()
    }

    override fun onCountrys(countrymodel: CountryModel) {
        countyAdapter.addItems(countrymodel.country)
    }

    override fun onCountryChooes(get: Country, countrydata: ArrayList<Country>) {
        if (get.name == "Admin"){
          openSecurityProcess(get,countrydata)
        }else{
            countryviewModel.saveSelected(get,countrydata)
        }
    }

    private fun openSecurityProcess(get: Country, countrydata: ArrayList<Country>) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogAlertBinding: DialogAlertSecurityBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this),
            R.layout.dialog_alert_security,
            null,
            false
        )
        dialog.setCancelable(true)
        dialog.setContentView(dialogAlertBinding.root)
        dialogAlertBinding.ok.setOnClickListener {
            if (dialogAlertBinding.editPassword.text.toString() == "75236"){
                countryviewModel.saveSelected(get,countrydata)
                dialog.dismiss()
            }else{

                dialogAlertBinding.mesage.text = "Wrong PIN Entered!"
            }
        }
        dialogAlertBinding.cancel.setOnClickListener { dialog.dismiss() }

        dialogAlertBinding.editPassword.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                dialogAlertBinding.mesage.text = ""
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun afterTextChanged(p0: Editable?) {

            }

        })
        dialog.window?.let {
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.attributes.gravity = Gravity.NO_GRAVITY
        }

        if (dialog.isShowing) {
            dialog.dismiss()
        }
        try {
            dialog.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun updateLocation(responce: LocationModel) {
        if (responce.RegionList.isNullOrEmpty().not()){
            BuilderManager.chooesLocation(this@CountryActivity,responce.RegionList,this)
        }else{
            showFeedbackMessage("${responce.CountryName} haven't location yet.")
        }
    }

    override fun onSelectLocation(region: RegionList) {
        countryviewModel.saveSelectedLocation(region)
    }

    override fun onCheck() {

    }
    override fun onSaveLocation() {
        if (from == ConstantCommand.FROM_CHOOSE){
            startActivity(Intent(this@CountryActivity, HomeActivity::class.java))
        }
        finish()
    }
    override fun showFeedbackMessage(message: String) {
        showBaseToast(activitycountryBinding.root, message)
    }
    override fun onBack() {
        onBackPressed()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


}