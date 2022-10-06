package com.exchange.user.module.restaurent_module.model.data_model.reastaurent_mode

import com.google.gson.annotations.SerializedName

class DeliveryZones {
    @SerializedName("DeliveryZoneId")
    var deliveryZoneId: Long? = null
    @SerializedName("FillColor")
    var fillColor: String? = null
    @SerializedName("FixedCharge")
    var fixedCharge: Double = 0.0
    @SerializedName("LineColor")
    var lineColor: String? = null
    @SerializedName("PointString")
    var pointString: String? = null
    @SerializedName("ZoneName")
    var zoneName: String? = null
}