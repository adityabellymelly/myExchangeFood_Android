package com.exchange.user.module.web_module

import com.exchange.user.module.about_us_module.model.DeliveryProgramResponse
import com.exchange.user.module.country_module.model.CountryModel
import com.exchange.user.module.country_module.model.location_model.LocationModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {
    @GET("js/country/{location}")
    fun location(@Path("location")location: String,
                 @Query("v") version: String): Single<LocationModel>

    @GET("js/{country}")
    fun country(@Path("country")location: String): Single<CountryModel>

    @GET("js/{deliveryProgram}")
    fun aboutUs(@Path("deliveryProgram")deliveryProgram: String): Single<DeliveryProgramResponse>

//    https://aafesprem.imenu360.com/js/countrylist.json
}