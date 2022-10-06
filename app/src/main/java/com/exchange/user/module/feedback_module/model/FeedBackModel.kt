package com.exchange.user.module.feedback_module.model

import com.google.gson.annotations.SerializedName

class FeedBackModel {
    @SerializedName("Answer1")
    var answer1: String = "0"
    @SerializedName("Answer10")
    var answer10: String = "0"
    @SerializedName("Answer2")
    var answer2: String = "0"
    @SerializedName("Answer3")
    var answer3: String = "0"
    @SerializedName("Answer4")
    var answer4: String = "0"
    @SerializedName("Answer5")
    var answer5: String = "0"
    @SerializedName("Answer6")
    var answer6: String = "0"
    @SerializedName("Answer7")
    var answer7: String = "0"
    @SerializedName("Answer8")
    var answer8: String = "0"
    @SerializedName("Answer9")
    var answer9: String = "0"
    @SerializedName("comment")
    var comment: String? = null
    @SerializedName("CustomerEmail")
    var customerEmail: String? = null
    @SerializedName("CustomerName")
    var customerName: String? = null
    @SerializedName("CustomerTelephone")
    var customerTelephone: String? = null
    @SerializedName("OrderNumber")
    var orderNumber: String? = null
    @SerializedName("OrderTotal")
    var orderTotal: String? = null
    @SerializedName("PaymentMode")
    var paymentMode: String? = null
    @SerializedName("RestLocationId")
    var restLocationId: String? = null
    @SerializedName("RestaurantAddress")
    var restaurantAddress: String? = null
    @SerializedName("RestaurantName")
    var restaurantName: String? = null
    @SerializedName("RestaurantTelephone")
    var restaurantTelephone: String? = null
}