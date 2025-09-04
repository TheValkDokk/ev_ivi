package com.evn.ev_ivi.features.map.presentation.viewmodels

import android.app.Application
import android.location.Location
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
                _locationState.value = location
            }
        }
    }

    fun stopLocationUpdates() {
        locationJob?.cancel()
    }

    override fun onCleared() {
        super.onCleared()
        stopLocationUpdates()
    }
}