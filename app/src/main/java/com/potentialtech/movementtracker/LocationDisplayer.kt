package com.potentialtech.movementtracker

import android.location.Location
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class LocationDisplayer(activityIn: AppCompatActivity, tvIn: TextView): LocationReceiver {

    private var textView: TextView = tvIn
    private var activity: AppCompatActivity = activityIn

    override fun receive(lastLocation: Location) {
        activity.runOnUiThread(object : Thread() {
            override fun run() {
                if (lastLocation == null) {
                    Log.d(LOG_TAG,"Null location")
                } else {
                    val alt = lastLocation.altitude
                    val lat = lastLocation.latitude
                    val lon = lastLocation.longitude
                    val accuracy = lastLocation.accuracy
                    val gpsTime = lastLocation.time
                    val t = Calendar.getInstance().time
                    textView.text = "Lat: "+lat+"\nLon: "+lon+"\nAlt: "+alt+"\nAccuracy: "+accuracy+"\nGPS Time: "+gpsTime+"\nTime: "+t.toString()
                }
            }
        })
    }
}