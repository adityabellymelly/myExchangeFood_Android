package com.exchange.user.di_module.modules.fragment_module.cart_module

import com.exchange.user.module.cart_module.CartFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class CartFragmentProvider {

    @ContributesAndroidInjector(modules = arrayOf(CartFragmentModule::class))
    abstract fun provideCartFragmentFactory() : CartFragment
}