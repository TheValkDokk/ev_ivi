package com.evn.ev_ivi.features.map.presentation.screen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import kotlinx.coroutines.delay
import java.util.Locale
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.evn.ev_ivi.features.map.presentation.viewmodels.SearchPanelViewModelL
import com.evn.ev_ivi.features.map.presentation.viewmodels.SpeechToTextViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SearchPanel(
    modifier: Modifier = Modifier,
    viewModel: SearchPanelViewModelL = koinViewModel(),
    speechToTextViewModel: SpeechToTextViewModel = koinViewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val recognizedText by speechToTextViewModel.recognizedText.collectAsState()
    val isListening by speechToTextViewModel.isListening.collectAsState()
    val error by speechToTextViewModel.error.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val searchError by viewModel.error.collectAsState()

    LaunchedEffect(recognizedText) {
        if (recognizedText.isNotEmpty()) {
            viewModel.updateSearchQuery(recognizedText)
        }
    }

    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotBlank()) {
            delay(500)
            viewModel.search()
        } else {
            viewModel.clearResults()
        }
    }

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                label = { Text("Search") },
                placeholder = { Text("Enter search query or use voice") },
                modifier = Modifier.weight(1f)
            )
            
            IconButton(
                onClick = { speechToTextViewModel.toggleListening() }
            ) {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = "Voice search",
                    tint = if (isListening) MaterialTheme.colorScheme.primary else Color.Gray
                )
            }
        }

        error?.let { errorMessage ->
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        if (isListening) {
            Text(
                text = "Listening...",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodySmall
            )
        }

        searchError?.let { errorMessage ->
            Text(
                text = "Search error: $errorMessage",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        if (isLoading) {
            Text(
                text = "Searching...",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodySmall
            )
        }

        if (searchResults.isNotEmpty()) {
            Text(
                text = "Found ${searchResults.size} results",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodySmall
            )
        }

        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(searchResults) { location ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { /* Handle location selection */ },
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = location.address,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = "Type: ${location.caseType}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            
                            Text(
                                text = "Floors: ${location.floorCount}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        
                        Text(
                            text = "Similarity: ${String.format(Locale.getDefault(), "%.1f%%", location.similarity * 100)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        
                        location.distance?.let { distance ->
                            Text(
                                text = "Distance: ${distance}m",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}