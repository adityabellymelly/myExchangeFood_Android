package com.exchange.user.di_module.modules.fragment_module.orderhistory_module
import com.exchange.user.module.order_history.order_history_fragment.OrderHistoryFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class OrderHistoryFragmentProvider {
    @ContributesAndroidInjector(modules = arrayOf(OrderHistoryFragmentModule::class))
    abstract fun provideOrderHistoryFragmentFactory() : OrderHistoryFragment
}