package com.exchange.user.module.order_info_module.model

import java.util.*
import kotlin.collections.ArrayList

class FeedbackQuestion {
    var QuestionName: String? = null
    var answers: ArrayList<Answer> = ArrayList()
    var DriverfeedbackCnt = "0"
}