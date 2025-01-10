// ui/screens/SpacecraftListScreen.kt
package com.example.spacecraftcatalog.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.spacecraftcatalog.R
import com.example.spacecraftcatalog.domain.model.Spacecraft
import com.example.spacecraftcatalog.ui.viewmodel.SpacecraftListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpacecraftListScreen(
    viewModel: SpacecraftListViewModel,
    onSpacecraftClick: (Int) -> Unit,
    onNavigateUp: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Spacecraft") },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Navigate back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                state.error != null -> {
                    ErrorContent(
                        error = state.error!!,
                        onRetry = viewModel::refreshSpacecraft,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                state.spacecraft.isEmpty() -> {
                    EmptyContent(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    SpacecraftList(
                        spacecraft = state.spacecraft,
                        onSpacecraftClick = onSpacecraftClick
                    )
                }
            }

            if (state.isLoading) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SpacecraftList(
    spacecraft: List<Spacecraft>,
    onSpacecraftClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(spacecraft) { spacecraft ->
            ElevatedCard(
                onClick = { onSpacecraftClick(spacecraft.id) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    // Spacecraft Image
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(spacecraft.imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = "${spacecraft.name} image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Crop,
                        error = painterResource(R.drawable.ic_placeholder),
                        placeholder = painterResource(R.drawable.ic_launcher_foreground)
                    )

                    // Spacecraft Info
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = spacecraft.name,
                            style = MaterialTheme.typography.titleLarge
                        )

                        if (!spacecraft.serialNumber.isNullOrBlank()) {
                            Text(
                                text = "Serial Number: ${spacecraft.serialNumber}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        Text(
                            text = "Status: ${spacecraft.status.name}",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Text(
                            text = spacecraft.description,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ErrorContent(
    error: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Error: $error",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}

@Composable
private fun EmptyContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "No spacecraft found",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}