package com.potentialtech.movementtracker

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

const val REQUEST_CODE = 8
const val TAG = "UI updater"

class MainActivity : AppCompatActivity() {

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

    private fun startLocationCollection() {
        val textView = findViewById<TextView>(R.id.textPosition)
        val displayer = LocationDisplayer(this, textView)
        val gatherer = FusedGatherer(this)
        gatherer.registerReceiver(displayer)
        gatherer.start()
    }
}