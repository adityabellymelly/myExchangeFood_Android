package com.exchange.user.module.edit_profile_module

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.exchange.user.R
import com.exchange.user.databinding.ActivityEditProfileBinding
import com.exchange.user.module.base_module.BaseActivity
import com.exchange.user.module.base_module.ConstantCommand
import com.exchange.user.module.base_module.base_view.ViewModelProviderFactory
import javax.inject.Inject

class EditProfileActivity : BaseActivity<ActivityEditProfileBinding,EditProfileViewModel>(),EditProfileNavigator {
    @Inject
    lateinit var factory: ViewModelProviderFactory
    private lateinit var editProfileViewModel: EditProfileViewModel
    private lateinit var  editProfileActivityBinding : ActivityEditProfileBinding

    override fun getLayoutId(): Int {
        return R.layout.activity_edit_profile
    }

    override fun getViewModel(): EditProfileViewModel {
        editProfileViewModel = ViewModelProvider(this, factory)
            .get(EditProfileViewModel::class.java)
        return editProfileViewModel
    }
    override fun getBindingVariable(): Int {
        return androidx.databinding.library.baseAdapters.BR.editProfileViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editProfileActivityBinding = getViewDataBinding()
        editProfileViewModel.setNavigator(this)
        initData()
    }

    override fun initData() {
        val profile = editProfileViewModel.getProfileData()
        profile?.let {
            editProfileActivityBinding.firstname.setText(profile.fName)
            editProfileActivityBinding.lastname.setText(profile.lName)
            editProfileActivityBinding.username.setText(profile.email)
            editProfileActivityBinding.mobileno.setText(profile.tel)
        }
        editProfileActivityBinding.firstname.highlightColor = Color.TRANSPARENT
        editProfileActivityBinding.lastname.highlightColor = Color.TRANSPARENT
        editProfileActivityBinding.username.highlightColor = Color.TRANSPARENT
        editProfileActivityBinding.mobileno.highlightColor = Color.TRANSPARENT
    }

    override fun updateProfile() {
        editProfileViewModel.saveAndUpdate(editProfileActivityBinding.firstname.text.toString(),
            editProfileActivityBinding.lastname.text.toString(),
            editProfileActivityBinding.username.text.toString(),
                    editProfileActivityBinding.mobileno.text.toString())
    }

    override fun showError(error: Int, msg: String) {
        when (error) {
            ConstantCommand.USERNAME_ERROR->{
                editProfileActivityBinding.firstnameTexture.error = msg
            }
            ConstantCommand.PASSWORD_ERROR->{
                editProfileActivityBinding.firstnameTexture.error = ""
                editProfileActivityBinding.lastnameTexture.error = msg
            }
            ConstantCommand.EMAIL_ERROR->{
                editProfileActivityBinding.firstnameTexture.error = ""
                editProfileActivityBinding.lastnameTexture.error = ""
                editProfileActivityBinding.usernametaxture.error = msg

            }
            ConstantCommand.M0BILE_ERROR->{
                editProfileActivityBinding.firstnameTexture.error = ""
                editProfileActivityBinding.lastnameTexture.error = ""
                editProfileActivityBinding.usernametaxture.error = ""
                editProfileActivityBinding.mobileNotaxture.error = msg

            }
            else->{
                editProfileActivityBinding.firstnameTexture.error = ""
                editProfileActivityBinding.lastnameTexture.error = ""
                editProfileActivityBinding.usernametaxture.error = ""
                editProfileActivityBinding.mobileNotaxture.error =""

            }

        }
    }

    override fun showProgress(b: Boolean) {
        if (b){
            editProfileActivityBinding.animatedview.visibility = View.VISIBLE
            editProfileActivityBinding.saveaddrsssbtn.visibility = View.GONE
        }else{
            editProfileActivityBinding.animatedview.visibility = View.GONE
            editProfileActivityBinding.saveaddrsssbtn.visibility = View.VISIBLE
        }
    }
    override fun showFeedbackMessage(message: String) {
        showBaseToast(editProfileActivityBinding.root,message)
    }

    override fun onBackPress() {
        onBackPressed()
    }


}