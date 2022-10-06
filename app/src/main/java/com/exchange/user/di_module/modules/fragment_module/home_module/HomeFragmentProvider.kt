package com.exchange.user.di_module.modules.fragment_module.home_module

import com.exchange.user.module.home_module.home_fragment.HomeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class HomeFragmentProvider {

    @ContributesAndroidInjector(modules = arrayOf(HomeFragmentModule::class))
    abstract fun provideHomeFragmentFactory() : HomeFragment
}