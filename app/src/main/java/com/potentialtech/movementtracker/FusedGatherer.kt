package com.potentialtech.movementtracker

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.*

class FusedGatherer(activityIn: AppCompatActivity, rec: LocationListener) {

    private var receiver: LocationListener  = rec
    private var activity: AppCompatActivity = activityIn

    @SuppressLint("MissingPermission")
    fun start() {
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations){
                    receiver.onLocationChanged(location)
                }
            }
        }
        // Seed things with the last known location
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
        fusedLocationClient.lastLocation.addOnSuccessListener {
                lastLocation: Location? -> if (lastLocation != null) receiver.onLocationChanged(lastLocation)
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