package com.exchange.user.module.utility_module.dialog_commands

import com.exchange.user.module.country_module.model.location_model.RegionList

interface LocationDailogListner {

    fun onSelectLocation(region: RegionList)
}