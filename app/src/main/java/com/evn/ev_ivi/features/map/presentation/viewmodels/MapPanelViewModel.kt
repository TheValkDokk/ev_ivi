package com.evn.ev_ivi.features.map.presentation.viewmodels

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.evn.ev_ivi.features.map.domain.entities.MapLocation
import com.kakaomobility.knsdk.KNLanguageType
import com.kakaomobility.knsdk.KNSDK
import com.kakaomobility.knsdk.common.objects.KNPOI
import com.kakaomobility.knsdk.ui.view.KNNaviView
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.internal.platform.PlatformRegistry.applicationContext

class MapPanelViewModel() : ViewModel() {
    private val _kakaoInit = MutableStateFlow(false)
    val kakaoInit = _kakaoInit.asStateFlow()

    init {
        KNSDK.apply {
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

    fun onNavigate(startLat: Int, startLong: Int ,end: MapLocation) {
        val start = KNPOI( "current", startLong, startLat, "current")
//        val endLat = (end.latitude?.times(10000))?.toInt()!!
//        val endLong = (end.longitude?.times(10000))?.toInt()!!
        Log.d("WWWW","RRRRR")
        val endLat = 3754784
        val endLong = 12702461
        val end = KNPOI(end.id.toString(), endLong, endLat, end.address)
        Log.d("WWWW","TTTTT")
        KNSDK.makeTripWithStart(start, end, null) { error,trip ->
            Log.d("WWW","Error: $error")
            Log.d("WWWW","Trip: $trip")
            if(error != null) {
                print("Error")
            }
        }
    }
}