package com.exchange.user.di_module.modules.activity_module.order_history_module

import com.exchange.user.module.order_history.OrderHistoryActivity
import com.exchange.user.module.order_history.adapter.OrderHistoryListAdapter
import dagger.Module
import dagger.Provides
@Module
class OrderHistoryModule {
    @Provides
    fun provideCoupenListAdapter(fragment : OrderHistoryActivity): OrderHistoryListAdapter {
        return OrderHistoryListAdapter(fragment,ArrayList())
    }
}