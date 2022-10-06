package com.exchange.user.module.restaurent_module.model.api_model

import com.google.gson.annotations.SerializedName

class WLCDetails {

    @SerializedName("DefaultURL")
    var defaultURL: String? = null
    @SerializedName("ErrorURL")
    var errorURL: String? = null
    @SerializedName("InactiveURL")
    var inactiveURL: String? = null
    @SerializedName("mDefaultURL")
    var mDefaultURL: String? = null
    @SerializedName("mErrorURL")
    var mErrorURL: String? = null
    @SerializedName("mInactiveURL")
    var mInactiveURL: String? = null
    @SerializedName("mMaintenanceURL")
    var mMaintenanceURL: String? = null
    @SerializedName("MaintenanceURL")
    var maintenanceURL: String? = null
    @SerializedName("WlcId")
    var wlcId: String? = null

}
