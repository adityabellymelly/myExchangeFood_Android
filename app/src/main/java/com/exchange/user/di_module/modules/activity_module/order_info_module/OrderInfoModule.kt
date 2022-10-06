package com.exchange.user.di_module.modules.activity_module.order_info_module

import androidx.recyclerview.widget.LinearLayoutManager
import com.exchange.user.module.order_info_module.OrderInfoActivity
import com.exchange.user.module.order_info_module.adapter.OrderInfoItemsAdapter
import dagger.Module
import dagger.Provides

@Module
class OrderInfoModule {
    @Provides
    fun provideLinearLayoutManager(fragment : OrderInfoActivity): LinearLayoutManager {
        return LinearLayoutManager(fragment)
    }
    @Provides
    fun provideOrderInfoItemsAdapter(fragment :OrderInfoActivity): OrderInfoItemsAdapter {
        return OrderInfoItemsAdapter(fragment,ArrayList())
    }


}