package com.exchange.user.di_module.modules.fragment_module.aboutUs_module

import com.exchange.user.module.about_us_module.AboutUsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AboutUsFragmentProvider{
    @ContributesAndroidInjector(modules = arrayOf(AboutUsFragmentModule::class))
    abstract fun provideSearchFragmentFactory() : AboutUsFragment
}