package com.exchange.user.module.base_module

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.exchange.user.R
import com.exchange.user.module.base_module.base_view.BaseViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.AndroidSupportInjection
import java.lang.Exception

abstract class BaseFragment<T : ViewDataBinding, V : BaseViewModel> : Fragment(){

    private var baseActivity : BaseActivity<*, *>? = null
    lateinit var viewDataBinding: T
        private set
    private var mViewModel: V? = null
    abstract val bindingVariable: Int
    abstract val layoutId: Int
    abstract val viewModel: V
    private var mRootView: View? = null
    lateinit var mContext: Context


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity<*, *>) {
            val activity = context as BaseActivity<*, *>?
            this.baseActivity = activity as BaseActivity<*, *>
            mContext = context
            performDependencyInjection()
            activity.onFragmentAttached()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        mViewModel = viewModel
        setHasOptionsMenu(false)
    }

    fun getContaxt() = mContext

    fun getBaseAcivity() = baseActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewDataBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        mRootView = viewDataBinding.root
        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.setVariable(bindingVariable, mViewModel)
        viewDataBinding.lifecycleOwner = this
        viewDataBinding.executePendingBindings()
    }

    override fun onDetach() {
        baseActivity = null
        super.onDetach()
    }

    private fun performDependencyInjection() {
        AndroidSupportInjection.inject(this)
    }
    interface Callback{
        fun onFragmentAttached()
        fun onFragmentDetached(tag: String)
    }

    fun hideKeyboard() {
        if (baseActivity != null) {
            baseActivity!!.hideKeyboard()
        }
    }

    fun showKeyboard(){
        if (baseActivity != null) {
            baseActivity!!.showKeyboard()
        }
    }

    @SuppressLint("WrongConstant")
    fun showBaseToast(view: View, message: String) {
        try {
            val mSnackbar = Snackbar.make(view, message, Toast.LENGTH_SHORT)
            mSnackbar.view.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.colorPrimary))
            if (mSnackbar.isShown){ mSnackbar.dismiss()}
            mSnackbar.show()
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    abstract fun initData()


    fun onBackPressed(){

    }


}