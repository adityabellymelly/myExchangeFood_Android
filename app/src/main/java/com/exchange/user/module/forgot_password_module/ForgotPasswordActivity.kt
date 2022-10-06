package com.exchange.user.module.forgot_password_module

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.ActionMode
import androidx.lifecycle.ViewModelProvider
import com.exchange.user.BR
import com.exchange.user.R
import com.exchange.user.databinding.ActivityForgotPasswordBinding
import com.exchange.user.module.base_module.BaseActivity
import com.exchange.user.module.base_module.ConstantCommand
import com.exchange.user.module.base_module.base_view.ViewModelProviderFactory
import com.exchange.user.module.signin_module.SignInActivity
import com.exchange.user.module.signin_module.model.SignInRequest
import com.exchange.user.module.varifyotp_module.VarifyOtpActivity
import com.google.gson.GsonBuilder
import javax.inject.Inject

class ForgotPasswordActivity: BaseActivity<ActivityForgotPasswordBinding, ForgotPasswordViewModel>(),ForgotPasswordNavigator{
    @Inject
    lateinit var factory: ViewModelProviderFactory
    private lateinit var forgotviewModel:ForgotPasswordViewModel
    private lateinit var forgotactivityBinding :ActivityForgotPasswordBinding
    override fun getLayoutId(): Int {
        return R.layout.activity_forgot_password
    }
    override fun getViewModel(): ForgotPasswordViewModel {
        forgotviewModel =  ViewModelProvider(this, factory).get(ForgotPasswordViewModel::class.java)
        return forgotviewModel
    }

    override fun getBindingVariable(): Int {
        return BR.forgotPasswordViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        forgotactivityBinding = getViewDataBinding()
        forgotviewModel.setNavigator(this)
        initData()

    }
    override fun initData() {
        forgotactivityBinding.mobileno.highlightColor = Color.TRANSPARENT
    }


    override fun onActionModeStarted(mode: ActionMode?) {
        val menu = mode?.menu
        menu?.clear()
        menu?.removeItem(android.R.id.copy)
        menu?.removeItem(android.R.id.paste)
        menu?.removeItem(android.R.id.selectAll)
        super.onActionModeStarted(mode)
    }

    override fun openOtpScreen(loginrequest: SignInRequest) {
        forgotactivityBinding.sendotp.isEnabled = true
        startActivity(Intent(this@ForgotPasswordActivity, VarifyOtpActivity:: class.java)
            .putExtra(ConstantCommand.DATA,GsonBuilder().setPrettyPrinting().create().toJson(loginrequest))
            .putExtra(ConstantCommand.FROM_CHOOSE,ConstantCommand.FROM_FORGOT))
          finish()
    }


    override fun onBackPress() {
        startActivity(Intent(applicationContext, SignInActivity:: class.java))
        finish()
    }

    override fun onSendOtp() {
        forgotactivityBinding.sendotp.isEnabled = false
        forgotviewModel.sendOTPReq(forgotactivityBinding.mobileno.text.toString())
    }

    override fun showError(emailError: Int, string: String) {
        forgotactivityBinding.mobilenotaxture.error = string
        forgotactivityBinding.sendotp.isEnabled = true
    }
    override fun showFeedbackMessage(message: String) {
        showBaseToast(forgotactivityBinding.root,message)
        forgotactivityBinding.sendotp.isEnabled = true
    }
}