package com.exchange.user.module.utility_module.dialog_commands

import com.exchange.user.module.order_info_module.model.DeliveryStatus
import com.exchange.user.module.order_info_module.model.OrderInfoModel
import com.exchange.user.module.profile_module.model.OrderHistory

interface CallApi {

    fun callFunction(orderHistory: OrderHistory, trackOrderCallBack :TrackOrderCallBack)
    fun trackHistory(orderHistory: OrderHistory,
                     orderInfoResponse: OrderInfoModel,
                     deliveryStatus: DeliveryStatus?)
}