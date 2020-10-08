package com.potentialtech.movementtracker

import android.content.Context
import android.location.Location
import android.util.Log
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import java.io.FileOutputStream

class FileRecorder(destFilename: String, contextIn: Context) : LocationCallback() {

    private var context = contextIn
    private var dest: FileOutputStream = context.openFileOutput(destFilename, Context.MODE_APPEND)

    override fun onLocationResult(locationResult: LocationResult?) {
        Log.d(LOG_TAG, "onLocationResult()")
        locationResult ?: return
        for (location in locationResult.locations){
            onLocationChanged(location)
        }
    }

    fun onLocationChanged(location: Location) {
        Log.d(LOG_TAG, "onLocationChanged()")
        // Record structure:
        // A,LONG,LAT,ELEV,ACC,TS
        val string = StringBuilder("A,")
        string.append(location.longitude).append(",").append(location.latitude).append(",")
        string.append(location.altitude).append(",").append(location.accuracy).append(",")
        string.append(location.time).append("\n")
        dest.write(string.toString().toByteArray())
    }

    fun close() {
        dest.close()
    }

}