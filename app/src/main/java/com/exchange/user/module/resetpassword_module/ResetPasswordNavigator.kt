package com.exchange.user.module.resetpassword_module

import com.exchange.user.module.base_module.base_view.CommonActivityOrFragmentView

interface ResetPasswordNavigator : CommonActivityOrFragmentView {
    fun onBackPress()
    fun resetPassword()
    fun openSigninScreen()
}