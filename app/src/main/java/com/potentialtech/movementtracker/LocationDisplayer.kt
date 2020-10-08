package com.potentialtech.movementtracker

import android.location.Location
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class LocationDisplayer(activityIn: AppCompatActivity, tvIn: TextView):
    com.google.android.gms.location.LocationListener {

    private var textView: TextView = tvIn
    private var activity: AppCompatActivity = activityIn

    override fun onLocationChanged(lastLocation: Location) {
        activity.runOnUiThread(object : Thread() {
            override fun run() {
                val alt = lastLocation.altitude
                val lat = lastLocation.latitude
                val lon = lastLocation.longitude
                val accuracy = lastLocation.accuracy
                val gpsTime = lastLocation.time
                val t = Calendar.getInstance().time
                textView.text = "Lat: "+lat+"\nLon: "+lon+"\nAlt: "+alt+"\nAccuracy: "+accuracy+"\nGPS Time: "+gpsTime+"\nTime: "+t.toString()
            }
        })
    }
}