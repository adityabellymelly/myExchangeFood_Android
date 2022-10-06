package com.exchange.user.di_module.modules

import com.exchange.user.module.base_module.ConstantCommand
import com.exchange.user.module.web_module.ApiInterface
import com.exchange.user.module.web_module.ApiInterface2
import com.exchange.user.module.web_module.ApiInterface3
import com.exchange.user.module.web_module.ApplicationRequest
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class RetrofitModule {
    @Provides
    @Singleton
    fun provideApiInterface(): ApiInterface {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val gson = GsonBuilder().setLenient().create()
        val client = OkHttpClient.Builder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS)
            .addInterceptor(interceptor).build()
        return Retrofit.Builder().baseUrl(ConstantCommand.BASE_URL_COUNTRY)
            .addConverterFactory(GsonConverterFactory.create(gson)).client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(ApiInterface::class.java)
    }


    @Provides
    @Singleton
    fun provideApiInterface2(): ApiInterface2 {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val gson = GsonBuilder().setLenient().create()
        val client = OkHttpClient.Builder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS)
            .addInterceptor(interceptor).build()
        return Retrofit.Builder().baseUrl(ConstantCommand.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson)).client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(ApiInterface2::class.java)
    }
    @Provides
    @Singleton
    fun provideApiInterface3(): ApiInterface3 {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val goon = GsonBuilder().setLenient().create()
        val client = OkHttpClient.Builder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS)
            .addInterceptor(interceptor).build()
        return Retrofit.Builder().baseUrl(ConstantCommand.BASE_DRIVER_URL)
            .addConverterFactory(GsonConverterFactory.create(goon)).client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(ApiInterface3::class.java)
    }


    @Provides
    @Singleton
    fun provideApplicationRequest(api :ApiInterface,
                                  api2: ApiInterface2,
                                  api3: ApiInterface3): ApplicationRequest {
        return ApplicationRequest(api,api2,api3)
    }
}