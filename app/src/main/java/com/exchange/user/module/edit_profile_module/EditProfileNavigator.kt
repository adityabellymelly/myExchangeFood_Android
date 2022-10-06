package com.exchange.user.module.edit_profile_module

import com.exchange.user.module.base_module.base_view.CommonActivityOrFragmentView

interface EditProfileNavigator : CommonActivityOrFragmentView {
    fun onBackPress()
    fun showProgress(b: Boolean)
    fun updateProfile()
    fun showError(error:Int,msg: String)

}