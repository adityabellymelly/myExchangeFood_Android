package com.exchange.user.module.utility_module

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher

class FourDigitCardFormatWatcher : TextWatcher {

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

    override fun afterTextChanged(s: Editable) {
        // Remove spacing char
        if (s.isNotEmpty() && s.length % 5 == 0) {
            val c = s[s.length - 1]
            if (space == c) {
                s.delete(s.length - 1, s.length)
            }
        }
        // Insert char where needed.
        if (s.isNotEmpty() && s.length % 5 == 0) {
            val c = s[s.length - 1]
            // Only if its a digit where there should be a space we insert a space
            if (Character.isDigit(c) && TextUtils.split(s.toString(), space.toString()).size <= 3) {
                s.insert(s.length - 1, space.toString())
            }
        }
    }

    companion object {
        // Change this to what you want... ' ', '-' etc..
        const val space = ' '

        fun getCardWithSpaces(cardNumber: String): String {
            val builder = StringBuilder()
            for (i in cardNumber.indices) {
                builder.append(cardNumber[i])
                // Insert char where needed.
                if (i > 0 && (i == 3 || i == 7 || i == 11)) {
                    builder.append(space)
                }
            }
            return builder.toString()
        }
    }
}