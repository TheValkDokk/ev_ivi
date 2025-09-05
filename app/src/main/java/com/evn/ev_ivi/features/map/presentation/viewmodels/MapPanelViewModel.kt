package com.evn.ev_ivi.features.map.presentation.viewmodels

import android.app.Application
import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evn.ev_ivi.MainApplication
import com.evn.ev_ivi.core.utils.toFloatPoint
import com.evn.ev_ivi.features.map.domain.entities.MapLocation
import com.evn.ev_ivi.features.map.domain.usecases.GetLocationUpdatesUseCase
import com.kakaomobility.knsdk.KNCarFuel
import com.kakaomobility.knsdk.KNCarType
import com.kakaomobility.knsdk.KNLanguageType
import com.kakaomobility.knsdk.KNRoutePriority
import com.kakaomobility.knsdk.KNSDK
import com.kakaomobility.knsdk.common.gps.KN_DEFAULT_POS_X
import com.kakaomobility.knsdk.common.gps.KN_DEFAULT_POS_Y
import com.kakaomobility.knsdk.common.gps.WGS84ToKATEC
import com.kakaomobility.knsdk.common.objects.KNError
import com.kakaomobility.knsdk.common.objects.KNPOI
import com.kakaomobility.knsdk.common.util.DoublePoint
import com.kakaomobility.knsdk.common.util.FloatPoint
import com.kakaomobility.knsdk.common.util.IntPoint
import com.kakaomobility.knsdk.map.uicustomsupport.renewal.KNMapMarker
import com.kakaomobility.knsdk.trip.knrouteconfiguration.KNRouteConfiguration
import com.kakaomobility.knsdk.trip.kntrip.KNTrip
import com.kakaomobility.knsdk.trip.kntrip.knroute.KNRoute
import com.kakaomobility.knsdk.ui.utils.getAddressWithReverseGeocodeResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
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

    private val _isPreviewRoute = MutableStateFlow(false)
    val isPreviewRoute = _isPreviewRoute.asStateFlow()


    private val _locationState = MutableStateFlow<Location?>(null)
    val locationState: StateFlow<Location?> = _locationState.asStateFlow()

    private var locationJob: Job? = null

    private val _trip = MutableStateFlow<KNTrip?>(null)
    val trip = _trip.asStateFlow()

    private val _routes = MutableStateFlow<List<KNRoute>?>(null)
    val routes = _routes.asStateFlow()

    private val _isFirstMapOpen = MutableStateFlow(true)
    val isFirstMapOpen = _isFirstMapOpen.asStateFlow()

    private val _currentUserMarker = MutableStateFlow<KNMapMarker?>(null)
    val currentUserMarker = _currentUserMarker.asStateFlow()

    private val _previousMarkerLocation = MutableStateFlow<Location?>(null)
    val previousMarkerLocation = _previousMarkerLocation.asStateFlow()

    private val _destinationMarker = MutableStateFlow<KNMapMarker?>(null)
    val destinationMarker = _destinationMarker.asStateFlow()

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

    fun createMapMarker(loc: MapLocation): KNMapMarker {
        return KNMapMarker(loc.toFloatPoint())
    }

    suspend fun onNavigate(end: MapLocation) {
        val goalLoc = MainApplication.knsdk.convertWGS84ToKATEC(
            end.lng,
            end.lat
        )
        _destinationMarker.value = null
        val goalPoi = KNPOI("목적지", goalLoc.x.toInt(), goalLoc.y.toInt(), "목적지")
        route(
            goalPoi,
            null,
            0,
            KNRoutePriority.KNRoutePriority_Recommand
        ) { error, trip, routes ->
            if (error == null) {
                _trip.value = trip
                _routes.value = routes
                _isPreviewRoute.value = true
                _destinationMarker.value = createMapMarker(end)
            } else {
                Log.e("KNSDK", "Failed to route: $error")
            }
        }
    }

    suspend fun route(
        aDestination: KNPOI?,
        aWayPoints: MutableList<KNPOI>?,
        aAvoidOption: Int,
        aRouteOption: KNRoutePriority,
        aCompletion: ((KNError?, KNTrip?, MutableList<KNRoute>?) -> Unit)?
    ) {
        coroutineScope {
            if (aDestination != null && _locationState.value != null) {
                val pos = _locationState.value!!.toFloatPoint()

                KNSDK.reverseGeocodeWithPos(pos.toDoublePoint()) { aReverseGeocodeError, _, aDoName, aSiGunGuName, aDongName ->
                    val address = if (aReverseGeocodeError != null) {
                        "현위치"
                    } else {
                        getAddressWithReverseGeocodeResult(aDoName, aSiGunGuName, aDongName)
                            ?: "현위치"
                    }

                    val start = KNPOI(address, pos.x.toInt(), pos.y.toInt(), address)
                    val goal = KNPOI(
                        aDestination.name,
                        aDestination.pos.x,
                        aDestination.pos.y,
                        aDestination.address
                    )

                    MainApplication.knsdk.makeTripWithStart(
                        start,
                        goal,
                        if (aWayPoints != null && aWayPoints.isNotEmpty()) aWayPoints else null
                    ) { aError, aTrip ->
                        if (aError != null) {
                            aCompletion?.invoke(aError, null, null)
                        } else {
                            val routeConfig = KNRouteConfiguration(
                                KNCarType.KNCarType_3,
                                KNCarFuel.KNCarFuel_Gasoline,
                            )
                            aTrip?.setRouteConfig(routeConfig)
                            aTrip?.routeWithPriority(
                                aRouteOption,
                                aAvoidOption
                            ) { aError2, aRoutes ->
                                aCompletion?.invoke(aError2, aTrip, aRoutes)
                            }
                        }
                    }
                }
            } else {
                aCompletion?.invoke(null, null, null)
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

    fun markFirstMapOpenComplete() {
        _isFirstMapOpen.value = false
    }

    fun setCurrentUserMarker(marker: KNMapMarker) {
        _currentUserMarker.value = marker
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
            } catch (e: Exception) {

            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopLocationUpdates()
    }
}