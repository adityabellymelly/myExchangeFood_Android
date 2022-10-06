package com.exchange.user.module.discover_module

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.exchange.user.R
import com.exchange.user.databinding.AcvitivityDiscoverBinding
import com.exchange.user.module.base_module.BaseActivity
import com.exchange.user.module.base_module.base_view.ViewModelProviderFactory
import com.exchange.user.module.choose_order_module.ChooseOrderActivity
import javax.inject.Inject

class DiscoverActivity: BaseActivity<AcvitivityDiscoverBinding,DiscoverViewModel>(),DiscoverNavigator {
    @Inject
    lateinit var factory : ViewModelProviderFactory

    @Inject
    lateinit var context: Context
    private lateinit var discoverViewModel : DiscoverViewModel
    private lateinit var discoverbinding: AcvitivityDiscoverBinding

    override fun getLayoutId(): Int {
        return R.layout.acvitivity_discover
    }

    override fun getViewModel(): DiscoverViewModel {
        discoverViewModel = ViewModelProvider(this,factory).get(DiscoverViewModel::class.java)
        return discoverViewModel
    }

    override fun getBindingVariable(): Int {
        return androidx.databinding.library.baseAdapters.BR.discoverViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        discoverbinding = getViewDataBinding()
        discoverViewModel.setNavigator(this)
        initData()
    }

    override fun openChooseOrderActivity() {
        startActivity(Intent(this@DiscoverActivity, ChooseOrderActivity::class.java))
        finish()
    }


    override fun onBack() {

    }

    override fun initData() {

    }
}