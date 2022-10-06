package com.exchange.user.di_module.modules.activity_module.country_module

import androidx.recyclerview.widget.LinearLayoutManager
import com.exchange.user.module.country_module.CountryActivity
import com.exchange.user.module.country_module.adapter.CountyAdapter
import dagger.Module
import dagger.Provides

@Module
class CountyModule {

    @Provides
    fun provideLinearLayoutManager(fragment : CountryActivity): LinearLayoutManager {
        return LinearLayoutManager(fragment)
    }

    @Provides
    fun provideFeedAdapter(fragment : CountryActivity): CountyAdapter {
        return CountyAdapter(fragment,ArrayList())
    }
}