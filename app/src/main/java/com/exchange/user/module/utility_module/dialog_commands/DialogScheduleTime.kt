package com.exchange.user.module.utility_module.dialog_commands

import com.exchange.user.module.restaurent_module.model.data_model.reastaurent_mode.Service

interface DialogScheduleTime {
    fun onScheduleTimeString(service: Service)
}