package com.geskot.app.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * Shape definitions for the GesKot app
 * Following Material 3 design guidelines
 */
val Shapes = Shapes(
    // Extra small shapes - for small components
    extraSmall = RoundedCornerShape(4.dp),

    // Small shapes - for buttons, chips, and small cards
    small = RoundedCornerShape(8.dp),

    // Medium shapes - for cards and dialogs
    medium = RoundedCornerShape(12.dp),

    // Large shapes - for navigation drawers and large surfaces
    large = RoundedCornerShape(16.dp),

    // Extra large shapes - for special large components
    extraLarge = RoundedCornerShape(24.dp)
)