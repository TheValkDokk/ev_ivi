package com.evn.ev_ivi.features.map.presentation.screen.components

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.evn.ev_ivi.MainActivity
import com.evn.ev_ivi.MainApplication
import com.evn.ev_ivi.features.map.presentation.viewmodels.MapPanelViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.kakaomobility.knsdk.KNRoutePriority
import com.kakaomobility.knsdk.KNSDK
import com.kakaomobility.knsdk.common.objects.KNPOI
import com.kakaomobility.knsdk.ui.component.MapViewCameraMode
import com.kakaomobility.knsdk.ui.view.KNNaviView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@SuppressLint("ContextCastToActivity")
@Composable
fun NaviMap(
    modifier: Modifier = Modifier,
    viewModel: MapPanelViewModel = koinViewModel()
) {
    val trip by viewModel.trip.collectAsState()
    val locationState by viewModel.locationState.collectAsState()

    val activity = LocalContext.current as MainActivity

    val curRoutePriority = KNRoutePriority.KNRoutePriority_Recommand
    val curAvoidOptions = 4 or 8


    if (trip == null) {
        CircularProgressIndicator()
    } else {
        Box(
            modifier = modifier.fillMaxSize()
        ) {
            AndroidView(
                modifier = modifier.fillMaxSize(),
                factory = { context ->
                    KNNaviView(context).apply {
                        useDashBoardSpeed = true
                        useDarkMode = true
                        mapViewMode = MapViewCameraMode.Bird

//                        trip!!.routeWithPriority(curRoutePriority, curAvoidOptions) { _, _ ->
//                            val guidance = MainApplication.knsdk.sharedGuidance()
//                            guidance?.apply {
//                                guideStateDelegate = activity
//                                locationGuideDelegate = activity
//                                routeGuideDelegate = activity
//                                safetyGuideDelegate = activity
//                                voiceGuideDelegate = activity
//                                citsGuideDelegate = activity
//                                useBackgroundUpdate = true
//                            }
//                            if (guidance != null) {
//                                activity.naviView.initWithGuidance(
//                                    guidance,
//                                    trip,
//                                    curRoutePriority,
//                                    0
//                                )
//                            }
//                        }
                        activity.naviView = this
                        if (ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            )
                            == PackageManager.PERMISSION_GRANTED
                        ) {
                            LocationServices.getFusedLocationProviderClient(context)
                                .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                                .addOnSuccessListener { location ->
                                    if (location != null) {
                                        val katecPoint = KNSDK.convertWGS84ToKATEC(
                                            location.longitude,
                                            location.latitude
                                        )

                                        val startPoi = KNPOI(
                                            "현재 위치",
                                            katecPoint.x.toInt(),
                                            katecPoint.y.toInt(),
                                            "출발지"
                                        )

                                        CoroutineScope(Dispatchers.IO).launch {
                                            val guidance = MainApplication.knsdk.sharedGuidance()!!
                                            guidance.stop()
                                            val ss = KNSDK.convertWGS84ToKATEC(
                                                126.93131425331487,
                                                37.46864877011771
                                            )
                                            MainApplication.knsdk.makeTripWithStart(
                                                aStart = startPoi,
                                                aGoal = KNPOI(
                                                    "Destination",
                                                    ss.x.toInt(),
                                                    ss.y.toInt(),
                                                    "Des"
                                                ),
                                                aVias = null
                                            ) { error, trip ->
                                                val guidance =
                                                    MainApplication.knsdk.sharedGuidance()
                                                guidance?.apply {
                                                    guideStateDelegate = activity
                                                    locationGuideDelegate = activity
                                                    routeGuideDelegate = activity
                                                    safetyGuideDelegate = activity
                                                    voiceGuideDelegate = activity
                                                    citsGuideDelegate = activity
                                                }

                                                if (guidance != null) {
                                                    activity.naviView.initWithGuidance(
                                                        guidance,
                                                        trip,
                                                        KNRoutePriority.KNRoutePriority_Recommand,
                                                        0
                                                    )

                                                }
                                            }
                                        }
                                    }
                                }
                        }
                    }
                }
            )
        }
    }

}