package com.exchange.user.module.base_module.base_command

import android.content.Context

interface BasicInterface {

    fun showProgress()
    fun hideProgress()
    fun initProgress(context: Context)
    fun logoutUser()
}