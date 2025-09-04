package com.evn.ev_ivi.features.map.presentation.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.evn.ev_ivi.MainActivity
import com.evn.ev_ivi.MainApplication
import com.evn.ev_ivi.features.map.presentation.screen.components.Map
import com.evn.ev_ivi.features.map.presentation.screen.components.SearchPanel
import com.evn.ev_ivi.features.map.presentation.screen.components.SpeechToTextButton
import com.evn.ev_ivi.features.map.presentation.screen.components.rememberSpeechPermission
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.kakaomobility.knsdk.common.util.FloatPoint
import com.kakaomobility.knsdk.map.knmaprenderer.objects.KNMapCameraUpdate

@SuppressLint("ContextCastToActivity", "MissingPermission")
@Composable
fun MapPanelScreen() {
    val hasPermission = rememberSpeechPermission(
        context = LocalContext.current
    )

    val activity = LocalContext.current as MainActivity

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
                    fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).addOnSuccessListener { loc ->
                        val startLat = loc.latitude
                        val startLong = loc.longitude
                        val ss = MainApplication.knsdk.convertWGS84ToKATEC(startLong, startLat)
                        val currentPos = FloatPoint(ss.x.toFloat(), ss.y.toFloat())
                        activity.mapView.animateCamera(
                            cameraUpdate = KNMapCameraUpdate.targetTo(currentPos).zoomTo(2.5f),
                            duration = 400,
                            withUserLocation = true,
                            useNorthHeadingMode = true,
                        )
                    }
                },
            ) {
                Icon(Icons.Filled.Add, "Floating action button.")
            }
        }
    ) { padding ->
        Row {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                SearchPanel(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(5f)
                )

                if (hasPermission) {
                    SpeechToTextButton(
                        modifier = Modifier.weight(1f),
                        onTextRecognized = { text -> }
                    )
                } else {
                    Text(
                        text = "Microphone permission required",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            Map(
                modifier = Modifier.weight(2f)
            )
        }
    }
}