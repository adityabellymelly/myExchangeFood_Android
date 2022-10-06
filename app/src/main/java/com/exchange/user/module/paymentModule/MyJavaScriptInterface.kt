package com.exchange.user.module.paymentModule

import android.content.Context
import android.util.Log
import android.webkit.JavascriptInterface

class MyJavaScriptInterface(val context: Context){
    @JavascriptInterface
    fun submitPayment(html : String){
     Log.e("html",html)
    }
}