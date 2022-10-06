package com.exchange.user.di_module.modules

import android.app.Application
import android.content.Context
import com.exchange.user.module.utility_module.ApplicationSharePrefrence
import com.exchange.user.module.utility_module.ApplicationSharedPref
import com.exchange.user.module.utility_module.ApplicationUtility
import com.exchange.user.module.utility_module.Validations
import com.exchange.user.rx_module.AppSchedulerProvider
import com.exchange.user.rx_module.SchedulerProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class UtilModule {
    @Provides
    @Singleton
    fun provideContext(application : Application): Context {
        return application
    }

    @Provides
    fun provideSchedulerProvider() : SchedulerProvider {
        return AppSchedulerProvider()
    }

    @Provides
    @Singleton
    fun provideApplicationUtility(applicatsharepref : Application): ApplicationUtility {
        return ApplicationUtility(applicatsharepref)
    }
    @Provides
    @Singleton
    fun provideValidations() : Validations {
        return Validations()
    }

    @Provides
    @Singleton
    fun provideApplicationSharedPref(applicatsharepref : ApplicationSharedPref): ApplicationSharePrefrence {
        return applicatsharepref
    }


}