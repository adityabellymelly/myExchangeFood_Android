package com.exchange.user.module.createaccount_module

import com.exchange.user.module.base_module.base_view.CommonActivityOrFragmentView
import com.exchange.user.module.signin_module.model.SignInRequest

interface CreateAccountNavigator :CommonActivityOrFragmentView{

    fun onBackPress()
    fun continous()
    fun showError(emailError: Int, string: String)
    fun openVerifyScreen(loginrequest: SignInRequest)
}