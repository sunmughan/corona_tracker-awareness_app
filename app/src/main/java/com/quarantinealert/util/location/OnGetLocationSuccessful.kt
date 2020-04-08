package com.quarantinealert.util.location

import android.location.Location

interface OnGetLocationSuccessful {
    fun onGetLocationSuccessful(location: Location)
}