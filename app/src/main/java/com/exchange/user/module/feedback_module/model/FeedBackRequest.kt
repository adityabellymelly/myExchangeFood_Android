package com.exchange.user.module.feedback_module.model

import com.google.gson.annotations.SerializedName

class FeedBackRequest {
    @SerializedName("tid")
    var tid: String? = null
    @SerializedName("data")
    var data: FeedBackData? = null
}