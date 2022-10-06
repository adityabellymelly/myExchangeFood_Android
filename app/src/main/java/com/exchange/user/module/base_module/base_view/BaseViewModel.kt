package com.exchange.user.module.base_module.base_view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModel
import com.exchange.user.R
import com.exchange.user.module.base_module.base_command.BasicInterface
import com.exchange.user.module.utility_module.ApplicationSharePrefrence
import com.exchange.user.module.utility_module.ApplicationUtility
import com.exchange.user.module.utility_module.Validations
import com.exchange.user.module.web_module.ApplicationRequest
import com.exchange.user.rx_module.SchedulerProvider
import com.github.ybq.android.spinkit.style.Wave
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel(
    private var applicationSharedPref : ApplicationSharePrefrence, var schedulerProvider : SchedulerProvider,
    var applicationutility : ApplicationUtility,
    var applicationcontext : Context,
    var validations  : Validations,
    var applicationrequest : ApplicationRequest
): ViewModel(), BasicInterface {

    private val mCompositeDisposable: CompositeDisposable = CompositeDisposable()
    //private val mIsLoading = ObservableBoolean()
    private lateinit var mAlertDialog: AlertDialog

    private lateinit var mNavigator: Any

    override fun showProgress() {
        if (this::mAlertDialog.isInitialized) {
            mAlertDialog.show()
        }
    }

    override fun hideProgress() {  if (this::mAlertDialog.isInitialized && mAlertDialog.isShowing) {
        mAlertDialog.dismiss()
    }
    }

    override fun initProgress(context: Context) {
        showBaseProgressDialog(context,"")
    }

    override fun logoutUser() {}

    fun getApplicationUtility(): ApplicationUtility {
        return applicationutility
    }

    fun getScheduleProvider(): SchedulerProvider {
        return schedulerProvider
    }

    fun getContext(): Context {
        return applicationcontext
    }

    //
    fun getApplicationSharePref(): ApplicationSharePrefrence {
        return applicationSharedPref
    }

    //
    fun getApplicationRequest(): ApplicationRequest {
        return applicationrequest
    }

    fun getValidation(): Validations {
        return validations
    }


    fun getCompositeDisposable(): CompositeDisposable {
        return mCompositeDisposable
    }

    fun getNavigator(): Any {
        return mNavigator
    }

    fun setNavigator(navigator: Any) {
        this.mNavigator = navigator
    }

    override fun onCleared() {
        mCompositeDisposable.dispose()
        super.onCleared()
    }

    @SuppressLint("InflateParams")
   private fun showBaseProgressDialog(context: Context, message: String) {
        val mAlertDialogBuilder = AlertDialog.Builder(context,R.style.dialof_style)
        mAlertDialogBuilder.setCancelable(false)
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.view_progress_dialog, null)
        view.findViewById<ProgressBar>(R.id.progressBarPB).apply {
            indeterminateDrawable = Wave()
        }
        view.findViewById<TextView>(R.id.progressMessageTV).apply {
            text = message
        }
        mAlertDialogBuilder.setView(view)
        mAlertDialog = mAlertDialogBuilder.create()
        mAlertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        val layoutParams = WindowManager.LayoutParams()
        val window = mAlertDialog.window
        window!!.setGravity(Gravity.CENTER)
        layoutParams.copyFrom(window.attributes)
        // show dialog on full screen
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        window.attributes = layoutParams

    }

}

