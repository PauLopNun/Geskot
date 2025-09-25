package com.geskot.app.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.geskot.app.data.model.AvailabilityStatus
import com.geskot.app.data.model.UiState
import com.geskot.app.data.model.ValenbisiStation
import com.geskot.app.presentation.viewmodel.ValenbisiViewModel

/**
 * Main screen displaying the list of Valenbisi stations
 * @param onStationClick Callback when a station is clicked
 * @param viewModel ViewModel for managing state
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onStationClick: (ValenbisiStation) -> Unit,
    viewModel: ValenbisiViewModel = viewModel()
) {
    val stationsState by viewModel.stationsState.collectAsStateWithLifecycle()
    val filteredStations by viewModel.filteredStations.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val showOnlyAvailableBikes by viewModel.showOnlyAvailableBikes.collectAsStateWithLifecycle()
    val showOnlyAvailableSpots by viewModel.showOnlyAvailableSpots.collectAsStateWithLifecycle()

    var showSortMenu by remember { mutableStateOf(false) }
    var showFilterMenu by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = "GesKot - Valenbisi",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            },
            actions = {
                // Sort button
                IconButton(onClick = { showSortMenu = true }) {
                    Icon(
                        imageVector = Icons.Default.Sort,
                        contentDescription = "Ordenar"
                    )
                }

                // Filter button
                IconButton(onClick = { showFilterMenu = true }) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Filtrar"
                    )
                }

                // Refresh button
                IconButton(onClick = { viewModel.retryLoadStations() }) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Actualizar"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        )

        // Search Bar
        SearchBar(
            query = searchQuery,
            onQueryChange = viewModel::updateSearchQuery,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        // Content based on state
        when (stationsState) {
            is UiState.Idle -> {
                LoadingContent()
            }
            is UiState.Loading -> {
                LoadingContent()
            }
            is UiState.Success -> {
                StationsContent(
                    stations = filteredStations,
                    onStationClick = onStationClick
                )
            }
            is UiState.Error -> {
                ErrorContent(
                    message = (stationsState as UiState.Error).message,
                    onRetry = { viewModel.retryLoadStations() }
                )
            }
        }

        // Sort Menu
        SortMenu(
            expanded = showSortMenu,
            onDismiss = { showSortMenu = false },
            onSortByName = {
                viewModel.sortByName()
                showSortMenu = false
            },
            onSortByAvailability = {
                viewModel.sortByAvailability()
                showSortMenu = false
            }
        )

        // Filter Menu
        FilterMenu(
            expanded = showFilterMenu,
            onDismiss = { showFilterMenu = false },
            showOnlyAvailableBikes = showOnlyAvailableBikes,
            showOnlyAvailableSpots = showOnlyAvailableSpots,
            onToggleBikesFilter = { enabled ->
                viewModel.toggleAvailableBikesFilter(enabled, if (enabled) 1 else 0)
            },
            onToggleSpotsFilter = { enabled ->
                viewModel.toggleAvailableSpotsFilter(enabled, if (enabled) 1 else 0)
            },
            onFilterByAvailability = { minBikes ->
                viewModel.filterByAvailability(minBikes)
                showFilterMenu = false
            },
            onResetFilters = {
                viewModel.resetFilters()
                showFilterMenu = false
            }
        )
    }
}

/**
 * Search bar component
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier,
        placeholder = { Text("Buscar estaciones...") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Buscar"
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Limpiar"
                    )
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(12.dp)
    )
}

/**
 * Loading content component
 */
@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Cargando estaciones...",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

/**
 * Error content component
 */
@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = "Error",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(64.dp)
            )
            Text(
                text = "Error al cargar datos",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Button(
                onClick = onRetry,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Reintentar")
            }
        }
    }
}

/**
 * Stations list content
 */
@Composable
private fun StationsContent(
    stations: List<ValenbisiStation>,
    onStationClick: (ValenbisiStation) -> Unit
) {
    if (stations.isEmpty()) {
        EmptyContent()
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(
                items = stations,
                key = { _, station -> station.id }
            ) { index, station ->
                AnimatedVisibility(
                    visible = true,
                    enter = slideInVertically(
                        initialOffsetY = { it },
                        animationSpec = tween(durationMillis = 300, delayMillis = index * 50)
                    ) + fadeIn(animationSpec = tween(300))
                ) {
                    StationCard(
                        station = station,
                        onClick = { onStationClick(station) }
                    )
                }
            }
        }
    }
}

