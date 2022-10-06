package com.exchange.user.module.change_password_module

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.exchange.user.BR
import com.exchange.user.R
import com.exchange.user.databinding.ActivityChangePasswordBinding
import com.exchange.user.module.base_module.BaseActivity
import com.exchange.user.module.base_module.ConstantCommand
import com.exchange.user.module.base_module.base_view.ViewModelProviderFactory
import javax.inject.Inject

class ChangePasswordActivity : BaseActivity<ActivityChangePasswordBinding,ChangePasswordViewModel>(),
    ChangePasswordNavigation {
    @Inject
    lateinit var factory: ViewModelProviderFactory
    private lateinit var changePasswordViewModel: ChangePasswordViewModel
    private lateinit var  changePasswordActivityBinding : ActivityChangePasswordBinding
    override fun getLayoutId(): Int {
        return R.layout.activity_change_password
    }

    override fun getViewModel(): ChangePasswordViewModel {
        changePasswordViewModel = ViewModelProvider(this, factory)
            .get(ChangePasswordViewModel::class.java)
        return changePasswordViewModel
    }
    override fun getBindingVariable(): Int {
        return BR.changePasswordViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changePasswordActivityBinding = getViewDataBinding()
        changePasswordViewModel.setNavigator(this)
        initData()
    }

    override fun initData() {

    }

    override fun ChangePassword() {
        changePasswordViewModel.changePassword(
            changePasswordActivityBinding.currentPassword.text.toString(),
            changePasswordActivityBinding.newPassword.text.toString(),
            changePasswordActivityBinding.repeatPassword.text.toString()
        )
    }

    override fun onBackPress() {
        onBackPressed()
    }

    override fun showProgress(b: Boolean) {
        if (b){
            changePasswordActivityBinding.animatedview.visibility = View.VISIBLE
            changePasswordActivityBinding.saveaddrsssbtn.visibility = View.GONE
        }else{
            changePasswordActivityBinding.animatedview.visibility = View.GONE
            changePasswordActivityBinding.saveaddrsssbtn.visibility = View.VISIBLE
        }
    }

    override fun showError(error: Int, msg: String) {
        when (error) {
            ConstantCommand.USERNAME_ERROR ->{
                changePasswordActivityBinding.currentPasswordtaxture.error = msg
            }
            ConstantCommand.PASSWORD_ERROR ->{
                changePasswordActivityBinding.newPasswordtaxture.error = msg
                changePasswordActivityBinding.currentPasswordtaxture.error = ""
            }
            ConstantCommand.M0BILE_ERROR ->{
                changePasswordActivityBinding.repeatPasswordtaxture.error = msg
                changePasswordActivityBinding.newPasswordtaxture.error = ""
                changePasswordActivityBinding.currentPasswordtaxture.error = ""
            }else ->{
            changePasswordActivityBinding.newPasswordtaxture.error = ""
            changePasswordActivityBinding.repeatPasswordtaxture.error = ""
            changePasswordActivityBinding.currentPasswordtaxture.error = ""
            }
        }
    }

    override fun showFeedbackMessage(message: String) {
        showBaseToast(changePasswordActivityBinding.root,message)
    }
}