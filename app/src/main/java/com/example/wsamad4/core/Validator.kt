package com.example.wsamad4.core

import java.util.regex.Pattern

object Validator {
    fun validateEmail(s:String?): Boolean {
        val regex = Pattern.compile("^([a-zA-Z]{1,}@wsa[.]com)")
        return if (s.isNullOrEmpty()) {
            return false
        } else if (!regex.matcher(s).matches()) {
            return false
        } else {
            true
        }
    }
     fun validatePassword(s:String?): Boolean {
        return if (s.isNullOrEmpty()) {
            return false
        } else {
            true
        }
    }
}