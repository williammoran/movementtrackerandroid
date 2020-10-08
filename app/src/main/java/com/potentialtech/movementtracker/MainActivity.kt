package com.potentialtech.movementtracker

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.util.*

const val REQUEST_CODE = 8
const val TRACK_FILENAME = "com.potentialtech.movementtracker::TRACK_FILENAME"

class MainActivity : AppCompatActivity() {

    private lateinit var textAreaPosition: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textAreaPosition = findViewById<TextView>(R.id.textPosition)
        initLocationCollection()
    }

    fun onClickStart(v: View) {
        v.isEnabled = false
        val filename = "Track_" + Calendar.getInstance().time.time.toString() + ".csv"
        val recordIntent = Intent(this, GpsService::class.java)
        recordIntent.putExtra(TRACK_FILENAME, filename)
        startService(recordIntent)
        findViewById<View>(R.id.button_stop_recording).isEnabled = true
    }

    fun onClickStop(v: View) {
        v.isEnabled = false
        val stopRecordIntent = Intent(this, GpsService::class.java)
        stopService(stopRecordIntent)
        findViewById<View>(R.id.button_start_recording).isEnabled = true
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
        val displayer = LocationDisplayer(this, textAreaPosition)
        val fusedGatherer = FusedGatherer(this, displayer)
        fusedGatherer.start()
    }
}
