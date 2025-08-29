package com.evn.ev_ivi.features.map.presentation.screen.components

import androidx.compose.material3.Button
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.evn.ev_ivi.features.map.presentation.viewmodels.SpeechToTextViewModel
import org.koin.compose.viewmodel.koinViewModel

@androidx.compose.ui.tooling.preview.Preview
@Composable
fun SpeechToTextButton(
    modifier: Modifier = Modifier,
    onTextRecognized: (String) -> Unit = {},
    viewModel: SpeechToTextViewModel = koinViewModel()
) {
    val isListening by viewModel.isListening.collectAsState()
    val recognizedText by viewModel.recognizedText.collectAsState()

    LaunchedEffect(recognizedText) {
        if (recognizedText.isNotEmpty()) {
            onTextRecognized(recognizedText)
        }
    }

    Column (
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { viewModel.toggleListening() },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isListening)
                    MaterialTheme.colorScheme.error
                else
                    MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.size(64.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
            Icon(
                imageVector = if (isListening) Icons.Default.Call else Icons.Default.Done,
                contentDescription = if (isListening) "Stop listening" else "Start listening",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}