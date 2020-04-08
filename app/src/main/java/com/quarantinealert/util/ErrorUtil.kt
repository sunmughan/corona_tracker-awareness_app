package com.quarantinealert.util

import android.content.Context
import com.crashlytics.android.Crashlytics
import com.quarantinealert.R
import com.quarantinealert.exception.NetworkException
import java.lang.Exception

fun getErrorMessage(context: Context, ex: Exception): String {
    return if (ex is NetworkException) {
        ex.message!!
    } else {
        Crashlytics.logException(ex)
        context.getString(R.string.default_error_message)
    }
}