/**
 * Empty content when no stations found
 */
@Composable
private fun EmptyContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.SearchOff,
                contentDescription = "Sin resultados",
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "No se encontraron estaciones",
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = "Intenta con otros términos de búsqueda",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * Individual station card component
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StationCard(
    station: ValenbisiStation,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Availability indicator
            AvailabilityIndicator(
                status = station.availabilityStatus,
                availableBikes = station.availableBikes
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Station info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = station.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = station.address,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Bikes and spaces info
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    BikeInfo(
                        icon = Icons.Default.DirectionsBike,
                        count = station.availableBikes,
                        label = "bicis"
                    )
                    BikeInfo(
                        icon = Icons.Default.LocalParking,
                        count = station.freeSpaces,
                        label = "espacios"
                    )
                }
            }

            // Navigation arrow
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Ver detalle",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * Availability indicator with color coding
 */
@Composable
private fun AvailabilityIndicator(
    status: AvailabilityStatus,
    availableBikes: Int
) {
    val color = when (status) {
        AvailabilityStatus.HIGH -> Color(0xFF4CAF50)  // Green
        AvailabilityStatus.MEDIUM -> Color(0xFFFF9800)  // Orange
        AvailabilityStatus.LOW -> Color(0xFFF44336)   // Red
    }

    Box(
        modifier = Modifier
            .size(56.dp)
            .background(color, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = availableBikes.toString(),
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * Bike/space info component
 */
@Composable
private fun BikeInfo(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    count: Int,
    label: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "$count $label",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * Sort menu dropdown
 */
@Composable
private fun SortMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onSortByName: () -> Unit,
    onSortByAvailability: () -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss
    ) {
        DropdownMenuItem(
            text = { Text("Por nombre") },
            onClick = onSortByName,
            leadingIcon = {
                Icon(Icons.Default.SortByAlpha, contentDescription = null)
            }
        )
        DropdownMenuItem(
            text = { Text("Por disponibilidad") },
            onClick = onSortByAvailability,
            leadingIcon = {
                Icon(Icons.Default.DirectionsBike, contentDescription = null)
            }
        )
    }
}

/**
 * Filter menu dropdown
 */
@Composable
private fun FilterMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    showOnlyAvailableBikes: Boolean,
    showOnlyAvailableSpots: Boolean,
    onToggleBikesFilter: (Boolean) -> Unit,
    onToggleSpotsFilter: (Boolean) -> Unit,
    onFilterByAvailability: (Int) -> Unit,
    onResetFilters: () -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss
    ) {
        // Switch for available bikes filter
        DropdownMenuItem(
            text = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Solo con bicis disponibles")
                    Switch(
                        checked = showOnlyAvailableBikes,
                        onCheckedChange = onToggleBikesFilter
                    )
                }
            },
            onClick = { onToggleBikesFilter(!showOnlyAvailableBikes) },
            leadingIcon = {
                Icon(Icons.Default.DirectionsBike, contentDescription = null)
            }
        )

        // Switch for available spots filter
        DropdownMenuItem(
            text = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Solo con espacios libres")
                    Switch(
                        checked = showOnlyAvailableSpots,
                        onCheckedChange = onToggleSpotsFilter
                    )
                }
            },
            onClick = { onToggleSpotsFilter(!showOnlyAvailableSpots) },
            leadingIcon = {
                Icon(Icons.Default.LocalParking, contentDescription = null)
            }
        )

        Divider()

        // Legacy filters for specific bike counts
        DropdownMenuItem(
            text = { Text("5+ bicis disponibles") },
            onClick = { onFilterByAvailability(5) }
        )
        DropdownMenuItem(
            text = { Text("10+ bicis disponibles") },
            onClick = { onFilterByAvailability(10) }
        )

        Divider()

        DropdownMenuItem(
            text = { Text("Limpiar filtros") },
            onClick = onResetFilters,
            leadingIcon = {
                Icon(Icons.Default.Clear, contentDescription = null)
            }
        )
    }
}