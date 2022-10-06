package com.exchange.user.module.signin_module

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.exchange.user.BR
import com.exchange.user.R
import com.exchange.user.databinding.ActivitySigninBinding
import com.exchange.user.module.base_module.BaseActivity
import com.exchange.user.module.base_module.ConstantCommand
import com.exchange.user.module.base_module.base_view.ViewModelProviderFactory
import com.exchange.user.module.createaccount_module.CreateAccountActivity
import com.exchange.user.module.forgot_password_module.ForgotPasswordActivity
import com.exchange.user.module.signin_module.model.SignInRequest
import com.exchange.user.module.utility_module.BuilderManager
import com.exchange.user.module.utility_module.dialog_commands.DialogContinoue
import com.exchange.user.module.utility_module.dialog_commands.DilalogMessageCallBack
import com.exchange.user.module.utility_module.dialog_commands.SubmitButton
import com.exchange.user.module.varifyotp_module.VarifyOtpActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.GsonBuilder
import javax.inject.Inject

class SignInActivity : BaseActivity<ActivitySigninBinding, SignIntViewModel>(),SignInNavigator{

    @Inject
    lateinit var factory: ViewModelProviderFactory
    lateinit var signinviewModel:SignIntViewModel
    private lateinit var mGoogleApiClient : GoogleSignInClient
    private lateinit var callbackManager: CallbackManager
    lateinit var activitysigninBinding : ActivitySigninBinding
    private lateinit var dialogcalback: DilalogMessageCallBack
    override fun getLayoutId(): Int {
        return R.layout.activity_signin
    }
    override fun getViewModel(): SignIntViewModel {
        signinviewModel =  ViewModelProvider(this, factory).get(SignIntViewModel::class.java)
        return signinviewModel
    }

