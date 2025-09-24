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

/**
 * OpenStreetMap implementation for displaying Valenbisi stations
 * Free alternative to Google Maps - no API key required
 */
@Composable
fun OpenStreetMapView(
    station: ValenbisiStation,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { ctx ->
            // Initialize OSMDroid configuration
            Configuration.getInstance().load(ctx, ctx.getSharedPreferences("osmdroid", Context.MODE_PRIVATE))

            MapView(ctx).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)

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

                // Enable zoom controls
                zoomController.setVisibility(org.osmdroid.views.CustomZoomButtonsController.Visibility.SHOW_AND_FADEOUT)
            }
        },
        update = { mapView ->
            val stationPoint = GeoPoint(station.latitude, station.longitude)
            mapView.controller.animateTo(stationPoint)

            // Update marker if station data changes
            mapView.overlays.clear()
            val marker = Marker(mapView).apply {
                position = stationPoint
                title = station.name
                snippet = "${station.availableBikes} bicis disponibles"
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            }
            mapView.overlays.add(marker)
            mapView.invalidate()
        }
    )
}