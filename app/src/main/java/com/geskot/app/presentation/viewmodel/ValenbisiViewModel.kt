package com.geskot.app.presentation.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.geskot.app.data.model.UiState
import com.geskot.app.data.model.ValenbisiStation
import com.geskot.app.data.repository.ValenbisiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

/**
 * ViewModel for managing Valenbisi stations data and UI state
 * Uses StateFlow for reactive UI updates with Compose
 */
class ValenbisiViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ValenbisiRepository(application.applicationContext)

    companion object {
        private const val TAG = "ValenbisiViewModel"
    }

    // Private mutable state flows
    private val _stationsState = MutableStateFlow<UiState<List<ValenbisiStation>>>(UiState.Idle)
    private val _selectedStation = MutableStateFlow<ValenbisiStation?>(null)
    private val _searchQuery = MutableStateFlow("")
    private val _filteredStations = MutableStateFlow<List<ValenbisiStation>>(emptyList())

    // Public read-only state flows
    val stationsState: StateFlow<UiState<List<ValenbisiStation>>> = _stationsState.asStateFlow()
    val selectedStation: StateFlow<ValenbisiStation?> = _selectedStation.asStateFlow()
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    val filteredStations: StateFlow<List<ValenbisiStation>> = _filteredStations.asStateFlow()

    init {
        loadStations()
    }

    /**
     * Load stations from repository
     * Updates the UI state through StateFlow
     */
    fun loadStations() {
        Log.d(TAG, "Loading stations...")
        _stationsState.value = UiState.Loading

        viewModelScope.launch {
            repository.getStations()
                .catch { exception ->
                    Log.e(TAG, "Error loading stations", exception)
                    _stationsState.value = UiState.Error(
                        message = "Error al cargar las estaciones: ${exception.message}",
                        exception = exception
                    )
                }
                .collect { stations ->
                    Log.d(TAG, "Successfully loaded ${stations.size} stations")
                    _stationsState.value = UiState.Success(stations)
                    _filteredStations.value = stations
                }
        }
    }

    /**
     * Retry loading stations in case of error
     */
    fun retryLoadStations() {
        Log.d(TAG, "Retrying to load stations...")
        loadStations()
    }

    /**
     * Select a station for detail view
     * @param station The station to select
     */
    fun selectStation(station: ValenbisiStation) {
        Log.d(TAG, "Selecting station: ${station.name}")
        _selectedStation.value = station
    }

    /**
     * Clear selected station
     */
    fun clearSelectedStation() {
        Log.d(TAG, "Clearing selected station")
        _selectedStation.value = null
    }

    /**
     * Update search query and filter stations
     * @param query The search query string
     */
    fun updateSearchQuery(query: String) {
        Log.d(TAG, "Updating search query: $query")
        _searchQuery.value = query
        filterStations(query)
    }

    /**
     * Filter stations based on search query
     * Searches in station name and address
     */
    private fun filterStations(query: String) {
        val currentStations = when (val state = _stationsState.value) {
            is UiState.Success -> state.data
            else -> return
        }

        val filtered = if (query.isBlank()) {
            currentStations
        } else {
            currentStations.filter { station: ValenbisiStation ->
                station.name.contains(query, ignoreCase = true) ||
                station.address.contains(query, ignoreCase = true)
            }
        }

        Log.d(TAG, "Filtered ${filtered.size} stations from ${currentStations.size} total")
        _filteredStations.value = filtered
    }

    /**
     * Sort stations by availability (most bikes first)
     */
    fun sortByAvailability() {
        val currentStations = _filteredStations.value
        val sorted = currentStations.sortedByDescending { it.availableBikes }
        Log.d(TAG, "Sorted stations by availability")
        _filteredStations.value = sorted
    }

    /**
     * Sort stations by name alphabetically
     */
    fun sortByName() {
        val currentStations = _filteredStations.value
        val sorted = currentStations.sortedBy { it.name }
        Log.d(TAG, "Sorted stations by name")
        _filteredStations.value = sorted
    }

    /**
     * Sort stations by distance (requires current location - placeholder for now)
     * TODO: Implement actual distance calculation
     */
    fun sortByDistance() {
        Log.d(TAG, "Distance sorting not yet implemented")
        // Placeholder - in a real app, you'd calculate distance from user's location
    }

    /**
     * Filter stations by availability status
     * @param minAvailability Minimum number of available bikes
     */
    fun filterByAvailability(minAvailability: Int) {
        val currentStations = when (val state = _stationsState.value) {
            is UiState.Success -> state.data
            else -> return
        }
        val filtered = currentStations.filter { it.availableBikes >= minAvailability }
        Log.d(TAG, "Filtered ${filtered.size} stations with at least $minAvailability bikes")
        _filteredStations.value = filtered
    }

    /**
     * Reset all filters and show all stations
     */
    fun resetFilters() {
        Log.d(TAG, "Resetting all filters")
        _searchQuery.value = ""
        val allStations = when (val state = _stationsState.value) {
            is UiState.Success -> state.data
            else -> emptyList()
        }
        _filteredStations.value = allStations
    }

    /**
     * Get stations grouped by availability status for quick overview
     */
    fun getStationsGroupedByAvailability(): Map<String, List<ValenbisiStation>> {
        val stations = _filteredStations.value
        return stations.groupBy { station ->
            when {
                station.availableBikes >= 10 -> "Alta disponibilidad"
                station.availableBikes >= 5 -> "Media disponibilidad"
                else -> "Baja disponibilidad"
            }
        }
    }

    /**
     * Get total statistics for current filtered stations
     */
    fun getStationsStatistics(): StationStatistics {
        val stations = _filteredStations.value
        return StationStatistics(
            totalStations = stations.size,
            totalBikes = stations.sumOf { it.availableBikes },
            totalSpaces = stations.sumOf { it.freeSpaces },
            averageAvailability = if (stations.isNotEmpty()) {
                stations.map { it.availabilityPercentage }.average().toFloat()
            } else 0f
        )
    }
}

/**
 * Data class for station statistics
 */
data class StationStatistics(
    val totalStations: Int,
    val totalBikes: Int,
    val totalSpaces: Int,
    val averageAvailability: Float
)