package com.exchange.user.module.signin_module

import com.exchange.user.module.base_module.base_view.CommonActivityOrFragmentView
import com.exchange.user.module.signin_module.model.SignInRequest

interface SignInNavigator: CommonActivityOrFragmentView{
    fun signOutFacebookLogin()
    fun signOutGoogleLogin()
    fun openConfirmation(
        request: SignInRequest,
        from: Int
    )
    fun forgotPassword()
    fun createAccount()
    fun faceBookLogin()
    fun googleLogin()
    fun signIn()

    fun showError(emailError: Int, string: String)
    fun moveToNext()
    fun takeMobileNo(from: Int,requestSigin: SignInRequest)
    fun openVerifyScreen(
        requestSigin: SignInRequest,
        from: Int
    )
}