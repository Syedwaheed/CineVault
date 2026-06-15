package com.edu.core.presentation.designsystem

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// ---------------------------------------------------------------------------
// CineVault Material3 colour schemes
// All values are sourced from Color.kt which maps /design/tokens.jsx → roles.
// ---------------------------------------------------------------------------

private val CineVaultDarkColorScheme = darkColorScheme(
    primary            = DarkPrimary,
    onPrimary          = DarkOnPrimary,
    primaryContainer   = DarkPrimaryContainer,
    onPrimaryContainer = DarkOnPrimaryContainer,

    secondary            = DarkSecondary,
    onSecondary          = DarkOnSecondary,
    secondaryContainer   = DarkSecondaryContainer,
    onSecondaryContainer = DarkOnSecondaryContainer,

    tertiary            = DarkTertiary,
    onTertiary          = DarkOnTertiary,
    tertiaryContainer   = DarkTertiaryContainer,
    onTertiaryContainer = DarkOnTertiaryContainer,

    background         = DarkBackground,
    onBackground       = DarkOnBackground,
    surface            = DarkSurface,
    onSurface          = DarkOnSurface,
    surfaceVariant     = DarkSurfaceVariant,
    onSurfaceVariant   = DarkOnSurfaceVariant,

    error            = DarkError,
    onError          = DarkOnError,
    errorContainer   = DarkErrorContainer,
    onErrorContainer = DarkOnErrorContainer,

    outline        = DarkOutline,
    outlineVariant = DarkOutlineVariant,

    // Scrim and inverse kept at Material3 defaults for system dialogs
    scrim = Color(0xFF000000),
)

private val CineVaultLightColorScheme = lightColorScheme(
    primary            = LightPrimary,
    onPrimary          = LightOnPrimary,
    primaryContainer   = LightPrimaryContainer,
    onPrimaryContainer = LightOnPrimaryContainer,

    secondary            = LightSecondary,
    onSecondary          = LightOnSecondary,
    secondaryContainer   = LightSecondaryContainer,
    onSecondaryContainer = LightOnSecondaryContainer,

    tertiary            = LightTertiary,
    onTertiary          = LightOnTertiary,
    tertiaryContainer   = LightTertiaryContainer,
    onTertiaryContainer = LightOnTertiaryContainer,

    background         = LightBackground,
    onBackground       = LightOnBackground,
    surface            = LightSurface,
    onSurface          = LightOnSurface,
    surfaceVariant     = LightSurfaceVariant,
    onSurfaceVariant   = LightOnSurfaceVariant,

    error            = LightError,
    onError          = LightOnError,
    errorContainer   = LightErrorContainer,
    onErrorContainer = LightOnErrorContainer,

    outline        = LightOutline,
    outlineVariant = LightOutlineVariant,
)

// ---------------------------------------------------------------------------
// CineVaultTheme — root theme composable
//
// Apply at the very top of the composition tree (Activity / NavHost root).
// The app is dark-first; light mode is provided for system completeness.
// Edge-to-edge is handled here so every screen is automatically full-bleed.
// ---------------------------------------------------------------------------
@Composable
fun CineVaultTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) CineVaultDarkColorScheme else CineVaultLightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            // Bar colours are managed by the Activity via enableEdgeToEdge() —
            // the theme only controls light/dark icon tint for each theme mode.
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).apply {
                // TopAppBar containerColor is always CvBg (dark) — always use light icons.
                isAppearanceLightStatusBars     = false
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = CineVaultTypography,
        content     = content,
    )
}