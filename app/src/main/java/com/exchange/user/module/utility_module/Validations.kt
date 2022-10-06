package com.exchange.user.module.utility_module

import android.util.Patterns
import java.util.regex.Pattern
import javax.inject.Inject

class Validations
@Inject constructor(){

    private var EMAIL_PATTREN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
    private var EMAIL_ADDRESS_PATTREN = Pattern.compile(EMAIL_PATTREN)

    fun isValidEmail(email : String) : Boolean{
        return EMAIL_ADDRESS_PATTREN.matcher(email.trim()).matches()
    }

    fun isPhoneNumberValid(mobileno :String):Boolean{
        var phone = mobileno
        if (phone.startsWith("0"))
            phone = phone.substring(1)
        if (phone.startsWith("+1"))
            phone = phone.substring(2)
        if (phone.startsWith("+91"))
            phone = phone.substring(2)
        return phone.trim { it <= ' ' }.length in 10..10
    }

    fun isFieldEmpty(value : String) : Boolean {
        return value.isEmpty() || value.isBlank()
    }

    fun isValidPassword(value : String,length:Int) : Boolean {
        return value.length >= length
    }

    fun isEmailValid(email : String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun matchTwoStrings(string1: String, string2: String): Boolean {
        return string1 == string2
    }
}