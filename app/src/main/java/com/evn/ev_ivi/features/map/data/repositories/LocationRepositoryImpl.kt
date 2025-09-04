package com.evn.ev_ivi.features.map.data.repositories

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.evn.ev_ivi.features.map.domain.repositories.LocationRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class LocationRepositoryImpl(
    private val context: Context,
    private val locationClient: FusedLocationProviderClient
): LocationRepository {
    override fun getLocationUpdates(): Flow<Location> {
        return callbackFlow {
            val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000L)
                .setMinUpdateIntervalMillis(2000L)
                .build()

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    locationResult.lastLocation?.let {
                        Log.d("LocationRepositoryImpl", "onLocationResult: ${it.latitude}, ${it.longitude}")
                        trySend(it)
                    }
                }
            }

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
            }

            awaitClose {
                locationClient.removeLocationUpdates(locationCallback)
            }
        }
    }
}