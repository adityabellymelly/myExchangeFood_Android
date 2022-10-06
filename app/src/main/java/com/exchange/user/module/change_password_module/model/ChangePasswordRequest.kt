package com.exchange.user.module.change_password_module.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ChangePasswordRequest {
    @SerializedName("tId")
    var tId: String = ""
    @SerializedName("data")
    var dataPassword : PasswordData? = null

    class PasswordData : Serializable {
        @SerializedName("username")
        var username: String? = null
        @SerializedName("password")
        var password: String? = null
        @SerializedName("oldpassword")
        var oldpassword: String? = null
    }
}