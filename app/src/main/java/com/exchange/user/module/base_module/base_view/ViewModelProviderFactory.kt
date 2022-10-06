package com.exchange.user.module.base_module.base_view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.exchange.user.module.cart_module.CartFragmentViewModel
import com.exchange.user.module.change_address_module.ChangeAddressViewModel
import com.exchange.user.module.change_password_module.ChangePasswordViewModel
import com.exchange.user.module.choose_order_module.ChooseOrderViewModel
import com.exchange.user.module.country_module.CountryViewModel
import com.exchange.user.module.coupon_module.AppliedCoupenViewModel
import com.exchange.user.module.createaccount_module.CreateAccountViewModel
import com.exchange.user.module.discover_module.DiscoverViewModel
import com.exchange.user.module.edit_address_module.EditAdressViewModel
import com.exchange.user.module.edit_profile_module.EditProfileViewModel
import com.exchange.user.module.feedback_module.FeedbackViewModel
import com.exchange.user.module.forgot_password_module.ForgotPasswordViewModel
import com.exchange.user.module.home_module.HomeViewModel
import com.exchange.user.module.home_module.home_fragment.HomeFragmentViewModel
import com.exchange.user.module.order_confirm_module.OrderConfirmViewModel
import com.exchange.user.module.order_history.OrderHistoryViewModel
import com.exchange.user.module.order_info_module.OrderInfoViewModel
import com.exchange.user.module.paymentModule.WebPaymentViewModel
import com.exchange.user.module.profile_module.ProfileFragmentViewModel
import com.exchange.user.module.resetpassword_module.ResetPasswordViewModel
import com.exchange.user.module.restaurent_module.ResturentViewModel
import com.exchange.user.module.schedule_order_module.ScheduleViewModel
import com.exchange.user.module.about_us_module.AboutUsViewModel
import com.exchange.user.module.signin_module.SignIntViewModel
import com.exchange.user.module.splesh_module.SpleshViewModel
import com.exchange.user.module.utility_module.ApplicationSharePrefrence
import com.exchange.user.module.utility_module.ApplicationUtility
import com.exchange.user.module.utility_module.Validations
import com.exchange.user.module.varifyotp_module.VarifyOtpViewModel
import com.exchange.user.module.web_module.ApplicationRequest
import com.exchange.user.rx_module.SchedulerProvider
import javax.inject.Inject
import javax.inject.Singleton

