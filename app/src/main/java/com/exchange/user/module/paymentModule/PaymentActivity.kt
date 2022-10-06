package com.exchange.user.module.paymentModule

import android.util.Log
import com.google.androidbrowserhelper.trusted.LauncherActivity

class PaymentActivity : LauncherActivity(){
    override fun onDestroy() {
        super.onDestroy()
        Log.e("Payment Actibity - ","Distroy")
    }

}