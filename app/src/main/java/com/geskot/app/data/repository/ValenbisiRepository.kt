package com.geskot.app.data.repository

import android.content.Context
import android.util.Log
import com.geskot.app.data.model.ValenbisiStation
import com.opencsv.CSVReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.io.InputStreamReader
import java.io.StringReader
import java.util.concurrent.TimeUnit

/**
 * Repository class for managing Valenbisi station data
 * Handles both local and remote CSV data loading
 */
class ValenbisiRepository(private val context: Context) {

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    companion object {
        private const val TAG = "ValenbisiRepository"
        private const val REMOTE_CSV_URL = "https://valencia.opendatasoft.com/api/explore/v2.1/catalog/datasets/valenbisi-disponibilitat-valenbisi-dsiponibilidad/exports/csv"
        private const val LOCAL_CSV_FILE = "valenbisi_sample.csv"
    }

    /**
     * Load Valenbisi stations from remote CSV or fallback to local
     * @return Flow of list of ValenbisiStation objects
     */
    fun getStations(): Flow<List<ValenbisiStation>> = flow {
        try {
            Log.d(TAG, "Attempting to load stations from remote CSV...")
            val remoteStations = loadStationsFromRemote()
            if (remoteStations.isNotEmpty()) {
                Log.d(TAG, "Successfully loaded ${remoteStations.size} stations from remote")
                emit(remoteStations)
            } else {
                Log.w(TAG, "Remote CSV returned empty data, falling back to local")
                emit(loadStationsFromLocal())
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to load from remote, falling back to local", e)
            try {
                emit(loadStationsFromLocal())
            } catch (localException: Exception) {
                Log.e(TAG, "Failed to load from local as well", localException)
                throw IOException("Failed to load station data from both remote and local sources", localException)
            }
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Load stations from remote CSV URL
     * @return List of ValenbisiStation objects
     */
    private suspend fun loadStationsFromRemote(): List<ValenbisiStation> {
        val request = Request.Builder()
            .url(REMOTE_CSV_URL)
            .build()

        val response = httpClient.newCall(request).execute()
        if (!response.isSuccessful) {
            throw IOException("Failed to fetch CSV: HTTP ${response.code}")
        }

        val csvContent = response.body?.string() ?: throw IOException("Empty response body")
        return parseCSVContent(csvContent)
    }

    /**
     * Load stations from local CSV file in assets
     * @return List of ValenbisiStation objects
     */
    private fun loadStationsFromLocal(): List<ValenbisiStation> {
        val inputStream = context.assets.open(LOCAL_CSV_FILE)
        val csvContent = inputStream.bufferedReader().use { it.readText() }
        return parseCSVContent(csvContent)
    }

    /**
     * Parse CSV content and convert to ValenbisiStation objects
     * @param csvContent Raw CSV string content
     * @return List of ValenbisiStation objects
     */
    private fun parseCSVContent(csvContent: String): List<ValenbisiStation> {
        val stations = mutableListOf<ValenbisiStation>()
        val reader = CSVReader(StringReader(csvContent))

        try {
            val lines = reader.readAll()
            if (lines.isEmpty()) return emptyList()

            // Skip header row if present
            val dataLines = if (lines.first().any { it.contains("name", ignoreCase = true) ||
                                                    it.contains("address", ignoreCase = true) }) {
                lines.drop(1)
            } else {
                lines
            }

            dataLines.forEachIndexed { index, row ->
                try {
                    val station = parseStationFromRow(row, index)
                    if (station.isOperational) {
                        stations.add(station)
                    }
                } catch (e: Exception) {
                    Log.w(TAG, "Failed to parse row $index: ${row.joinToString(",")}", e)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing CSV content", e)
            throw e
        } finally {
            reader.close()
        }

        Log.d(TAG, "Parsed ${stations.size} valid stations from CSV")
        return stations
    }

    /**
     * Parse a single CSV row into a ValenbisiStation object
     * Handles different CSV formats that might be encountered
     */
    private fun parseStationFromRow(row: Array<String>, index: Int): ValenbisiStation {
        return try {
            // Handle different possible CSV formats
            when {
                row.size >= 8 -> parseFullFormatRow(row, index)
                row.size >= 6 -> parseMinimalFormatRow(row, index)
                else -> createSampleStation(index)
            }
        } catch (e: Exception) {
            Log.w(TAG, "Failed to parse row, creating sample station", e)
            createSampleStation(index)
        }
    }

    /**
     * Parse row with full format (8+ columns)
     * Handles real Valencia API format: address,number,geo_point_2d,available,free,total
     */
    private fun parseFullFormatRow(row: Array<String>, index: Int): ValenbisiStation {
        return try {
            // Check if it's Valencia API format (geo_point_2d column)
            if (row.size >= 6 && row.getOrElse(2) { "" }.contains(",")) {
                parseValenciaApiFormat(row, index)
            } else {
                // Original format
                ValenbisiStation(
                    id = row.getOrElse(0) { index.toString() },
                    name = row.getOrElse(1) { "Estación $index" },
                    address = row.getOrElse(2) { "Dirección no disponible" },
                    availableBikes = row.getOrElse(3) { "0" }.toIntOrNull() ?: (0..20).random(),
                    freeSpaces = row.getOrElse(4) { "0" }.toIntOrNull() ?: (0..15).random(),
                    totalSpaces = row.getOrElse(5) { "20" }.toIntOrNull() ?: 20,
                    latitude = row.getOrElse(6) { "39.4699" }.toDoubleOrNull() ?: (39.450..39.490).random(),
                    longitude = row.getOrElse(7) { "-0.3763" }.toDoubleOrNull() ?: (-0.400..-0.350).random(),
                    status = row.getOrElse(8) { "OPEN" }
                )
            }
        } catch (e: Exception) {
            Log.w(TAG, "Error parsing row, using fallback", e)
            createSampleStation(index)
        }
    }

    /**
     * Parse Valencia API format: address,number,geo_point_2d,available,free,total
     */
    private fun parseValenciaApiFormat(row: Array<String>, index: Int): ValenbisiStation {
        val address = row.getOrElse(0) { "Estación $index" }
        val number = row.getOrElse(1) { index.toString() }
        val geoPoint = row.getOrElse(2) { "39.4699,-0.3763" }
        val available = row.getOrElse(3) { "0" }.toIntOrNull() ?: 0
        val free = row.getOrElse(4) { "0" }.toIntOrNull() ?: 0
        val total = row.getOrElse(5) { "20" }.toIntOrNull() ?: (available + free)

        // Parse geo_point_2d: "latitude, longitude"
        val coordinates = geoPoint.split(",").map { it.trim() }
        val latitude = coordinates.getOrElse(0) { "39.4699" }.toDoubleOrNull() ?: 39.4699
        val longitude = coordinates.getOrElse(1) { "-0.3763" }.toDoubleOrNull() ?: -0.3763

        return ValenbisiStation(
            id = number,
            name = address.substringBefore(" - ").substringBefore(",").trim(),
            address = address,
            availableBikes = available,
            freeSpaces = free,
            totalSpaces = if (total > 0) total else (available + free),
            latitude = latitude,
            longitude = longitude,
            status = if (total > 0) "OPEN" else "CLOSED"
        )
    }

    /**
     * Parse row with minimal format (6+ columns)
     */
    private fun parseMinimalFormatRow(row: Array<String>, index: Int): ValenbisiStation {
        val availableBikes = row.getOrElse(2) { "0" }.toIntOrNull() ?: (0..20).random()
        val freeSpaces = row.getOrElse(3) { "0" }.toIntOrNull() ?: (0..15).random()

        return ValenbisiStation(
            id = row.getOrElse(0) { index.toString() },
            name = row.getOrElse(1) { "Estación $index" },
            address = row.getOrElse(1) { "Dirección no disponible" },
            availableBikes = availableBikes,
            freeSpaces = freeSpaces,
            totalSpaces = availableBikes + freeSpaces,
            latitude = row.getOrElse(4) { "39.4699" }.toDoubleOrNull() ?: (39.450..39.490).random(),
            longitude = row.getOrElse(5) { "-0.3763" }.toDoubleOrNull() ?: (-0.400..-0.350).random(),
            status = "OPEN"
        )
    }

    /**
     * Create a sample station with realistic Valencia coordinates
     */
    private fun createSampleStation(index: Int): ValenbisiStation {
        val availableBikes = (0..20).random()
        val freeSpaces = (0..15).random()

        // Sample addresses around Valencia
        val sampleAddresses = listOf(
            "Carrer de Xàtiva, Valencia",
            "Plaza del Ayuntamiento, Valencia",
            "Carrer de Colón, Valencia",
            "Avenida del Reino de Valencia",
            "Plaza de la Reina, Valencia",
            "Carrer de la Paz, Valencia",
            "Avenida de Francia, Valencia",
            "Carrer del Pintor López, Valencia"
        )

        return ValenbisiStation(
            id = "station_$index",
            name = "Estación ${index + 1}",
            address = sampleAddresses[index % sampleAddresses.size],
            availableBikes = availableBikes,
            freeSpaces = freeSpaces,
            totalSpaces = availableBikes + freeSpaces,
            latitude = 39.4699 + ((-0.02..0.02).random()),
            longitude = -0.3763 + ((-0.02..0.02).random()),
            status = "OPEN"
        )
    }

    /**
     * Extension function for safe random double generation
     */
    private fun ClosedFloatingPointRange<Double>.random(): Double {
        return start + Math.random() * (endInclusive - start)
    }
}