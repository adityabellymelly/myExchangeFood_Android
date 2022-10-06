package com.exchange.user.di_module.modules.fragment_module.orderhistory_module

import androidx.recyclerview.widget.LinearLayoutManager
import com.exchange.user.module.order_history.adapter.OrderHistoryListAdapter
import com.exchange.user.module.order_history.order_history_fragment.OrderHistoryFragment
import dagger.Module
import dagger.Provides

@Module
class OrderHistoryFragmentModule {
//
    @Provides
    fun provideLinearLayoutManager(fragment : OrderHistoryFragment): LinearLayoutManager {
        return LinearLayoutManager(fragment.activity)
    }

    @Provides
    fun provideRestaruentAdapter(fragment : OrderHistoryFragment): OrderHistoryListAdapter {
        return OrderHistoryListAdapter(fragment.requireContext(),ArrayList())
    }


}