package com.exchange.user.module.splesh_module

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.exchange.user.R
import com.exchange.user.databinding.AcvitivitySplashBinding
import com.exchange.user.module.base_module.BaseActivity
import com.exchange.user.module.base_module.base_view.ViewModelProviderFactory
import com.exchange.user.module.discover_module.DiscoverActivity
import com.exchange.user.module.home_module.HomeActivity
import javax.inject.Inject

class SplashActivity: BaseActivity<AcvitivitySplashBinding, SpleshViewModel>(),SplashNavigator{
    @Inject
    lateinit var factory : ViewModelProviderFactory
    @Inject
    lateinit var context: Context
    private lateinit var mSplashViewModel : SpleshViewModel
    private lateinit var spleshbinding: AcvitivitySplashBinding

    override fun getLayoutId(): Int {
        return R.layout.acvitivity_splash
    }
    override fun getViewModel(): SpleshViewModel {
        mSplashViewModel = ViewModelProvider(this,factory).get(SpleshViewModel::class.java)
        return mSplashViewModel
    }

    override fun getBindingVariable(): Int {
        return androidx.databinding.library.baseAdapters.BR.spleshViewModel
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        spleshbinding = getViewDataBinding()
        initData()
        mSplashViewModel.setNavigator(this)
        mSplashViewModel.startSeeding(intent.extras)
    }
    override fun initData() {

    }

    override fun openDiscoverActivity() {
        startActivity(Intent(this, DiscoverActivity::class.java))
        finish()
    }

    override fun openHomeActivity() {
        val intent = Intent(this@SplashActivity,HomeActivity:: class.java)
        startActivity(intent)
        finish()
    }

    override fun openOrderHistory() {
//        startActivity(Intent(this@SpleshActivity,OrderHistoryActivity::class.java))
//        finish()
    }

    override fun onBack() {

    }

}