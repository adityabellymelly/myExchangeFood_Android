package com.exchange.user.module.order_info_module

import com.exchange.user.module.base_module.base_view.CommonActivityOrFragmentView
import com.exchange.user.module.order_info_module.model.DeliveryStatus
import com.exchange.user.module.order_info_module.model.OrderInfoModel
import com.exchange.user.module.restaurent_module.model.data_model.reastaurent_mode.RestrurentModel
import com.exchange.user.module.restaurent_module.model.menu_model.MenuModel

interface OrderInfoNavigator: CommonActivityOrFragmentView {
    fun onBack()
    fun onLocMobileUrl(mobileUrl: String)
    fun onLocation(resturentata: RestrurentModel)
    fun onOrderInfo(resturentata: OrderInfoModel)
    fun onOrderInfoTimer(resturentata: OrderInfoModel)
    fun onUpdateDeliveryStatus(delievryStatusData: DeliveryStatus)
    fun onUpdateDeliveryStatusTimer(delievryStatusData: DeliveryStatus)
    fun callToDriver()
    fun reOrder()
    fun getHelp()
    fun getEReceipt()
    fun trackingHistory()
    fun previewParcelImage()
}