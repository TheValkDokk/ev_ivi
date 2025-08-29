package com.evn.ev_ivi.features.map.presentation.screen.components

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
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
import com.kakaomobility.knsdk.ui.view.KNNaviView
import org.koin.compose.viewmodel.koinViewModel

@SuppressLint("ContextCastToActivity")
@Composable
fun Map(
    modifier: Modifier = Modifier,
    viewModel: MapPanelViewModel = koinViewModel()
) {
    val kakaoInit by viewModel.kakaoInit.collectAsState()
    var isMapReady by remember { mutableStateOf(false) }
    val trip by viewModel.trip.collectAsState()

    val hasPermission = rememberLocationPermission(
        context = LocalContext.current
    )

    val activity = LocalContext.current as MainActivity

    fun initTrip() {
        trip?.let { it ->
            val curRoutePriority = KNRoutePriority.KNRoutePriority_Recommand
            val curAvoidOptions = 4 or 8
            it.routeWithPriority(curRoutePriority, curAvoidOptions) { error, _ ->
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

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (kakaoInit && hasPermission) {
            AndroidView(
                factory = { context ->
                    KNNaviView(context).apply {
                        useDarkMode = true
                        activity.naviView = this
                    }
                },
                update = { view ->
                    Log.d("UPDATE", "UPDATE")
                    if (!isMapReady) {
                        isMapReady = true
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        } else {
            CircularProgressIndicator()
        }
    }
}