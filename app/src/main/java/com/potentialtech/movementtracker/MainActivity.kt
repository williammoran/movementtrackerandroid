package com.potentialtech.movementtracker

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import java.util.*

const val REQUEST_CODE = 8
const val TAG = "UI updater"

class MainActivity : AppCompatActivity() {

    private lateinit var locationCallback: LocationCallback
    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initLocationCollection()
    }

    private fun initLocationCollection() {
        val hasPermission = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (hasPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                Array<String?>(1){Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_CODE
            )
        } else {
            startLocationCollection()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE) {
            for ((index, value) in permissions.withIndex()) {
                if (value == Manifest.permission.ACCESS_FINE_LOCATION) {
                    if (grantResults[index] == PackageManager.PERMISSION_GRANTED) {
                        startLocationCollection()
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationCollection() {
        textView = findViewById<TextView>(R.id.textPosition)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations){
                    displayLocation(location)
                }
            }
        }
        // Seed things with the last known location
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation.addOnSuccessListener {
            lastLocation: Location? -> displayLocation(lastLocation)
        }
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 1000
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    fun displayLocation(lastLocation: Location?) {
        runOnUiThread(object : Thread() {
            override fun run() {
                if (lastLocation == null) {
                    Log.d(TAG,"Null location")
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