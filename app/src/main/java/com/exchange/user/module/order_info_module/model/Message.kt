package com.exchange.user.module.order_info_module.model

import com.google.gson.annotations.SerializedName
import java.util.*

class Message {
    @SerializedName("MsgText")
    var msgText:String = ""
    @SerializedName("CreatedOn")
    var createdOn:String = ""

    var  dateValue : Date?=null
}