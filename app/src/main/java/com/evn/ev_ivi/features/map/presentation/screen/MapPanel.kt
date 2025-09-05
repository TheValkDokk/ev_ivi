package com.evn.ev_ivi.features.map.presentation.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.evn.ev_ivi.MainActivity
import com.evn.ev_ivi.MainApplication
import com.evn.ev_ivi.core.utils.toFloatPoint
import com.evn.ev_ivi.features.map.presentation.screen.components.Map
import com.evn.ev_ivi.features.map.presentation.screen.components.SearchPanel
import com.evn.ev_ivi.features.map.presentation.screen.components.SpeechToTextButton
import com.evn.ev_ivi.features.map.presentation.screen.components.rememberSpeechPermission
import com.evn.ev_ivi.features.map.presentation.viewmodels.MapPanelViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.kakaomobility.knsdk.common.util.FloatPoint
import com.kakaomobility.knsdk.map.knmaprenderer.objects.KNMapCameraUpdate
import org.koin.compose.viewmodel.koinViewModel

@SuppressLint("ContextCastToActivity", "MissingPermission")
@Composable
fun MapPanelScreen(
    viewModel: MapPanelViewModel = koinViewModel()
) {
    val hasPermission = rememberSpeechPermission(
        context = LocalContext.current
    )

    val locationState by viewModel.locationState.collectAsState()

    val activity = LocalContext.current as MainActivity

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val loc = locationState
                    if (loc != null) {
                        val currentPos = loc.toFloatPoint()
                        activity.mapView.animateCamera(
                            cameraUpdate = KNMapCameraUpdate.targetTo(currentPos).zoomTo(2.5f),
                            duration = 400,
                            withUserLocation = true,
                            useNorthHeadingMode = true,
                        )
                    }
                },
            ) {
                Icon(Icons.Filled.LocationOn, "Center to user location")
            }
        }
    ) { _ ->
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
                        onTextRecognized = { _ -> }
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