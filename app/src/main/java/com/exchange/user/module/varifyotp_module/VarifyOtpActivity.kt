package com.exchange.user.module.varifyotp_module

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.exchange.user.BR
import com.exchange.user.R
import com.exchange.user.databinding.ActivityVarifyOtpBinding
import com.exchange.user.module.base_module.BaseActivity
import com.exchange.user.module.base_module.ConstantCommand
import com.exchange.user.module.base_module.base_view.ViewModelProviderFactory
import com.exchange.user.module.resetpassword_module.ResetPasswordActivity
import com.exchange.user.module.signin_module.SignInActivity
import com.exchange.user.module.signin_module.model.SignInRequest
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import javax.inject.Inject

class VarifyOtpActivity : BaseActivity<ActivityVarifyOtpBinding,VarifyOtpViewModel>(),VarifyOtpNavigator{
    @Inject
    lateinit var factory: ViewModelProviderFactory
    lateinit var varifyviewModel:VarifyOtpViewModel
    lateinit var forgotactivityBinding :ActivityVarifyOtpBinding
    private var resendActive : Boolean =  false

    override fun getLayoutId(): Int {
        return R.layout.activity_varify_otp
    }

    override fun getViewModel(): VarifyOtpViewModel {
        varifyviewModel =  ViewModelProvider(this, factory).get(VarifyOtpViewModel::class.java)
        return varifyviewModel
    }

    override fun getBindingVariable(): Int {
        return BR.varifyotpViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        forgotactivityBinding = getViewDataBinding()
        varifyviewModel.setNavigator(this)
        initData()
    }

    override fun initData() {
        val signInRequest: SignInRequest = Gson().fromJson(intent.getStringExtra(ConstantCommand.DATA),SignInRequest::class.java)
        varifyviewModel.setFrom(intent.getIntExtra(ConstantCommand.FROM_CHOOSE,0))
        varifyviewModel.setSignInRequest(signInRequest)
        forgotactivityBinding.mobileno.text = "".plus(signInRequest.data?.username)
        activateTimer()
        if (varifyviewModel.getFrom()==ConstantCommand.FROM_FORGOT) {
            forgotactivityBinding.passwordlay.visibility = View.VISIBLE
            forgotactivityBinding.varifyBT.text = getString(R.string.varfy_reset)
        }else
            forgotactivityBinding.passwordlay.visibility = View.GONE

        forgotactivityBinding.varifyBT.text = getString(R.string.verify_otp)
    }
    override fun showFeedbackMessage(message: String) {
        showBaseToast(forgotactivityBinding.root,message)
    }
    override fun openSigninScreen() {
        startActivity(Intent(applicationContext,SignInActivity:: class.java))
        finish()
    }

    override fun onVerify() {
        varifyviewModel.gotoVerify(forgotactivityBinding.otpView2.text.toString(),forgotactivityBinding.password.text.toString())
    }

    override fun onBackPress() {
        startActivity(Intent(applicationContext, SignInActivity:: class.java))
        finish()
    }

    override fun moveToNext() {
        finish()
    }

    override fun openResetPassword(signInRequest: SignInRequest) {
        startActivity(Intent(applicationContext, ResetPasswordActivity::class.java)
            .putExtra(ConstantCommand.DATA,GsonBuilder().setPrettyPrinting().create().toJson(signInRequest)))
        finish()
    }

     fun activateTimer(){
         val timer = object : CountDownTimer(120000,1000){
             @SuppressLint("SetTextI18n")
             override fun onFinish() {
                 activateResend()
                 forgotactivityBinding.timertext.text = "Left 0:00 second"
             }

             @SuppressLint("SetTextI18n")
             override fun onTick(millisUntilFinished: Long) {
                 val remaining = (millisUntilFinished / 1000).toInt()
                 // val progress = 120 - remaining
                 forgotactivityBinding.timertext.text = "Left ".plus(varifyviewModel.calculateTime(remaining.toLong())).plus(" second")
             }

         }
         timer.start()
     }


    override fun onResendOtp() {
        if (resendActive){
            varifyviewModel.goresendOtp()
            resendActive = false
            forgotactivityBinding.resend.setTextColor(ContextCompat.getColor(this,R.color.gray_light))
            activateTimer()

        }

    }

    override fun onresendSucess() {

    }
    private fun activateResend() {
        resendActive = true
        forgotactivityBinding.resend.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary))
    }
}