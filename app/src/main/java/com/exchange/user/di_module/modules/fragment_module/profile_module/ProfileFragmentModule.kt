package com.exchange.user.di_module.modules.fragment_module.profile_module

import androidx.recyclerview.widget.LinearLayoutManager
import com.exchange.user.module.profile_module.ProfileFragment
import dagger.Module
import dagger.Provides

@Module
class ProfileFragmentModule {
    @Provides
    fun provideLinearLayoutManager(fragment : ProfileFragment): LinearLayoutManager {
        return LinearLayoutManager(fragment.activity)
    }

}