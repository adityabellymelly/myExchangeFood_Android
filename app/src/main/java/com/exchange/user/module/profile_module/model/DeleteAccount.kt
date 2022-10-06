package com.exchange.user.module.profile_module.model

import com.google.gson.annotations.SerializedName

class DeleteAccount {
    @SerializedName("tId")
    var tId: String? = null
    @SerializedName("data")
    var data: DeleteData? = null
}

class DeleteData{
    @SerializedName("chainid")
    var chainid: Long? = null
    @SerializedName("UserId")
    var UserId: String? = null

}