package com.potentialtech.movementtracker

import android.location.Location

interface LocationReceiver {
    fun receive(location: Location)
}