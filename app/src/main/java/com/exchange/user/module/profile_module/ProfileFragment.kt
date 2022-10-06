package com.exchange.user.module.profile_module

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.exchange.user.BR
import com.exchange.user.R
import com.exchange.user.databinding.FragmentProfileBinding
import com.exchange.user.databinding.ViewOptionBinding
import com.exchange.user.databinding.ViewProductBinding
import com.exchange.user.module.base_module.BaseFragment
import com.exchange.user.module.base_module.ConstantCommand
import com.exchange.user.module.base_module.base_command.CartDataInt
import com.exchange.user.module.base_module.base_view.ViewModelProviderFactory
import com.exchange.user.module.cart_module.model.card_model.CartData
import com.exchange.user.module.change_password_module.ChangePasswordActivity
import com.exchange.user.module.country_module.CountryActivity
import com.exchange.user.module.edit_profile_module.EditProfileActivity
import com.exchange.user.module.order_history.OrderHistoryActivity
import com.exchange.user.module.restaurent_module.model.data_model.reastaurent_mode.RestrurentModel
import com.exchange.user.module.signin_module.SignInActivity
import com.exchange.user.module.utility_module.dialog_commands.OkButton
import com.rey.material.app.BottomSheetDialog
import javax.inject.Inject
import kotlin.math.roundToInt

class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileFragmentViewModel>(),ProfileFragmentNavigator{
    @Inject
    lateinit var factory : ViewModelProviderFactory
    private lateinit var profilefragmentViewModel : ProfileFragmentViewModel
    private lateinit var fragmentprofileBinding: FragmentProfileBinding
    override val bindingVariable: Int get() = BR.profileFragmentViewModel
    override val layoutId: Int get() = R.layout.fragment_profile

    override val viewModel: ProfileFragmentViewModel
        get() {
            profilefragmentViewModel = ViewModelProvider(this, factory).get(ProfileFragmentViewModel::class.java)
            return profilefragmentViewModel
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.statusBarColor = ContextCompat.getColor(getContaxt(), R.color.white_color)
        profilefragmentViewModel.setNavigator(this)

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentprofileBinding = viewDataBinding
        initData()
    }

    override fun inviteFriend() {
        Toast.makeText(getContaxt(),"Sharing Link Coming Soon",Toast.LENGTH_LONG).show()
    }

    override fun showFeedbackMessage(message: String) {


    }

    override fun onResume() {
        super.onResume()
        initData()
    }
    override fun initData() {
        if (profilefragmentViewModel.getUserID()!=null){
            fragmentprofileBinding.logoutbt.text = getContaxt().getString(R.string.logout)
            fragmentprofileBinding.data.visibility = View.GONE
            fragmentprofileBinding.view.visibility = View.VISIBLE
            fragmentprofileBinding.username.text = profilefragmentViewModel.getUserName()
            fragmentprofileBinding.username.text = profilefragmentViewModel.getUserName()
            fragmentprofileBinding.mobileno.text = profilefragmentViewModel.getPhoneNumber()
            fragmentprofileBinding.email.text = profilefragmentViewModel.getEmail()
            fragmentprofileBinding.location.text = if(profilefragmentViewModel.getAddress().isNullOrEmpty()){profilefragmentViewModel.getAddress()}else{"No address here"}
            val rewardPoint = profilefragmentViewModel.getReward()!!.toDouble().roundToInt().toString()
            val moneySave = if (CartDataInt.profiledata.orderHistory.isNullOrEmpty()){"0"}else{CartDataInt.profiledata.orderHistory.size.toString()}
            val unlock = if (profilefragmentViewModel.getLocation().isEmpty().not()){profilefragmentViewModel.getLocation()}else{"Not selected"}
            fragmentprofileBinding.rewardpoint.text = rewardPoint
            fragmentprofileBinding.unlock.text = unlock
            fragmentprofileBinding.moneysave.text = moneySave
        }
        else{
            fragmentprofileBinding.logoutbt.text = getContaxt().getString(R.string.signin_text)
            fragmentprofileBinding.data.visibility = View.VISIBLE
            fragmentprofileBinding.view.visibility = View.GONE
            CartDataInt.cartdata = CartData()
            CartDataInt.restrurent = RestrurentModel()
        }
        if (CartDataInt.profiledata.id!=null){
            // fragmentprofileBinding.editlayout.visibility = View.VISIBLE
        }else{
            fragmentprofileBinding.editlayout.visibility = View.GONE
        }
    }

    override fun openOptions() {
       openBottomOption()
    }


    private fun showAlertToDeleteAccount(okButton: OkButton) {
        val builder = AlertDialog.Builder(getContaxt())
        builder.setTitle("Delete Account")
        builder.setMessage("Are you sure you want to permanently delete your account?")
        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            profilefragmentViewModel.delete()
            okButton.onClickOkey()
        }
        builder.setNegativeButton(android.R.string.no) { dialog, which ->
            Toast.makeText(getContaxt(),
                android.R.string.no, Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        builder.show()
    }


    private fun openBottomOption() {
        val dialog = BottomSheetDialog(context, R.style.SheetDialog)
        dialog.window?.setBackgroundDrawableResource(R.color.transparent_color)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.heightParam(ViewGroup.LayoutParams.WRAP_CONTENT).inDuration(400)
        val viewOptionBinding : ViewOptionBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.view_option, null, false)
        dialog.setContentView(viewOptionBinding.root)

//        viewOptionBinding.deleteaccount.setOnClickListener {
//            showAlertToDeleteAccount(object: OkButton {
//                override fun onClickOkey() {
//                    dialog.dismiss()
//                }
//
//            })
//        }

        viewOptionBinding.editprofile.setOnClickListener {
            openEditActivity()
            dialog.dismiss()
        }

        viewOptionBinding.changepassword.setOnClickListener {
            openChangePassword()
            dialog.dismiss()
        }
        viewOptionBinding.baseLocation.setOnClickListener {
            startActivity(Intent(getContaxt(), CountryActivity:: class.java)
                .putExtra(ConstantCommand.DATA, ConstantCommand.FROM_HOME))
            dialog.dismiss()
        }

        viewOptionBinding.cancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun openSiginActivity() {
        startActivity(Intent(getContaxt(), SignInActivity::class.java))
    }
    override fun openEditActivity() {
        startActivity(Intent(getContaxt(), EditProfileActivity::class.java))
    }

    override fun openChangePassword() {
        startActivity(Intent(getContaxt(), ChangePasswordActivity::class.java))
    }

    override fun onOrderhistory() {
        startActivity(Intent(getContaxt(),OrderHistoryActivity::class.java)
            .putExtra(ConstantCommand.DATA,ConstantCommand.FROM_CHOOSE)
        )
    }
    override fun changeLocation() {
        startActivity(Intent(getContaxt(), CountryActivity:: class.java)
            .putExtra(ConstantCommand.DATA, ConstantCommand.FROM_HOME))
    }

    @SuppressLint("SetTextI18n")
    override fun updateUI() {
        fragmentprofileBinding.logoutbt.text = "Sign In"
        initData()
    }


}