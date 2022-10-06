package com.exchange.user.di_module.modules

import com.exchange.user.di_module.modules.activity_module.change_address_module.ChangeAddressModule
import com.exchange.user.di_module.modules.activity_module.country_module.CountyModule
import com.exchange.user.di_module.modules.activity_module.coupon_module.CouponModule
import com.exchange.user.di_module.modules.activity_module.feedback_module.FeedbackModule
import com.exchange.user.di_module.modules.activity_module.order_history_module.OrderHistoryModule
import com.exchange.user.di_module.modules.activity_module.order_info_module.OrderInfoModule
import com.exchange.user.di_module.modules.activity_module.resturent_module.RestaurentModule
import com.exchange.user.di_module.modules.fragment_module.cart_module.CartFragmentProvider
import com.exchange.user.di_module.modules.fragment_module.home_module.HomeFragmentProvider
import com.exchange.user.di_module.modules.fragment_module.orderhistory_module.OrderHistoryFragmentProvider
import com.exchange.user.di_module.modules.fragment_module.profile_module.ProfileFragmentProvider
import com.exchange.user.di_module.modules.fragment_module.aboutUs_module.AboutUsFragmentProvider
import com.exchange.user.module.change_address_module.ChangeAddressActivity
import com.exchange.user.module.change_password_module.ChangePasswordActivity
import com.exchange.user.module.choose_order_module.ChooseOrderActivity
import com.exchange.user.module.country_module.CountryActivity
import com.exchange.user.module.coupon_module.AppliedCoupenActivity
import com.exchange.user.module.createaccount_module.CreateAccountActivity
import com.exchange.user.module.discover_module.DiscoverActivity
import com.exchange.user.module.edit_address_module.EditAddressActivity
import com.exchange.user.module.edit_profile_module.EditProfileActivity
import com.exchange.user.module.feedback_module.FeedbackActivity
import com.exchange.user.module.forgot_password_module.ForgotPasswordActivity
import com.exchange.user.module.home_module.HomeActivity
import com.exchange.user.module.order_confirm_module.OrderConfirmActivity
import com.exchange.user.module.order_history.OrderHistoryActivity
import com.exchange.user.module.order_info_module.OrderInfoActivity
import com.exchange.user.module.paymentModule.WebPayment
import com.exchange.user.module.resetpassword_module.ResetPasswordActivity
import com.exchange.user.module.restaurent_module.ResturantActivity
import com.exchange.user.module.schedule_order_module.ScheduleOrderActivity
import com.exchange.user.module.signin_module.SignInActivity
import com.exchange.user.module.splesh_module.SplashActivity
import com.exchange.user.module.varifyotp_module.VarifyOtpActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract  class ActivityBuilder {
    @ContributesAndroidInjector
    abstract  fun bindSplashActivity() : SplashActivity
    @ContributesAndroidInjector
    abstract  fun bindDiscoverActivity() : DiscoverActivity
    @ContributesAndroidInjector
    abstract  fun bindChooseOrderActivity() : ChooseOrderActivity
    @ContributesAndroidInjector
    abstract  fun bindEditProfileActivity() : EditProfileActivity
    @ContributesAndroidInjector
    abstract  fun bindChangePasswordActivity() : ChangePasswordActivity

    @ContributesAndroidInjector(modules = [CountyModule::class])
    abstract  fun bindCountryActivity() : CountryActivity

    @ContributesAndroidInjector(modules = [
        HomeFragmentProvider::class,
        CartFragmentProvider::class,
        ProfileFragmentProvider::class,
        AboutUsFragmentProvider::class,
        OrderHistoryFragmentProvider::class])
    abstract fun bindHomeActivityActivity():HomeActivity

    @ContributesAndroidInjector(modules=[OrderInfoModule::class])
    abstract  fun bindOrderInfoActivity() : OrderInfoActivity

    @ContributesAndroidInjector(modules = [RestaurentModule::class])
    abstract  fun bindResturantActivity() : ResturantActivity

    @ContributesAndroidInjector
    abstract  fun bindSignInActivity() : SignInActivity

    @ContributesAndroidInjector
    abstract  fun bindForgotPasswordActivity() : ForgotPasswordActivity

    @ContributesAndroidInjector
    abstract  fun bindVarifyOtpActivity() : VarifyOtpActivity

    @ContributesAndroidInjector
    abstract  fun bindResetPasswordActivity() : ResetPasswordActivity

    @ContributesAndroidInjector
    abstract  fun bindCreateAccountActivity() : CreateAccountActivity

    @ContributesAndroidInjector(modules = [CouponModule::class])
    abstract  fun bindAppliedCoupenActivity() : AppliedCoupenActivity

    @ContributesAndroidInjector
    abstract  fun bindOrderConfirmActivity() : OrderConfirmActivity

    @ContributesAndroidInjector(modules = [OrderHistoryModule::class])
    abstract  fun bindOrderHistoryActivity() : OrderHistoryActivity
    @ContributesAndroidInjector(modules = [ChangeAddressModule::class])
    abstract  fun bindChangeAddressActivity() : ChangeAddressActivity
    @ContributesAndroidInjector
    abstract  fun bindAddWebPayment() : WebPayment
    @ContributesAndroidInjector
    abstract  fun bindEditAddressActivity() : EditAddressActivity
    @ContributesAndroidInjector
    abstract  fun bindScheduleOrderActivity() : ScheduleOrderActivity
    @ContributesAndroidInjector(modules = [FeedbackModule::class])
    abstract  fun bindFeedbackActivity() : FeedbackActivity

}