    override fun getBindingVariable(): Int {
        return BR.signIntviewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppEventsLogger.activateApp(application)
        activitysigninBinding = getViewDataBinding()
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary)
        callbackManager = CallbackManager.Factory.create()
        signinviewModel.setNavigator(this)
        signinviewModel.initProgress(this)
        initData()
    }
    @SuppressLint("ClickableViewAccessibility")
    override fun initData() {
        mGoogleApiClient = GoogleSignIn.getClient(this, GoogleSignInOptions.Builder(
            GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build())
        GoogleSignIn.getLastSignedInAccount(this)?.let {
            if (it.isExpired) {
                mGoogleApiClient.signOut()
            }
        }
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task->
            if (!task.isSuccessful) {
                return@addOnCompleteListener
            }
            signinviewModel.saveFcmToken( task.result?:"")
        }

        LoginManager.getInstance().registerCallback(callbackManager, object :
            FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult?) {
                loginResult?.accessToken
                signinviewModel.getProfileData()
            }
            override fun onCancel() {
                LoginManager.getInstance().logOut()
                showFeedbackMessage("Login Cancel")
            }

            override fun onError(exception: FacebookException?) {
                exception?.let {
                    showFeedbackMessage(it.message!!)
                }
            }

        })
        activitysigninBinding.rootlay.setOnTouchListener { _, event ->
            if (currentFocus != null) {
                hideKeyboard()
                currentFocus!!.clearFocus()
            }
            true
        }



        activitysigninBinding.password.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                activitysigninBinding.passwordtaxture.error = ""
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        activitysigninBinding.email.highlightColor = Color.TRANSPARENT
        activitysigninBinding.password.highlightColor = Color.TRANSPARENT
    }






    override fun onActionModeStarted(mode: ActionMode?) {
        val menu = mode?.menu
        menu?.clear()
        menu?.removeItem(android.R.id.copy)
        menu?.removeItem(android.R.id.paste)
        menu?.removeItem(android.R.id.selectAll)
        super.onActionModeStarted(mode)
    }


    override fun moveToNext() {
        finish()
    }

    override fun openConfirmation(request: SignInRequest, from: Int) {
        BuilderManager.openConfirm(this@SignInActivity,request,object : DialogContinoue{
            override fun onContinoue(request: SignInRequest) {
                getPasswordforVarify(request,from)
            }

        })
    }

    fun getPasswordforVarify(request: SignInRequest, from: Int) {
        BuilderManager.getPassword(this@SignInActivity,request,object : SubmitButton{
            override fun submit(value : String, dialog: DilalogMessageCallBack) {
                dialogcalback = dialog
                signinviewModel.mergeAccount(value,request,from)
            }

        })
    }
    override fun takeMobileNo(from: Int,requestSigin: SignInRequest) {
        BuilderManager.getPhoneNo(this@SignInActivity,from,requestSigin, object : SubmitButton {
            override fun submit(value: String, dialog: DilalogMessageCallBack) {
                dialogcalback = dialog
                signinviewModel.socialSignup(from,value,requestSigin)
            }

        })
    }

    override fun signIn() {
        activitysigninBinding.signinbtn.isEnabled = false
        signinviewModel.sigInUser(activitysigninBinding.email.text.toString(),activitysigninBinding.password.text.toString())
    }

    override fun forgotPassword() {
        activitysigninBinding.forgotpassword.isEnabled = false
        startActivity(Intent(applicationContext, ForgotPasswordActivity::class.java))
        finish()
    }

    override fun createAccount() {
        activitysigninBinding.createAccount.isEnabled = false
        startActivity(Intent(applicationContext, CreateAccountActivity::class.java))
        finish()
    }

    override fun openVerifyScreen(requestSigin: SignInRequest, from: Int) {
        val jsonresponce: String = GsonBuilder().setPrettyPrinting().create().toJson(requestSigin)
        startActivity(Intent(this@SignInActivity, VarifyOtpActivity:: class.java)
            .putExtra(ConstantCommand.DATA,jsonresponce)
            .putExtra(ConstantCommand.FROM_CHOOSE,from))
        finish()
    }

    override fun faceBookLogin() {
        LoginManager.getInstance().logInWithReadPermissions(this@SignInActivity,BuilderManager.getPermission())
    }

    override fun googleLogin() {
        resultLauncher.launch(mGoogleApiClient.signInIntent)
    }

    override fun signOutFacebookLogin() {
        LoginManager.getInstance().logOut()
    }
    override fun signOutGoogleLogin() {
        mGoogleApiClient.signOut()
        if(::mGoogleApiClient.isInitialized){
            mGoogleApiClient.signOut()
        }
    }

    override fun showFeedbackMessage(message: String) {
        if (::dialogcalback.isInitialized){
            dialogcalback.onError(message)
        }
        activitysigninBinding.signinbtn.isEnabled = true
        showBaseToast(activitysigninBinding.root, message)
    }

    override fun showError(emailError: Int, string: String) {
        when (emailError) {
            ConstantCommand.EMAIL_ERROR -> {
                activitysigninBinding.emailtaxture.error =string
                activitysigninBinding.signinbtn.isEnabled = true
            }
            ConstantCommand.PASSWORD_ERROR -> {
                activitysigninBinding.emailtaxture.error = ""
                activitysigninBinding.passwordtaxture.error = string
                activitysigninBinding.signinbtn.isEnabled = true
            }
            ConstantCommand.NO_ERROR -> {
                hideKeyboard()
                activitysigninBinding.emailtaxture.error = ""
                activitysigninBinding.passwordtaxture.error = string
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode== Activity.RESULT_OK && requestCode == ConstantCommand.RC_SIGN_IN) {
//            val result = GoogleSignIn.getSignedInAccountFromIntent(data)
//            signinviewModel.handleSignInResult(result)
//        }
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode== Activity.RESULT_OK) {
            signinviewModel.handleSignInResult(GoogleSignIn.getSignedInAccountFromIntent(result.data))
        }
    }

}