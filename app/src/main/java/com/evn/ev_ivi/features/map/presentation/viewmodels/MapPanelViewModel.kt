package com.evn.ev_ivi.features.map.presentation.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import com.evn.ev_ivi.MainApplication
import com.evn.ev_ivi.features.map.domain.entities.MapLocation
import com.kakaomobility.knsdk.KNLanguageType
import com.kakaomobility.knsdk.KNSDK
import com.kakaomobility.knsdk.common.objects.KNPOI
import com.kakaomobility.knsdk.trip.kntrip.KNTrip
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.internal.platform.PlatformRegistry.applicationContext

class MapPanelViewModel(
) : ViewModel() {
    private val _kakaoInit = MutableStateFlow(false)
    val kakaoInit = _kakaoInit.asStateFlow()

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
}