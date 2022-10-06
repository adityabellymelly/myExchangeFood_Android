package com.exchange.user.di_module.modules.fragment_module.cart_module

import androidx.recyclerview.widget.LinearLayoutManager
import com.exchange.user.module.cart_module.CartFragment
import com.exchange.user.module.cart_module.adapter.PaymentAdapter
import com.exchange.user.module.cart_module.adapter.ServiceAdapter
import com.exchange.user.module.cart_module.model.action.adapter.CartSelectedAdapter
import com.exchange.user.module.home_module.home_fragment.adapter.RestaruentAdapter
import dagger.Module
import dagger.Provides

@Module
class CartFragmentModule {
    @Provides
    fun provideLinearLayoutManager(fragment : CartFragment): LinearLayoutManager {
        return LinearLayoutManager(fragment.activity)
    }

    @Provides
    fun provideRestaruentAdapter(fragment : CartFragment): CartSelectedAdapter {
        return CartSelectedAdapter(fragment.requireContext(),ArrayList())
    }

    @Provides
    fun provideServiceAdapter(fragment : CartFragment): ServiceAdapter {
        return ServiceAdapter(fragment.requireContext(),ArrayList())
    }

    @Provides
    fun providePaymentAdapter(fragment : CartFragment): PaymentAdapter {
        return PaymentAdapter(fragment.requireContext(),ArrayList())
    }

}