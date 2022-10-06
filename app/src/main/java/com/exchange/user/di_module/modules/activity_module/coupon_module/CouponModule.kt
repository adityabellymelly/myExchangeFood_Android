package com.exchange.user.di_module.modules.activity_module.coupon_module

import com.exchange.user.module.coupon_module.AppliedCoupenActivity
import com.exchange.user.module.coupon_module.adapter.CoupenListAdapter
import com.exchange.user.module.coupon_module.adapter.DiscountListAdapter
import dagger.Module
import dagger.Provides
@Module
class CouponModule {
    @Provides
    fun provideCoupenListAdapter(fragment :AppliedCoupenActivity): CoupenListAdapter {
        return CoupenListAdapter(fragment,ArrayList())
    }

    @Provides
    fun provideDiscountListAdapter(fragment :AppliedCoupenActivity): DiscountListAdapter {
        return DiscountListAdapter(fragment,ArrayList())
    }


}