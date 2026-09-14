package com.example.ui.theme

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = StarCyan,
    onPrimary = DeepNavy,
    primaryContainer = SurfaceNavy,
    onPrimaryContainer = StarCyan,
    secondary = StarGold,
    onSecondary = DeepNavy,
    secondaryContainer = Color(0xFF2C2209),
    onSecondaryContainer = StarGold,
    tertiary = StarCyanDark,
    background = DeepNavy,
    onBackground = TextPrimary,
    surface = CardNavy,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceNavy,
    onSurfaceVariant = TextSecondary,
    outline = BorderNavy
)

private val LightColorScheme = darkColorScheme( // Keep ISP high-contrast tech vibe consistent
    primary = StarCyan,
    onPrimary = DeepNavy,
    primaryContainer = SurfaceNavy,
    onPrimaryContainer = StarCyan,
    secondary = StarGold,
    onSecondary = DeepNavy,
    secondaryContainer = Color(0xFF2C2209),
    onSecondaryContainer = StarGold,
    tertiary = StarCyanDark,
    background = DeepNavy,
    onBackground = TextPrimary,
    surface = CardNavy,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceNavy,
    onSurfaceVariant = TextSecondary,
    outline = BorderNavy
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false, // Keep consistent Ten Star Net branded theme
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = DeepNavy.toArgb()
                window.navigationBarColor = DeepNavy.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
                WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
