package com.exchange.user.module.base_module

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.exchange.user.R
import com.exchange.user.module.base_module.base_view.BaseViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.android.AndroidInjection
import java.lang.Exception

abstract class BaseActivity <T: ViewDataBinding,V : BaseViewModel> : AppCompatActivity(),BaseFragment.Callback {

    private lateinit var mViewDataBinding : T
    private lateinit var mViewModel : V

    @LayoutRes
    abstract fun getLayoutId():Int

    abstract fun getViewModel() : V
    abstract fun getBindingVariable() :Int

    override fun onFragmentAttached() {}

    override fun onFragmentDetached(tag: String) {
        supportFragmentManager.popBackStackImmediate()
    }

    fun getViewDataBinding(): T {
        return mViewDataBinding
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        performDependencyInjection()
        super.onCreate(savedInstanceState)
        performDataBinding()
        // initData()
    }
    @SuppressLint("WrongConstant")
    fun showBaseToast(view: View, message: String) {
        try {
            val mSnackbar = Snackbar.make(view, message, Toast.LENGTH_SHORT)
            mSnackbar.view.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.colorPrimary))
            if (mSnackbar.isShown){ mSnackbar.dismiss()}
            mSnackbar.show()
        }catch (e: Exception){
            e.printStackTrace()
        }
    }
    private fun performDependencyInjection(){
        AndroidInjection.inject(this)
    }

    private fun performDataBinding(){
        mViewDataBinding =  DataBindingUtil.setContentView(this,getLayoutId())
        if (!::mViewModel.isInitialized){
            this.mViewModel  = getViewModel()
        }
        mViewDataBinding.setVariable(getBindingVariable(),mViewModel)
        mViewDataBinding.executePendingBindings()
    }

    fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun showKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    abstract fun initData()
}