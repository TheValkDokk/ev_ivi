package com.evn.ev_ivi.features.map.presentation.screen.components

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.evn.ev_ivi.MainActivity
import com.evn.ev_ivi.MainApplication
import com.evn.ev_ivi.features.map.presentation.viewmodels.MapPanelViewModel
import com.kakaomobility.knsdk.KNRoutePriority
import com.kakaomobility.knsdk.common.util.FloatPoint
import com.kakaomobility.knsdk.map.knmaprenderer.objects.KNMapCameraUpdate
import com.kakaomobility.knsdk.map.knmapview.KNMapView
import com.kakaomobility.knsdk.map.uicustomsupport.renewal.KNMapMarker
import com.kakaomobility.knsdk.map.uicustomsupport.renewal.theme.base.KNMapTheme
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel

@SuppressLint("ContextCastToActivity", "MissingPermission")
@Composable
fun Map(
    modifier: Modifier = Modifier,
    viewModel: MapPanelViewModel = koinViewModel()
) {
    val kakaoInit by viewModel.kakaoInit.collectAsState()
    var isMapReady by remember { mutableStateOf(false) }
    var mapBindingComplete by remember { mutableStateOf(false) }
    val trip by viewModel.trip.collectAsState()
    val locationState by viewModel.locationState.collectAsState()

    val hasPermission = rememberLocationPermission(
        context = LocalContext.current
    )

    val activity = LocalContext.current as MainActivity

    fun initTrip() {
        trip?.let { it ->
            val curRoutePriority = KNRoutePriority.KNRoutePriority_Recommand
            val curAvoidOptions = 4 or 8
            it.routeWithPriority(curRoutePriority, curAvoidOptions) { _, _ ->
                MainApplication.knsdk.sharedGuidance()?.apply {
                    guideStateDelegate = activity
                    locationGuideDelegate = activity
                    routeGuideDelegate = activity
                    safetyGuideDelegate = activity
                    voiceGuideDelegate = activity
                    citsGuideDelegate = activity

                    activity.naviView.initWithGuidance(
                        this,
                        trip,
                        curRoutePriority,
                        curAvoidOptions
                    )
                }
            }

        }
    }

    LaunchedEffect(trip) {
        if (trip != null) {
            initTrip()
        }
    }

    LaunchedEffect(mapBindingComplete) {
        if (mapBindingComplete) {
            delay(100)
            viewModel.startLocationUpdates()
            isMapReady = true
        }
    }

    LaunchedEffect(locationState, mapBindingComplete) {
        Log.d("LOCATION", "LocationState: $locationState")
        if (mapBindingComplete && locationState != null) {
            val loc = locationState!!
            val startLat = loc.latitude
            val startLong = loc.longitude
            val ss = MainApplication.knsdk.convertWGS84ToKATEC(startLong, startLat)
            val currentPos = FloatPoint(ss.x.toFloat(), ss.y.toFloat())

            // Clear existing markers (optional, depending on your needs)
//            activity.mapView?.removeAllMarkers()

            // Add new marker
            val marker = KNMapMarker(currentPos)
            activity.mapView?.addMarker(marker)

            // Animate camera
            activity.mapView?.animateCamera(
                cameraUpdate = KNMapCameraUpdate.targetTo(currentPos).zoomTo(2.5f),
                duration = 400,
                withUserLocation = true,
                useNorthHeadingMode = true
            )
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (kakaoInit && hasPermission) {
            AndroidView(
                factory = { context ->
                    KNMapView(context).apply {
                        val theme = KNMapTheme.driveDay()
                        try {
                            MainApplication.knsdk.bindingMapView(this, theme) { error ->
                                activity.mapView = this
                                if (error == null) {
                                    mapBindingComplete = true
                                } else {
                                    Log.e("KNSDK", "Failed to bind map view: $error")
                                }
                            }
                        } catch (e: Exception) {
                            Log.e("KNSDK", "Exception during map binding: ${e.message}", e)
                        }
                    }
                },
                update = { view ->
                    Log.d("UPDATE", "Map AndroidView update - isMapReady: $isMapReady, mapBindingComplete: $mapBindingComplete")
                    if (mapBindingComplete && !isMapReady) {
                        view.invalidate()
                        view.requestLayout()
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
            
            // Show loading indicator while map is binding
            if (!mapBindingComplete) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.7f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            }
        } else {
            CircularProgressIndicator()
        }
    }
}