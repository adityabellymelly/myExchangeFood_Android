package com.exchange.user.module.change_password_module

import com.exchange.user.module.base_module.base_view.CommonActivityOrFragmentView

interface ChangePasswordNavigation : CommonActivityOrFragmentView {
    fun ChangePassword()
    fun onBackPress()
    fun showProgress(b: Boolean)
    fun showError(error:Int,msg: String)

}