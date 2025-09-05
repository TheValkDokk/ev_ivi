package com.evn.ev_ivi.features.map.presentation.screen.components

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.core.graphics.toColorInt
import com.evn.ev_ivi.MainActivity
import com.evn.ev_ivi.MainApplication
import com.evn.ev_ivi.core.utils.toFloatPoint
import com.evn.ev_ivi.features.map.presentation.viewmodels.MapPanelViewModel
import com.kakaomobility.knsdk.map.knmaprenderer.objects.KNMapCameraUpdate
import com.kakaomobility.knsdk.map.knmaprenderer.objects.KNMapCoordinateRegion
import com.kakaomobility.knsdk.map.knmapview.KNMapView
import com.kakaomobility.knsdk.map.knmapview.idl.KNMapRouteProperties
import com.kakaomobility.knsdk.map.uicustomsupport.renewal.KNMapMarker
import com.kakaomobility.knsdk.map.uicustomsupport.renewal.theme.base.KNMapRouteTheme
import com.kakaomobility.knsdk.map.uicustomsupport.renewal.theme.base.KNMapTheme
import com.kakaomobility.knsdk.map.uicustomsupport.renewal.theme.base.entity.KNAlterRouteInfo
import com.kakaomobility.knsdk.map.uicustomsupport.renewal.theme.base.entity.KNRouteColors
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
    val routes by viewModel.routes.collectAsState()
    val locationState by viewModel.locationState.collectAsState()
    val isFirstMapOpen by viewModel.isFirstMapOpen.collectAsState()
    val isNavigating by viewModel.isNavigating.collectAsState()

    val destinationMarker by viewModel.destinationMarker.collectAsState()

    val hasPermission = rememberLocationPermission(
        context = LocalContext.current
    )

    val activity = LocalContext.current as MainActivity

    LaunchedEffect(trip) {
        if (trip != null) {

        }
    }

    LaunchedEffect(destinationMarker) {
        if (!mapBindingComplete) return@LaunchedEffect
        if (destinationMarker != null) {
            val marker = destinationMarker!!
            activity.mapView.addMarker(marker)
        } else {
            activity.mapView.removeMarkersAll()
        }
    }

    LaunchedEffect(routes) {
        if (routes != null) {
            activity.mapView.setRoutes(routes!!)
            val region = KNMapCoordinateRegion.initWithRoute(routes!!)
            val camUpdate = KNMapCameraUpdate.fitTo(region)
            activity.mapView.animateCamera(
                camUpdate,
                duration = 500,
                withUserLocation = true,
                useNorthHeadingMode = true
            )
        }
    }

    LaunchedEffect(mapBindingComplete) {
        if (mapBindingComplete) {
            delay(100)
            viewModel.startLocationUpdates()
            isMapReady = true
        }
    }

    LaunchedEffect(locationState, mapBindingComplete, isFirstMapOpen, destinationMarker, isNavigating) {
        if (mapBindingComplete && locationState != null) {
            val loc = locationState!!
            val currentPos = loc.toFloatPoint()

            if (viewModel.shouldUpdateMarker(loc)) {
                viewModel.removeCurrentUserMarker(activity.mapView)

                val marker = KNMapMarker(currentPos)
                activity.mapView.addMarker(marker)
                viewModel.setCurrentUserMarker(marker)

                viewModel.updatePreviousMarkerLocation(loc)
            }

            if (isFirstMapOpen) {
                activity.mapView.animateCamera(
                    cameraUpdate = KNMapCameraUpdate.targetTo(currentPos).zoomTo(2.5f),
                    duration = 400,
                    withUserLocation = true,
                    useNorthHeadingMode = true
                )
                viewModel.markFirstMapOpenComplete()
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.removeCurrentUserMarker(activity.mapView)
        }
    }


//    if(isNavigating){
//        NaviMap()
//    }
//    else{
//
//        }
//    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isNavigating) {
            NaviMap(
                modifier = modifier.fillMaxSize()
            )
        } else
            if (kakaoInit && hasPermission) {
                AndroidView(
                    factory = { context ->
                        KNMapView(context).apply {
                            val theme = KNMapTheme.driveDay()
                            try {
                                MainApplication.knsdk.bindingMapView(this, theme) { error ->
                                    val ww = KNMapRouteProperties()
                                    ww.theme = KNMapRouteTheme(
                                        -1f,
                                        -1f,
                                        lineColors = KNRouteColors().apply {
                                            normal = "#4A90E2".toColorInt()
                                            trafficJamModerate = "#4A90E2".toColorInt()
                                            trafficJamHeavy = "#4A90E2".toColorInt()
                                            trafficJamVeryHeavy = "#4A90E2".toColorInt()
                                            unknown = "#4A90E2".toColorInt()
                                            blocked = "#4A90E2".toColorInt()
                                        },
                                        KNRouteColors(),
                                        KNAlterRouteInfo().apply {

                                        }
                                    )
                                    routeProperties = ww
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
                        Log.d(
                            "UPDATE",
                            "Map AndroidView update - isMapReady: $isMapReady, mapBindingComplete: $mapBindingComplete"
                        )
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