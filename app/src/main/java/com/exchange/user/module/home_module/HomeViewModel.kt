package com.exchange.user.module.home_module

import android.content.Context
import android.view.MenuItem
import com.exchange.user.R
import com.exchange.user.module.about_us_module.AboutUsFragment
import com.exchange.user.module.base_module.ValidCoupon
import com.exchange.user.module.base_module.base_command.CartDataInt
import com.exchange.user.module.base_module.base_view.BaseViewModel
import com.exchange.user.module.cart_module.CartFragment
import com.exchange.user.module.cart_module.model.card_model.CartData
import com.exchange.user.module.home_module.home_fragment.HomeFragment
import com.exchange.user.module.order_history.order_history_fragment.OrderHistoryFragment
import com.exchange.user.module.profile_module.ProfileFragment
import com.exchange.user.module.profile_module.model.ProfileModel
import com.exchange.user.module.restaurent_module.model.data_model.reastaurent_mode.RestrurentModel
import com.exchange.user.module.restaurent_module.model.menu_model.MenuModel
import com.exchange.user.module.utility_module.ApplicationSharePrefrence
import com.exchange.user.module.utility_module.ApplicationUtility
import com.exchange.user.module.utility_module.Validations
import com.exchange.user.module.web_module.ApplicationRequest
import com.exchange.user.rx_module.SchedulerProvider
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson

class HomeViewModel (applicationprefrence: ApplicationSharePrefrence, schedulerProvider : SchedulerProvider,
                     applicationutility : ApplicationUtility,
                     applicationcontext : Context,
                     validations  : Validations,
                     applicationrequest : ApplicationRequest
) : BaseViewModel(applicationprefrence,schedulerProvider, applicationutility, applicationcontext , validations , applicationrequest){


    fun onNavigationItemSelected(it: MenuItem): Boolean {
        when (it.itemId) {
            R.id.home ->{(getNavigator() as HomeNavigator).onChangeFragmant(HomeFragment())}
            R.id.mycart -> {(getNavigator() as HomeNavigator).onChangeFragmant(CartFragment())}
            R.id.profile -> {(getNavigator() as HomeNavigator).onChangeFragmant(ProfileFragment())}
            R.id.orderhistory -> {(getNavigator() as HomeNavigator).onChangeFragmant(
                OrderHistoryFragment()
            )}
            R.id.aboutus -> {(getNavigator() as HomeNavigator).onChangeFragmant(AboutUsFragment())}

        }

        return true
    }

    fun setSavedData(){
        getProfileData()
        getCartData()
        getRestaurant()
        getValidCoupon()
        getMenuData()
    }
    private fun getMenuData() {
        getApplicationSharePref().getMenuData()?.let {
            val menudata: MenuModel = Gson().fromJson(it, MenuModel::class.java)
            CartDataInt.menudata = menudata
        }

    }
    private fun getProfileData() {
        getApplicationSharePref().getProfile()?.let {
            val profileData: ProfileModel = Gson().fromJson(it, ProfileModel::class.java)
            CartDataInt.profiledata = profileData
        }
    }

    private fun getCartData(){
        getApplicationSharePref().getCart()?.let {
            val cartData: CartData = Gson().fromJson(it, CartData::class.java)
            CartDataInt.cartdata = cartData
        }
    }

    private fun getRestaurant(){
        getApplicationSharePref().getResturent()?.let {
            val restaurant: RestrurentModel = Gson().fromJson(it,RestrurentModel::class.java)
            CartDataInt.restrurent = restaurant
        }
    }

    private fun getValidCoupon(){
        getApplicationSharePref().getValidCoupon()?.let {
            val validCoupon : ValidCoupon = Gson().fromJson(it, ValidCoupon::class.java)
            CartDataInt.validcoupon = validCoupon
        }
    }
    fun saveFcmToken() {
        if (getApplicationUtility().isInternetConnected()) {
            try {
                FirebaseMessaging.getInstance().token.addOnCompleteListener { task->
                    if (!task.isSuccessful) {
                        return@addOnCompleteListener
                    }
                    getApplicationSharePref().setFCMToken(task.result?:"")
                }
            }catch (e:Exception){e.printStackTrace()}

        }
    }

}