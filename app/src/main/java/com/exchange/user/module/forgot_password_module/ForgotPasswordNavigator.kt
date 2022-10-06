package com.exchange.user.module.forgot_password_module

import com.exchange.user.module.base_module.base_view.CommonActivityOrFragmentView
import com.exchange.user.module.signin_module.model.SignInRequest

interface ForgotPasswordNavigator :CommonActivityOrFragmentView {
    fun onSendOtp()
    fun onBackPress()
    fun showError(emailError: Int, string: String)
    fun openOtpScreen(loginrequest: SignInRequest)
}