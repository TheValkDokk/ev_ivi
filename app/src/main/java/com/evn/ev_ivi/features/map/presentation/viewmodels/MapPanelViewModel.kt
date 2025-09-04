package com.evn.ev_ivi.features.map.presentation.viewmodels

import android.app.Application
import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evn.ev_ivi.MainApplication
import com.evn.ev_ivi.features.map.domain.entities.MapLocation
import com.evn.ev_ivi.features.map.domain.usecases.GetLocationUpdatesUseCase
import com.kakaomobility.knsdk.KNLanguageType
import com.kakaomobility.knsdk.KNSDK
import com.kakaomobility.knsdk.common.gps.WGS84ToKATEC
import com.kakaomobility.knsdk.common.objects.KNPOI
import com.kakaomobility.knsdk.map.uicustomsupport.renewal.KNMapMarker
import com.kakaomobility.knsdk.trip.kntrip.KNTrip
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.internal.platform.PlatformRegistry.applicationContext

class MapPanelViewModel(
    private val getLocationUpdatesUseCase: GetLocationUpdatesUseCase
) : ViewModel() {
    private val _kakaoInit = MutableStateFlow(false)
    val kakaoInit = _kakaoInit.asStateFlow()

    val routeCustomObjectList = arrayListOf<KNMapMarker>()

    private val _locationState = MutableStateFlow<Location?>(null)
    val locationState: StateFlow<Location?> = _locationState.asStateFlow()

    private var locationJob: Job? = null

    private val _trip = MutableStateFlow<KNTrip?>(null)
    val trip = _trip.asStateFlow()

    private val _isFirstMapOpen = MutableStateFlow(true)
    val isFirstMapOpen = _isFirstMapOpen.asStateFlow()

    private val _currentUserMarker = MutableStateFlow<KNMapMarker?>(null)
    val currentUserMarker = _currentUserMarker.asStateFlow()

    private val _previousMarkerLocation = MutableStateFlow<Location?>(null)
    val previousMarkerLocation = _previousMarkerLocation.asStateFlow()

    init {
        MainApplication.knsdk = KNSDK.apply {
            val mainApplication = applicationContext as Application
            install(mainApplication, "${mainApplication.filesDir}")
            val appKey = "958d9749a2e1f35ef2982b0b2b1a09a5"
            initializeWithAppKey(
                appKey,
                "1.0.0",
                "test-user",
                KNLanguageType.KNLanguageType_KOREAN
            ) { error ->
                if (error == null) {
                    _kakaoInit.value = true
                }
            }
        }
    }

    fun createMapMarker(long: Double, lat: Double): KNMapMarker {
        val pos = WGS84ToKATEC(lat, long)
        return KNMapMarker(pos.toFloatPoint())
    }

    fun onNavigate(startLat: Double, startLong: Double, end: MapLocation) {
        val startLoc = MainApplication.knsdk.convertWGS84ToKATEC(
            startLong,
            startLat
        )
        val startPoi = KNPOI("current", startLoc.x.toInt(), startLoc.y.toInt(), "current")

        val goalLoc = MainApplication.knsdk.convertWGS84ToKATEC(
            end.lng,
            end.lat
        )
        val goalPoi = KNPOI("목적지", goalLoc.x.toInt(), goalLoc.y.toInt(), "목적지")

        MainApplication.knsdk.makeTripWithStart(startPoi, goalPoi, null) { error, trip ->
            if (error != null) {
                print("Error")
            } else {
                _trip.value = trip
            }
        }
    }

    fun startLocationUpdates() {
        locationJob?.cancel()
        locationJob = viewModelScope.launch {
            getLocationUpdatesUseCase().collect { location ->
                Log.d("LocationUpdates", "Received location: ${location.latitude} ${location.longitude}")
                _locationState.value = location
            }
        }
    }

    fun stopLocationUpdates() {
        locationJob?.cancel()
    }

    fun markFirstMapOpenComplete() {
        _isFirstMapOpen.value = false
    }

    fun setCurrentUserMarker(marker: KNMapMarker) {
        _currentUserMarker.value = marker
    }

    fun getCurrentUserMarker(): KNMapMarker? {
        return _currentUserMarker.value
    }

    fun clearCurrentUserMarker() {
        _currentUserMarker.value = null
    }

    private fun calculateDistance(loc1: Location, loc2: Location): Float {
        return loc1.distanceTo(loc2)
    }

    fun shouldUpdateMarker(newLocation: Location): Boolean {
        val previousLocation = _previousMarkerLocation.value
        return if (previousLocation == null) {
            Log.d("MapPanelViewModel", "First location, updating marker")
            true
        } else {
            val distance = calculateDistance(previousLocation, newLocation)
            Log.d("MapPanelViewModel", "Distance from previous location: ${distance}m")
            distance > 3.0f
        }
    }

    fun updatePreviousMarkerLocation(location: Location) {
        _previousMarkerLocation.value = location
    }

    fun removeCurrentUserMarker(mapView: com.kakaomobility.knsdk.map.knmapview.KNMapView?) {
        val currentMarker = _currentUserMarker.value
        if (currentMarker != null && mapView != null) {
           try {
               mapView.removeMarker(currentMarker)
               mapView.removeMarkersAll()
               clearCurrentUserMarker()
               _previousMarkerLocation.value = null
           }catch (e: Exception){

           }
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopLocationUpdates()
    }
}