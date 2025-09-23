package com.geskot.app.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * Light color scheme for the app
 */
private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    primaryContainer = Primary.copy(alpha = 0.1f),
    onPrimaryContainer = Primary,
    secondary = Secondary,
    onSecondary = OnSecondary,
    secondaryContainer = Secondary.copy(alpha = 0.1f),
    onSecondaryContainer = Secondary,
    tertiary = AvailabilityHigh,
    onTertiary = OnPrimary,
    tertiaryContainer = AvailabilityHigh.copy(alpha = 0.1f),
    onTertiaryContainer = AvailabilityHigh,
    error = Error,
    onError = OnError,
    errorContainer = Error.copy(alpha = 0.1f),
    onErrorContainer = Error,
    background = Background,
    onBackground = OnBackground,
    surface = Surface,
    onSurface = OnSurface,
    surfaceVariant = CardBackground,
    onSurfaceVariant = TextSecondary,
    outline = Divider,
    outlineVariant = Divider.copy(alpha = 0.5f),
    scrim = OnSurface.copy(alpha = 0.5f)
)

/**
 * Dark color scheme for the app
 */
private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    primaryContainer = DarkPrimary.copy(alpha = 0.2f),
    onPrimaryContainer = DarkPrimary,
    secondary = DarkSecondary,
    onSecondary = DarkOnSecondary,
    secondaryContainer = DarkSecondary.copy(alpha = 0.2f),
    onSecondaryContainer = DarkSecondary,
    tertiary = AvailabilityHigh,
    onTertiary = DarkOnPrimary,
    tertiaryContainer = AvailabilityHigh.copy(alpha = 0.2f),
    onTertiaryContainer = AvailabilityHigh,
    error = DarkError,
    onError = DarkOnError,
    errorContainer = DarkError.copy(alpha = 0.2f),
    onErrorContainer = DarkError,
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkCardBackground,
    onSurfaceVariant = DarkTextSecondary,
    outline = DarkDivider,
    outlineVariant = DarkDivider.copy(alpha = 0.5f),
    scrim = DarkOnSurface.copy(alpha = 0.5f)
)

/**
 * Main theme composable for the GesKot app
 *
 * @param darkTheme Whether to use dark theme
 * @param dynamicColor Whether to use dynamic colors (Android 12+)
 * @param content The content to theme
 */
@Composable
fun GesKotTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}