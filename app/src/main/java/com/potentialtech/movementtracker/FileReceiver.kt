package com.potentialtech.movementtracker

import android.content.Context
import android.location.Location
import java.io.FileOutputStream

class FileReceiver(destFilename: String, contextIn: Context) : LocationReceiver {

    private var context = contextIn
    private var dest: FileOutputStream = context.openFileOutput(destFilename, Context.MODE_APPEND)

    override fun receive(location: Location) {
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