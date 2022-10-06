package com.exchange.user.di_module.component

import android.app.Application
import com.exchange.user.application.ExchangeApp
import com.exchange.user.di_module.modules.ActivityBuilder
import com.exchange.user.di_module.modules.RetrofitModule
import com.exchange.user.di_module.modules.UtilModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@SuppressWarnings("unchecked")
@Suppress("UNCHECKED_CAST")
@Singleton
@Component(modules = [AndroidInjectionModule::class,
    ActivityBuilder::class, RetrofitModule::class,
    UtilModule::class])
interface ApplicationComponent {

    @SuppressWarnings("unchecked")
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): ApplicationComponent
    }
    fun inject(instance: ExchangeApp)
}