@Suppress("UNCHECKED_CAST")
@Singleton
class ViewModelProviderFactory
@Inject
constructor(var applicationprefrence : ApplicationSharePrefrence,
            var schedulerProvider : SchedulerProvider, var applicationutility : ApplicationUtility,
            var applicationcontext : Context,
            var validations : Validations,
            var applicationrequest : ApplicationRequest
): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SpleshViewModel::class.java))
            return SpleshViewModel(applicationprefrence,schedulerProvider, applicationutility, applicationcontext , validations , applicationrequest) as T
        if (modelClass.isAssignableFrom(DiscoverViewModel::class.java))
            return DiscoverViewModel(applicationprefrence,schedulerProvider, applicationutility, applicationcontext , validations , applicationrequest) as T
        if (modelClass.isAssignableFrom(ChooseOrderViewModel::class.java))
            return ChooseOrderViewModel(applicationprefrence,schedulerProvider, applicationutility, applicationcontext , validations , applicationrequest) as T
        if (modelClass.isAssignableFrom(CountryViewModel::class.java))
            return CountryViewModel(applicationprefrence,schedulerProvider, applicationutility, applicationcontext , validations , applicationrequest) as T
        if (modelClass.isAssignableFrom(HomeViewModel::class.java))
            return HomeViewModel(applicationprefrence,schedulerProvider, applicationutility, applicationcontext , validations , applicationrequest) as T
        if (modelClass.isAssignableFrom(HomeFragmentViewModel::class.java))
            return HomeFragmentViewModel(applicationprefrence,schedulerProvider, applicationutility, applicationcontext , validations , applicationrequest) as T
        if (modelClass.isAssignableFrom(ResturentViewModel::class.java))
            return ResturentViewModel(applicationprefrence,schedulerProvider, applicationutility, applicationcontext , validations , applicationrequest) as T
        if (modelClass.isAssignableFrom(CartFragmentViewModel::class.java))
            return CartFragmentViewModel(applicationprefrence, schedulerProvider, applicationutility, applicationcontext, validations, applicationrequest) as T
        if (modelClass.isAssignableFrom(ProfileFragmentViewModel::class.java))
            return ProfileFragmentViewModel(applicationprefrence, schedulerProvider, applicationutility, applicationcontext, validations, applicationrequest) as T
        if (modelClass.isAssignableFrom(SignIntViewModel::class.java))
            return SignIntViewModel(applicationprefrence, schedulerProvider, applicationutility, applicationcontext, validations, applicationrequest) as T
        if (modelClass.isAssignableFrom(ForgotPasswordViewModel::class.java))
            return ForgotPasswordViewModel(applicationprefrence, schedulerProvider, applicationutility, applicationcontext, validations, applicationrequest) as T
        if (modelClass.isAssignableFrom(VarifyOtpViewModel::class.java))
            return VarifyOtpViewModel(applicationprefrence, schedulerProvider, applicationutility, applicationcontext, validations, applicationrequest) as T
        if (modelClass.isAssignableFrom(ResetPasswordViewModel::class.java))
            return ResetPasswordViewModel(applicationprefrence, schedulerProvider, applicationutility, applicationcontext, validations, applicationrequest) as T
        if (modelClass.isAssignableFrom(CreateAccountViewModel::class.java))
            return CreateAccountViewModel(applicationprefrence, schedulerProvider, applicationutility, applicationcontext, validations, applicationrequest) as T
        if (modelClass.isAssignableFrom(AboutUsViewModel::class.java))
            return AboutUsViewModel(applicationprefrence, schedulerProvider, applicationutility, applicationcontext, validations, applicationrequest) as T
        if (modelClass.isAssignableFrom(AppliedCoupenViewModel::class.java))
            return AppliedCoupenViewModel(applicationprefrence, schedulerProvider, applicationutility, applicationcontext, validations, applicationrequest) as T
        if (modelClass.isAssignableFrom(OrderConfirmViewModel::class.java))
            return OrderConfirmViewModel(applicationprefrence, schedulerProvider, applicationutility, applicationcontext, validations, applicationrequest) as T
        if (modelClass.isAssignableFrom(OrderHistoryViewModel::class.java))
            return OrderHistoryViewModel(applicationprefrence, schedulerProvider, applicationutility, applicationcontext, validations, applicationrequest) as T
        if (modelClass.isAssignableFrom(ChangeAddressViewModel::class.java))
            return ChangeAddressViewModel(applicationprefrence, schedulerProvider, applicationutility, applicationcontext, validations, applicationrequest) as T
        if (modelClass.isAssignableFrom(WebPaymentViewModel::class.java))
            return WebPaymentViewModel(applicationprefrence, schedulerProvider, applicationutility, applicationcontext, validations, applicationrequest) as T
        if (modelClass.isAssignableFrom(EditAdressViewModel::class.java))
            return EditAdressViewModel(applicationprefrence, schedulerProvider, applicationutility, applicationcontext, validations, applicationrequest) as T
        if (modelClass.isAssignableFrom(OrderInfoViewModel::class.java))
            return OrderInfoViewModel(applicationprefrence, schedulerProvider, applicationutility, applicationcontext, validations, applicationrequest) as T
        if (modelClass.isAssignableFrom(ScheduleViewModel::class.java))
            return ScheduleViewModel(applicationprefrence, schedulerProvider, applicationutility, applicationcontext, validations, applicationrequest) as T
        if (modelClass.isAssignableFrom(EditProfileViewModel::class.java))
            return EditProfileViewModel(applicationprefrence, schedulerProvider, applicationutility, applicationcontext, validations, applicationrequest) as T
        if (modelClass.isAssignableFrom(ChangePasswordViewModel::class.java))
            return ChangePasswordViewModel(applicationprefrence, schedulerProvider, applicationutility, applicationcontext, validations, applicationrequest) as T
        if (modelClass.isAssignableFrom(FeedbackViewModel::class.java))
            return FeedbackViewModel(applicationprefrence, schedulerProvider, applicationutility, applicationcontext, validations, applicationrequest) as T

        return super.create(modelClass)
    }

}