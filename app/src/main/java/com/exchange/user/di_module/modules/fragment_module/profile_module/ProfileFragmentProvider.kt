package com.exchange.user.di_module.modules.fragment_module.profile_module

import com.exchange.user.module.profile_module.ProfileFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ProfileFragmentProvider {

    @ContributesAndroidInjector(modules = arrayOf(ProfileFragmentModule::class))
    abstract fun provideHomeFragmentFactory() : ProfileFragment
}