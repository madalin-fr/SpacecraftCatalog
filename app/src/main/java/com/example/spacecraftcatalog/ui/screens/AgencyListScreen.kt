// ui/screens/AgencyListScreen.kt
package com.example.spacecraftcatalog.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.spacecraftcatalog.domain.model.Agency
import com.example.spacecraftcatalog.ui.viewmodel.AgencyListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgencyListScreen(
    viewModel: AgencyListViewModel,
    onAgencyClick: (Int) -> Unit
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Space Agencies") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
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
                        onRetry = viewModel::refreshAgencies,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                state.agencies.isEmpty() -> {
                    EmptyContent(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    AgencyList(
                        agencies = state.agencies,
                        onAgencyClick = onAgencyClick
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
private fun AgencyList(
    agencies: List<Agency>,
    onAgencyClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(agencies) { agency ->
            ElevatedCard(
                onClick = { onAgencyClick(agency.id) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    // Log the image URL
                    Log.d("AgencyImage", "Loading image for URL: ${agency.imageUrl}")

                    // Agency Image with placeholder
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(agency.imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = "${agency.name} logo",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Crop,
                        // Use placeholder for null or empty URLs
                        error = painterResource(R.drawable.ic_placeholder),
                        placeholder = painterResource(R.drawable.ic_placeholder)
                    )

                    // Agency Info
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = agency.name,
                            style = MaterialTheme.typography.titleLarge
                        )

                        if (!agency.foundingYear.isNullOrBlank()) {
                            Text(
                                text = "Founded: ${agency.foundingYear}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        Text(
                            text = agency.description,
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
            text = "No agencies found",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}