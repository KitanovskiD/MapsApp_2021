package com.example.servisesapp

import android.Manifest
import android.app.Service
import android.content.ContentProviderClient
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.*

class LocationService: Service() {

//    val handler = Handler()
//
//    val runnable = object: Runnable {
//        override fun run() {
//            handler.postDelayed(this, 5000)
//
//            Log.d("LocationService", "HI!")
//        }
//    }

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback

    override fun onCreate() {
        super.onCreate()

        initMapsServices()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        handler.postDelayed(runnable, 5000)
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    private fun initMapsServices() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        locationRequest = LocationRequest.create()

        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        locationRequest.interval = 3000

        locationCallback = object: LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)

                if (locationResult == null) {
                    return
                }

                for (location in locationResult.locations) {
                    sendLocationUpdate(location)
                }
            }
        }
    }

    private fun sendLocationUpdate(location: Location) {
        val intent = Intent("location_event")

        intent.putExtra("Lat", location.latitude)
        intent.putExtra("Lng", location.longitude)

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }
}