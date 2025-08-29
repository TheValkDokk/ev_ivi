package com.evn.ev_ivi.features.map.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.evn.ev_ivi.features.map.presentation.screen.components.Map
import com.evn.ev_ivi.features.map.presentation.screen.components.SpeechToTextButton
import com.evn.ev_ivi.features.map.presentation.screen.components.rememberSpeechPermission

@Composable
fun MapPanelScreen() {
    val hasPermission = rememberSpeechPermission(
        context = LocalContext.current
    )
    var screenText by remember { mutableStateOf("") }

    Row {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Speech to Text Demo",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(32.dp))

            if (hasPermission) {
                SpeechToTextButton(
                    onTextRecognized = { text ->
                        screenText = text
                    }
                )
            } else {
                Text(
                    text = "Microphone permission required",
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = screenText.ifEmpty { "Recognized text will appear here..." },
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
        Map(
            modifier = Modifier.weight(2f)
        )
    }
}