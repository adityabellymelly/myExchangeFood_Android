package com.exchange.user.module.home_module

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.exchange.user.R
import com.exchange.user.databinding.ActivityHomeBinding
import com.exchange.user.module.base_module.BaseActivity
import com.exchange.user.module.base_module.ConstantCommand
import com.exchange.user.module.base_module.base_view.ViewModelProviderFactory
import com.exchange.user.module.cart_module.CartFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class HomeActivity : BaseActivity<ActivityHomeBinding, HomeViewModel>(),HomeNavigator,
    HasSupportFragmentInjector {
    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>
    @Inject
    lateinit var factory: ViewModelProviderFactory
    private lateinit var homeviewModel: HomeViewModel
    private lateinit var homeactivityBinding : ActivityHomeBinding
    private lateinit var appUpdateManager: AppUpdateManager
    companion object{
        lateinit var bottomNavigation: BottomNavigationView
        lateinit var  homeActivity : HomeActivity
        fun selectTab(resource : Int){
            if (::bottomNavigation.isInitialized){
                bottomNavigation.selectedItemId = resource
            }
        }
        fun navBehaviour(boolean: Boolean){
            if (boolean && ::bottomNavigation.isInitialized){
                bottomNavigation.isVerticalScrollBarEnabled
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        CartFragment.cartFragment.onActivityResult(requestCode,resultCode,data)
        if (requestCode == ConstantCommand.MY_REQUEST_CODE){
            if (resultCode != RESULT_OK){
                showFeedbackMessage("Download canceled.")
            }
        }
    }
    override fun getLayoutId(): Int {
        return R.layout.activity__home
    }
    override fun getViewModel(): HomeViewModel {
        homeviewModel =  ViewModelProvider(this, factory).get(HomeViewModel::class.java)
        return homeviewModel
    }
    override fun getBindingVariable(): Int {
        return androidx.databinding.library.baseAdapters.BR.homeviewModel
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeActivity = this
        homeactivityBinding = getViewDataBinding()
        bottomNavigation = homeactivityBinding.bottomNav
        homeviewModel.setNavigator(this)
        appUpdateManager =  AppUpdateManagerFactory.create(this)
        appUpdateManager.registerListener(listener)
        initData()
    }
    override fun initData() {
        homeactivityBinding.bottomNav.setOnItemSelectedListener { item ->
            homeviewModel.onNavigationItemSelected(item)
            true
        }
        selectTab(R.id.home)
        homeviewModel.setSavedData()
        homeviewModel.saveFcmToken()


    }
    override fun onStart() {
        super.onStart()
        checkUpdate()
    }
    override fun onChangeFragmant(fragment: Fragment) {
        loadFragment(fragment)
    }
    override fun showFeedbackMessage(message: String) {

    }


    private val listener: InstallStateUpdatedListener = InstallStateUpdatedListener {
            installState ->
        if (installState.installStatus() == InstallStatus.DOWNLOADED) {
            // After the update is downloaded, show a notification
            // and request user confirmation to restart the app.
            Log.d("TAG", "An update has been downloaded")
            showFeedbackMessage("An update has been downloaded")
            appUpdateManager.completeUpdate()
        }
    }


    private fun loadFragment(fragment: Fragment) {
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()

        if(fragment.isAdded){
            fragmentTransaction.remove(fragment).commit()
        }

        fragmentTransaction.replace(R.id.fragmentsContainerFL, fragment)
        fragmentTransaction.commitAllowingStateLoss()

    }

//    fun loadFragmentWitBackStak(fragment: Fragment) {
//        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
//        if(fragment.isAdded){
//            fragmentTransaction.remove(fragment).commit()
//        }
//        fragmentTransaction.replace(R.id.fragmentsContainerFL, fragment).addToBackStack(null)
//        fragmentTransaction.commitAllowingStateLoss()
//    }



    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentDispatchingAndroidInjector
    }


    private fun checkUpdate() {
        // Returns an intent object that you use to check for an update.
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        // Checks that the platform will allow the specified type of update.
        Log.d("TAG", "Checking for updates")
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                // Request the update.
                appUpdateManager.startUpdateFlowForResult(
                    // Pass the intent that is returned by 'getAppUpdateInfo()'.
                    appUpdateInfo,
                    // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                    AppUpdateType.FLEXIBLE,
                    // The current activity making the update request.
                    this,
                    // Include a request code to later monitor this update request.
                    ConstantCommand.MY_REQUEST_CODE)
            }
            else {
                Log.d("TAG", "No Update available")
            }
        }
    }

}