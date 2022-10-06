package com.exchange.user.di_module.modules.fragment_module.home_module

import androidx.recyclerview.widget.LinearLayoutManager
import com.exchange.user.module.home_module.home_fragment.HomeFragment
import com.exchange.user.module.home_module.home_fragment.adapter.RestaruentAdapter
import dagger.Module
import dagger.Provides

@Module
class HomeFragmentModule {
    @Provides
    fun provideLinearLayoutManager(fragment : HomeFragment): LinearLayoutManager {
        return LinearLayoutManager(fragment.activity)
    }

    @Provides
    fun provideRestaruentAdapter(fragment : HomeFragment): RestaruentAdapter {
        return RestaruentAdapter(fragment.requireContext(),ArrayList())
    }



}