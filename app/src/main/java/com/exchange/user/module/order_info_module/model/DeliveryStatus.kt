package com.exchange.user.module.order_info_module.model

import com.google.gson.annotations.SerializedName
import java.util.*
import kotlin.collections.ArrayList

class DeliveryStatus {
    var isDriverEnroute: String? = null
    var EstimatedDeliveryTime: String? = "20"
    var isDelivered: String? = null
    @SerializedName("DriverName")
    var driverName: String? = null


    var driverPhone: String? = null
    var driverEmail: String? = null
    var DriverBioInfo: String = ""

    var OrderDelName: String = ""
    var OrderDelAddr1: String = ""
    var OrderDelAddr2: String = ""
    var OrderDelCity: String = ""
    var OrderDelState: String = ""
    var OrderDelZip: String = ""
    var OrderDelPhone: String = ""

    @SerializedName("DriverProfilePic")
    var driverProfilePic : String? = null
    var QuestionList: ArrayList<QuestionList> = ArrayList()
    var isOrderConfirmed:String = "0"
}