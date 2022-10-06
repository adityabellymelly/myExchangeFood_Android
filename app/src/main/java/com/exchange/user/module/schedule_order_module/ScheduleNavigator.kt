package com.exchange.user.module.schedule_order_module

import com.exchange.user.module.base_module.base_view.CommonActivityOrFragmentView

interface ScheduleNavigator : CommonActivityOrFragmentView {

    fun onASAP()
    fun onTodayLate()
    fun onFuture()
    fun onSelectTime()
    fun onBack()
}