package com.exchange.user.module.resetpassword_module

import android.content.Intent
import android.os.Bundle
import android.view.ActionMode
import androidx.lifecycle.ViewModelProvider
import com.exchange.user.BR
import com.exchange.user.R
import com.exchange.user.databinding.ActivityResetPasswordBinding
import com.exchange.user.module.base_module.BaseActivity
import com.exchange.user.module.base_module.ConstantCommand
import com.exchange.user.module.base_module.base_view.ViewModelProviderFactory
import com.exchange.user.module.signin_module.SignInActivity
import com.exchange.user.module.signin_module.model.SignInRequest
import com.google.gson.Gson
import javax.inject.Inject

class ResetPasswordActivity : BaseActivity<ActivityResetPasswordBinding, ResetPasswordViewModel>(),ResetPasswordNavigator{
    @Inject
    lateinit var factory: ViewModelProviderFactory
    lateinit var resetviewModel: ResetPasswordViewModel
    lateinit var resetactivityBinding : ActivityResetPasswordBinding
    override fun getLayoutId(): Int {
        return R.layout.activity_reset_password
    }
    override fun getViewModel(): ResetPasswordViewModel {
        resetviewModel =  ViewModelProvider(this, factory).get(ResetPasswordViewModel::class.java)
        return resetviewModel
    }
    override fun getBindingVariable(): Int {
        return BR.resetPasswordViewModel
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resetactivityBinding = getViewDataBinding()
        resetviewModel.setNavigator(this)
        initData()
    }
    override fun initData() {
        val signInRequest: SignInRequest = Gson().fromJson(intent.getStringExtra(ConstantCommand.DATA),SignInRequest::class.java)
        resetviewModel.setSignInRequest(signInRequest)
    }
    override fun onActionModeStarted(mode: ActionMode?) {
        val menu = mode?.menu
        menu?.clear()
        menu?.removeItem(android.R.id.copy)
        menu?.removeItem(android.R.id.paste)
        menu?.removeItem(android.R.id.selectAll)
        super.onActionModeStarted(mode)
    }
    override fun resetPassword() {
        resetviewModel.appliyReset(resetactivityBinding.password.text.toString())
    }
    override fun openSigninScreen() {
        startActivity(Intent(applicationContext, SignInActivity:: class.java))
        finish()
    }
    override fun onBackPress() {
        startActivity(Intent(applicationContext, SignInActivity:: class.java))
        finish()
    }
    override fun showFeedbackMessage(message: String) {
        showBaseToast(resetactivityBinding.root,message)
    }
}