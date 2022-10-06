package com.exchange.user.rx_module

import io.reactivex.Scheduler

interface SchedulerProvider {
    fun computation() : Scheduler

    fun io() : Scheduler

    fun ui() : Scheduler
}