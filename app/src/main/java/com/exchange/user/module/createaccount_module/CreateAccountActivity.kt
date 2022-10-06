package com.exchange.user.module.createaccount_module

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.ActionMode
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.exchange.user.BR
import com.exchange.user.R
import com.exchange.user.databinding.ActivityCreateAccountBinding
import com.exchange.user.module.base_module.BaseActivity
import com.exchange.user.module.base_module.ConstantCommand
import com.exchange.user.module.base_module.base_view.ViewModelProviderFactory
import com.exchange.user.module.signin_module.SignInActivity
import com.exchange.user.module.signin_module.model.SignInRequest
import com.exchange.user.module.varifyotp_module.VarifyOtpActivity
import com.google.gson.GsonBuilder
import javax.inject.Inject

class CreateAccountActivity : BaseActivity<ActivityCreateAccountBinding,CreateAccountViewModel>(),CreateAccountNavigator{
    @Inject
    lateinit var factory: ViewModelProviderFactory
    lateinit var createviewModel:CreateAccountViewModel
    lateinit var createaccountactivityBinding : ActivityCreateAccountBinding

    override fun getLayoutId(): Int {
        return R.layout.activity_create_account
    }

    override fun getViewModel(): CreateAccountViewModel {
        createviewModel =  ViewModelProvider(this, factory).get(CreateAccountViewModel::class.java)
        return createviewModel
    }

    override fun getBindingVariable(): Int {
        return BR.createaccountviewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createaccountactivityBinding = getViewDataBinding()
        createviewModel.setNavigator(this)
        initData()
    }

    override fun onActionModeStarted(mode: ActionMode?) {
        val menu = mode?.menu
        menu?.clear()
        menu?.removeItem(android.R.id.copy)
        menu?.removeItem(android.R.id.paste)
        menu?.removeItem(android.R.id.selectAll)
        super.onActionModeStarted(mode)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initData() {
        createaccountactivityBinding.rootlay.setOnTouchListener { _, _ ->
            if (currentFocus != null) {
                hideKeyboard()
                currentFocus!!.clearFocus()
            }
            true
        }
        createaccountactivityBinding.password.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                createaccountactivityBinding.passwordtaxture.error = ""
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        })
        createaccountactivityBinding.username.highlightColor = Color.TRANSPARENT
        createaccountactivityBinding.emailid.highlightColor = Color.TRANSPARENT
        createaccountactivityBinding.mobileno.highlightColor = Color.TRANSPARENT
        createaccountactivityBinding.password.highlightColor = Color.TRANSPARENT
    }

    override fun openVerifyScreen(loginrequest: SignInRequest) {
        val jsonresponce: String = GsonBuilder().setPrettyPrinting().create().toJson(loginrequest)
        startActivity(Intent(this@CreateAccountActivity, VarifyOtpActivity:: class.java)
            .putExtra(ConstantCommand.DATA,jsonresponce)
            .putExtra(ConstantCommand.FROM_CHOOSE,ConstantCommand.FROM_CREATE_ACCOUNT))
        createaccountactivityBinding.continouesBTN.isEnabled = true
        finish()
    }


    override fun onBackPress() {
        onBackPressed()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(applicationContext, SignInActivity:: class.java))
        finish()
    }
    override fun continous() {
        createaccountactivityBinding.continouesBTN.isEnabled = false
        createviewModel.create(createaccountactivityBinding.username.text.toString(),
            createaccountactivityBinding.emailid.text.toString(),
            createaccountactivityBinding.mobileno.text.toString(),
            createaccountactivityBinding.password.text.toString())
    }

    override fun showError(emailError: Int, string: String) {
        when (emailError) {
            ConstantCommand.USERNAME_ERROR->{
                createaccountactivityBinding.usernametaxture.error =string
                createaccountactivityBinding.continouesBTN.isEnabled = true
            }
            ConstantCommand.EMAIL_ERROR->{
                createaccountactivityBinding.usernametaxture.error = ""
                createaccountactivityBinding.emailidtaxture.error = string
                createaccountactivityBinding.continouesBTN.isEnabled = true
            }
            ConstantCommand.M0BILE_ERROR->{
                createaccountactivityBinding.usernametaxture.error = ""
                createaccountactivityBinding.emailidtaxture.error = ""
                createaccountactivityBinding.mobilenotaxture.error = string
                createaccountactivityBinding.continouesBTN.isEnabled = true
            }

            ConstantCommand.PASSWORD_ERROR->{
                createaccountactivityBinding.usernametaxture.error = ""
                createaccountactivityBinding.emailidtaxture.error = ""
                createaccountactivityBinding.mobilenotaxture.error = ""
                createaccountactivityBinding.passwordtaxture.error = string
                createaccountactivityBinding.continouesBTN.isEnabled = true
            }
            ConstantCommand.NO_ERROR -> {
                hideKeyboard()
                createaccountactivityBinding.usernametaxture.error = ""
                createaccountactivityBinding.emailidtaxture.error = ""
                createaccountactivityBinding.mobilenotaxture.error = ""
                createaccountactivityBinding.passwordtaxture.error = ""
            }
        }

    }

    override fun showFeedbackMessage(message: String) {
        createaccountactivityBinding.continouesBTN.isEnabled = true
        showBaseToast(createaccountactivityBinding.rootlay,message)
    }
}