package com.exchange.user.module.splesh_module

import android.content.Context
import android.os.Bundle
import android.util.Base64
import android.util.Log
import com.exchange.user.module.base_module.base_view.BaseViewModel
import com.exchange.user.module.utility_module.ApplicationSharePrefrence
import com.exchange.user.module.utility_module.ApplicationUtility
import com.exchange.user.module.utility_module.Validations
import com.exchange.user.module.web_module.ApplicationRequest
import com.exchange.user.rx_module.SchedulerProvider

class SpleshViewModel (applicationprefrence: ApplicationSharePrefrence, schedulerProvider : SchedulerProvider,
                       applicationutility : ApplicationUtility,
                       applicationcontext : Context,
                       validations  : Validations,
                       applicationrequest : ApplicationRequest
): BaseViewModel(applicationprefrence,schedulerProvider, applicationutility, applicationcontext , validations , applicationrequest){

    fun next(){
        (getNavigator() as SplashNavigator).openDiscoverActivity()
    }


    fun startSeeding(extras: Bundle?) {
        hashFromSHA1("D7:86:A3:54:8E:85:B5:C8:6F:87:9C:B9:99:E7:0A:74:6B:A8:F2:34")
        if (extras!=null){
            if (!extras.getString("alertType").isNullOrEmpty()) {
                (getNavigator() as SplashNavigator).openOrderHistory()
            }else{
                movenext()
            }
        }else{
            movenext()
        }

    }
    private fun movenext() {
        if(getApplicationSharePref().getfirst().equals("first")){
            (getNavigator() as SplashNavigator).openHomeActivity()
        }else{
            getApplicationSharePref().setFirst("first")
        }
    }

    fun hashFromSHA1(sha1: String) {
        val arr = sha1.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val byteArr = ByteArray(arr.size)

        for (i in arr.indices) {
            byteArr[i] = Integer.decode("0x" + arr[i]).toByte()
        }

        Log.e("hash : ", Base64.encodeToString(byteArr, Base64.NO_WRAP))
    }

}