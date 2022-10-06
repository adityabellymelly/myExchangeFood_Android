package com.exchange.user.di_module.modules.activity_module.change_address_module

import com.exchange.user.module.change_address_module.ChangeAddressActivity
import com.exchange.user.module.change_address_module.adapter.AddressListAdapter
import dagger.Module
import dagger.Provides
@Module
class ChangeAddressModule {
    @Provides
    fun provideCoupenListAdapter(fragment : ChangeAddressActivity): AddressListAdapter {
        return AddressListAdapter(fragment,ArrayList())
    }
}