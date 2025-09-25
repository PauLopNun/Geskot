package com.geskot.app.presentation.components

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.geskot.app.data.model.ValenbisiStation
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants

/**
 * OpenStreetMap implementation for displaying Valenbisi stations
 * Free alternative to Google Maps - no API key required
 */
@Composable
fun OpenStreetMapView(
    station: ValenbisiStation,
    modifier: Modifier = Modifier
) {

    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { ctx ->
            // Initialize OSMDroid configuration with better settings
            Configuration.getInstance().apply {
                load(ctx, ctx.getSharedPreferences("osmdroid", Context.MODE_PRIVATE))
                userAgentValue = ctx.packageName
                cacheMapTileCount = 12
                cacheMapTileOvershoot = 2
                tileDownloadThreads = 2
                tileFileSystemThreads = 1
                // Reduce tile download frequency
                expirationExtendedDuration = 1000L * 60L * 60L * 24L * 7L // 7 days
            }

            MapView(ctx).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)

                // Optimize hardware acceleration
                setLayerType(android.view.View.LAYER_TYPE_HARDWARE, null)

                // Set initial position and zoom level
                val stationPoint = GeoPoint(station.latitude, station.longitude)
                controller.setZoom(16.0)
                controller.setCenter(stationPoint)

                // Add marker for the station
                val marker = Marker(this).apply {
                    position = stationPoint
                    title = station.name
                    snippet = "${station.availableBikes} bicis disponibles"
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                }
                overlays.add(marker)

                // Enable zoom controls with reduced visibility time
                zoomController.setVisibility(org.osmdroid.views.CustomZoomButtonsController.Visibility.SHOW_AND_FADEOUT)

                // Optimize tile loading
                isTilesScaledToDpi = true
                isHorizontalMapRepetitionEnabled = false
                isVerticalMapRepetitionEnabled = false

                // Set reasonable zoom limits to reduce memory usage
                minZoomLevel = 10.0
                maxZoomLevel = 19.0
            }
        },
        update = { mapView ->
            try {
                val stationPoint = GeoPoint(station.latitude, station.longitude)
                mapView.controller.animateTo(stationPoint)

                // Update marker if station data changes - only if needed
                if (mapView.overlays.isEmpty() || mapView.overlays.size != 1) {
                    mapView.overlays.clear()
                    val marker = Marker(mapView).apply {
                        position = stationPoint
                        title = station.name
                        snippet = "${station.availableBikes} bicis disponibles"
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    }
                    mapView.overlays.add(marker)
                    mapView.invalidate()
                } else {
                    // Just update the existing marker
                    (mapView.overlays[0] as? Marker)?.apply {
                        position = stationPoint
                        title = station.name
                        snippet = "${station.availableBikes} bicis disponibles"
                    }
                }
            } catch (e: Exception) {
                // Ignore map update errors to prevent crashes
            }
        }
    )
}