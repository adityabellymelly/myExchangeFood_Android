package com.exchange.user.module.cart_module.model.data_model

import com.exchange.user.module.utility_module.BuilderManager

class CartDataViewModel internal constructor(country: String) {
        private var country = "US"

       init {
            this.country = country
        }

        fun getFive(): String {
            return if (country.equals("IN", ignoreCase = true))
                BuilderManager.getFormat().format(5)
            else
                5.toString() + "%"
        }

        fun getTen(): String {
            return if (country.equals("IN", ignoreCase = true))
                BuilderManager.getFormat().format(10)
            else
                10.toString() + "%"
        }

        fun getFifteen(): String {
            return if (country.equals("IN", ignoreCase = true))
                BuilderManager.getFormat().format(15)
            else
                15.toString() + "%"
        }

        fun getTwenty(): String {
            return if (country.equals("IN", ignoreCase = true))
                BuilderManager.getFormat().format(20)
            else
                20.toString() + "%"
        }

        fun getTwentyfive(): String {
            return if (country.equals("IN", ignoreCase = true))
                BuilderManager.getFormat().format(25)
            else
                25.toString() + "%"
        }
}