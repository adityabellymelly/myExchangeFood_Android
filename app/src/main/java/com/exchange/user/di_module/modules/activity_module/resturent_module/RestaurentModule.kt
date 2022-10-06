package com.exchange.user.di_module.modules.activity_module.resturent_module

import androidx.recyclerview.widget.LinearLayoutManager
import com.exchange.user.module.country_module.CountryActivity
import com.exchange.user.module.restaurent_module.ResturantActivity
import com.exchange.user.module.restaurent_module.adapter.AboutPaymentAdapter
import com.exchange.user.module.restaurent_module.adapter.AboutServiceAdapter
import dagger.Module
import dagger.Provides

@Module
class RestaurentModule {
    @Provides
    fun provideLinearLayoutManager(fragment : ResturantActivity): LinearLayoutManager {
        return LinearLayoutManager(fragment)
    }

    @Provides
    fun provideAboutPaymentAdapter(fragment :ResturantActivity): AboutPaymentAdapter {
        return AboutPaymentAdapter(fragment,ArrayList())
    }

    @Provides
    fun provideAboutServiceAdapter(fragment :ResturantActivity): AboutServiceAdapter {
        return AboutServiceAdapter(fragment,ArrayList())
    }


}