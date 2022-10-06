package com.exchange.user.module.web_module

import com.exchange.user.module.base_module.ConstantCommand
import com.exchange.user.module.base_module.base_model.PostData
import com.exchange.user.module.base_module.base_model.ResponceModel
import com.exchange.user.module.base_module.base_model.ResponceModelSecond
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiInterface3 {

    @POST(ConstantCommand.GET_DELIVERY_STATUS)
    fun getOrderDeliveryStatusForCustomer(@Body postData : PostData): Single<ResponceModelSecond>
    @POST(ConstantCommand.SEND_SERVICE_FEEDBACK)
    fun sendServiceFeedback(@Body postData : PostData): Single<ResponceModelSecond>
}