// ui/screens/SpacecraftListScreen.kt
package com.example.spacecraftcatalog.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.spacecraftcatalog.R
import com.example.spacecraftcatalog.domain.model.SpacecraftStatus
import com.example.spacecraftcatalog.domain.model.Spacecraft
import com.example.spacecraftcatalog.ui.components.EmptyContent
import com.example.spacecraftcatalog.ui.components.ErrorContent
import com.example.spacecraftcatalog.ui.viewmodel.SpacecraftListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun (() -> Boolean).SpacecraftListScreen(
    viewModel: SpacecraftListViewModel,
    onSpacecraftClick: (Int) -> Unit,
) {


    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Spacecraft") },
                actions = {
                    Button(onClick = { viewModel.refreshSpacecraft(shuffle = true) }) {
                        Text("Shuffle")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    )
    { paddingValues ->
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
fun SpacecraftList(
    spacecraft: List<Spacecraft>,
    onSpacecraftClick: (Int) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(spacecraft) { sc ->
            ElevatedCard(
                onClick = { onSpacecraftClick(sc.id) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    // Image with proper error and placeholder handling
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(sc.imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = "${sc.name} image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Crop,
                        error = painterResource(R.drawable.ic_placeholder),
                        placeholder = painterResource(R.drawable.ic_placeholder)
                    )

                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = sc.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = sc.status.name.replace("_", " "),
                                style = MaterialTheme.typography.bodyMedium,
                                color = when (sc.status) {
                                    SpacecraftStatus.ACTIVE -> Color.Green
                                    SpacecraftStatus.RETIRED -> Color.Red
                                    SpacecraftStatus.IN_DEVELOPMENT -> Color.Blue
                                    else -> Color.Gray
                                }
                            )
                            sc.agency?.let { agency ->
                                Text(
                                    text = agency.name,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}