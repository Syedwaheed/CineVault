package com.edu.core.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * App-wide scaffold with cinematic gradient background.
 *
 * @param modifier Modifier for the scaffold
 * @param topBar Optional top app bar composable
 * @param bottomBar Optional bottom navigation bar composable
 * @param floatingActionButton Optional FAB composable
 * @param content The screen content - receives padding to account for bars
 */
@Composable
fun MovieScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    // Gradient colors - dark charcoal fading to near-black
    val gradientColors = listOf(
        Color(0xFF1A1A1A),  // Lighter charcoal at top
        Color(0xFF121212),  // Mid dark
        Color(0xFF0A0A0A)   // Near black at bottom
    )

    Scaffold(
        modifier = modifier,
        topBar = topBar,
        bottomBar = bottomBar,
        floatingActionButton = floatingActionButton,
        containerColor = Color.Transparent  // Important: transparent so gradient shows
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(colors = gradientColors)
                )
        ) {
            content(paddingValues)
        }
    }
}