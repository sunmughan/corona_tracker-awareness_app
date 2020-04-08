package com.quarantinealert.util.location

import java.lang.Exception

interface OnGetLocationFailed {
    fun onGetLocationFailed(exception: Exception)
}