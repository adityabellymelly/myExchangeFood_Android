package com.exchange.user.module.choose_order_module

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.exchange.user.R
import com.exchange.user.databinding.AcvitivityChooseBinding
import com.exchange.user.databinding.AcvitivityDiscoverBinding
import com.exchange.user.module.base_module.BaseActivity
import com.exchange.user.module.base_module.ConstantCommand
import com.exchange.user.module.base_module.base_view.ViewModelProviderFactory
import com.exchange.user.module.country_module.CountryActivity
import javax.inject.Inject

class ChooseOrderActivity : BaseActivity<AcvitivityChooseBinding, ChooseOrderViewModel>(),ChooseOrderNavigator {
    @Inject
    lateinit var factory : ViewModelProviderFactory
    @Inject
    lateinit var context: Context
    lateinit var chooseOrderViewModel : ChooseOrderViewModel
    lateinit var chooseorderbinding: AcvitivityChooseBinding

    override fun getLayoutId(): Int {
        return R.layout.acvitivity_choose
    }

    override fun getViewModel(): ChooseOrderViewModel {
        chooseOrderViewModel = ViewModelProvider(this,factory).get(ChooseOrderViewModel::class.java)
        return chooseOrderViewModel
    }

    override fun getBindingVariable(): Int {
        return androidx.databinding.library.baseAdapters.BR.chooseorderViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        chooseorderbinding = getViewDataBinding()
        chooseOrderViewModel.setNavigator(this)
        initData()
    }

    override fun openCountryActivity() {
//        if(chooseorderbinding.checkTermsandcondition.isChecked){
//
//        }else{
//            showBaseToast(chooseorderbinding.root,"Should agree with terms and conditions.")
//        }

        startActivity(Intent(applicationContext,CountryActivity:: class.java)
            .putExtra(ConstantCommand.DATA, ConstantCommand.FROM_CHOOSE))
        finish()
    }

    override fun openTermsCondition() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(ConstantCommand.TERMS_AND_CONDITIONS)
        startActivity(intent)
    }

    override fun openPolicy() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(ConstantCommand.PRIVACY_POLICY)
        startActivity(intent)
    }


    override fun initData() {

    }

    override fun onBack() {
        onBackPressed()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}