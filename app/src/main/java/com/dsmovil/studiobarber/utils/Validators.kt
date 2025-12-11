package com.dsmovil.studiobarber.utils

import java.util.regex.Pattern

private val EMAIL_ADDRESS_PATTERN = Pattern.compile(
    "^[a-zA-Z0-9+_%\\-]+(\\.[a-zA-Z0-9+_%\\-]+)*" +
            "@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+$"
)

fun isValidEmail(email: String): Boolean {
    if (email.isEmpty()) {
        return false
    }

    return EMAIL_ADDRESS_PATTERN.matcher(email).matches()
}