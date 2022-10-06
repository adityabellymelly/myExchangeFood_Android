package com.exchange.user.module.restaurent_module.model.data_model.reastaurent_mode

import com.google.gson.annotations.SerializedName

class Service {

    @SerializedName("Available")
    var available: String = "T"
    @SerializedName("DeliveryZones")
    var deliveryZones: ArrayList<DeliveryZones> ? = null
    @SerializedName("DynamicFee")
    var dynamicFee: String? = null
    @SerializedName("FlatFee1")
    var flatFee1:  Double = 0.0
    @SerializedName("FlatFee2")
    var flatFee2:  Double = 0.0
    @SerializedName("FlatFee3")
    var flatFee3: Double = 0.0
    @SerializedName("Id")
    var id: Long? = null
    @SerializedName("MaxOrder")
    var maxOrder: String? = null
    @SerializedName("MinCharges")
    var minCharges: Double = 0.0
    @SerializedName("MinFee")
    var minFee: Double =0.0
    @SerializedName("MinFlatFee")
    var minFlatFee: Double = 0.0
    @SerializedName("MinOrder")
    var minOrder: Double = 0.0
    @SerializedName("MinVarFee")
    var minVarFee: Double = 0.0
    @SerializedName("Name")
    var name: String? = null
    @SerializedName("OpenOn")
    var openOn: OpenOn? = null
    @SerializedName("OrderAmt1")
    var orderAmt1: Double = 999.99
    @SerializedName("OrderAmt2")
    var orderAmt2:Double = 999.99
    @SerializedName("OrderAmt3")
    var orderAmt3:Double = 999.99
    @SerializedName("Payment")
    var payment: List<Payment>? = null
    @SerializedName("ReadyIn")
    var readyIn: String? = null
    @SerializedName("ReadyInMin")
    var readyInMin: String? = null
    @SerializedName("ServiceId")
    var serviceId: Long? = null
    @SerializedName("ServiceName")
    var serviceName: String? = null
    @SerializedName("VarFee1")
    var varFee1: Double = 0.0
    @SerializedName("VarFee2")
    var varFee2:  Double = 0.0
    @SerializedName("VarFee3")
    var varFee3:  Double = 0.0
    var isSelected :Boolean = false

}
