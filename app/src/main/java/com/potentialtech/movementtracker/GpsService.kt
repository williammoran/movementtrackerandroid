package com.potentialtech.movementtracker

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices

const val LOG_TAG = "service_logging"
const val CHANNEL_ID = "com.potentialtech.movementtracker"

class GpsService: Service() {

    private lateinit var filename: String
    private lateinit var fileRecorder: FileRecorder
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val filename = intent.getStringExtra(TRACK_FILENAME)
        if (filename != null) {
            val notificationIntent = Intent(this, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                this,
                0, notificationIntent, 0
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel()
            }
            val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(getText(R.string.recording_title))
                .setContentText(getText(R.string.recording_message))
                .setContentIntent(pendingIntent)
                .build()
            startForeground(1, notification)
            this.filename = filename
            fileRecorder = FileRecorder(filename, applicationContext)
            start()
        }
        return START_NOT_STICKY
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val chan = NotificationChannel(
            CHANNEL_ID,
            getText(R.string.recording_channel_name),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
    }

    @SuppressLint("MissingPermission")
    fun start() {
        // Seed things with the last known location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation.addOnSuccessListener {
            lastLocation: Location? -> if (lastLocation != null) fileRecorder.onLocationChanged(
                lastLocation
            )
        }
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 1000
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            fileRecorder,
            Looper.getMainLooper()
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient.removeLocationUpdates(fileRecorder)
        fileRecorder.close()
        stopForeground(true)
    }

    override fun onCreate() {
        super.onCreate()
    }

}