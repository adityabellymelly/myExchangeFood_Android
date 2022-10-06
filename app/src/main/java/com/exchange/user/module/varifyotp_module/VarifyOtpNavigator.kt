package com.exchange.user.module.varifyotp_module

import com.exchange.user.module.base_module.base_view.CommonActivityOrFragmentView
import com.exchange.user.module.signin_module.model.SignInRequest

interface VarifyOtpNavigator : CommonActivityOrFragmentView {
    fun onBackPress()
    fun onVerify()
    fun openResetPassword(signInRequest: SignInRequest)
    fun openSigninScreen()
    fun moveToNext()
    fun onResendOtp()
    fun onresendSucess()
}