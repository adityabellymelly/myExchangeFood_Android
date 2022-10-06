package com.exchange.user.di_module.modules.fragment_module.aboutUs_module

import androidx.recyclerview.widget.LinearLayoutManager
import com.exchange.user.module.about_us_module.AboutUsFragment
import dagger.Module
import dagger.Provides

@Module
class AboutUsFragmentModule {
    @Provides
    fun provideLinearLayoutManager(fragment : AboutUsFragment): LinearLayoutManager {
        return LinearLayoutManager(fragment.activity)
    }
}