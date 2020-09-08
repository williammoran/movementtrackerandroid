package com.potentialtech.movementtracker

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.*

class FusedGatherer(activityIn: AppCompatActivity): LocationGatherer {

    private lateinit var receiver: LocationReceiver
    private var activity: AppCompatActivity = activityIn

    override fun registerReceiver(r: LocationReceiver) {
        this.receiver = r
    }

    @SuppressLint("MissingPermission")
    override fun start() {
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations){
                    receiver.receive(location)
                }
            }
        }
        // Seed things with the last known location
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
        fusedLocationClient.lastLocation.addOnSuccessListener {
                lastLocation: Location? -> if (lastLocation != null) receiver.receive(lastLocation)
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
}