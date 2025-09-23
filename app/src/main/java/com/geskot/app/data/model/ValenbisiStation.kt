package com.geskot.app.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Data class representing a Valenbisi station
 * Implements Parcelable for easy passing between Activities/Fragments
 */
@Parcelize
data class ValenbisiStation(
    val id: String = "",
    val name: String = "",
    val address: String = "",
    val availableBikes: Int = 0,
    val freeSpaces: Int = 0,
    val totalSpaces: Int = 0,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val status: String = "OPEN"
) : Parcelable {

    /**
     * Calculate availability percentage for UI color coding
     * @return percentage of available bikes (0.0 to 1.0)
     */
    val availabilityPercentage: Float
        get() = if (totalSpaces > 0) availableBikes.toFloat() / totalSpaces.toFloat() else 0f

    /**
     * Get availability status for color coding
     * @return AvailabilityStatus enum
     */
    val availabilityStatus: AvailabilityStatus
        get() = when {
            availabilityPercentage >= 0.6f -> AvailabilityStatus.HIGH
            availabilityPercentage >= 0.3f -> AvailabilityStatus.MEDIUM
            else -> AvailabilityStatus.LOW
        }

    /**
     * Check if station is operational
     * @return true if station is open and has spaces
     */
    val isOperational: Boolean
        get() = status.equals("OPEN", ignoreCase = true) && totalSpaces > 0
}

/**
 * Enum representing the availability status of bikes at a station
 */
enum class AvailabilityStatus {
    HIGH,    // Green - Many bikes available
    MEDIUM,  // Yellow - Some bikes available
    LOW      // Red - Few bikes available
}