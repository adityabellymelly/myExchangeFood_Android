package com.exchange.user.module.utility_module.dialog_commands

import com.exchange.user.module.signin_module.model.SignInRequest

interface DialogContinoue {
    fun onContinoue(request: SignInRequest)
}