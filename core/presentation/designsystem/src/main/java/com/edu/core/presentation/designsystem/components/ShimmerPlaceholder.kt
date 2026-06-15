package com.edu.core.presentation.designsystem.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.edu.core.presentation.designsystem.CineVaultAnimation
import com.edu.core.presentation.designsystem.CineVaultRadius
import com.edu.core.presentation.designsystem.CineVaultSpacing
import com.edu.core.presentation.designsystem.CineVaultTheme
import com.edu.core.presentation.designsystem.CvSurface
import com.edu.core.presentation.designsystem.CvSurfaceHi

// ---------------------------------------------------------------------------
// shimmerEffect — Modifier extension
//
// Draws a horizontally sweeping shimmer animation using the CineVault surface
// palette, clipped to the given corner radius.
//
// CLAUDE.md rule: shimmer must match the exact shape of the real content it
// replaces. Callers pass the appropriate [cornerRadius]:
//   - poster / large surfaces → CineVaultRadius.md (12 dp, default)
//   - small text lines         → CineVaultRadius.sm (8 dp)
//
// Implementation uses DrawScope.size so no onSizeChanged callback is needed.
// The shimmer sweeps from left to right in DURATION_LONG (500 ms).
// ---------------------------------------------------------------------------

/**
 * Applies a left-to-right shimmer sweep behind the composable.
 *
 * Usage:
 * ```
 * Box(modifier = Modifier.fillMaxWidth().aspectRatio(2f / 3f).shimmerEffect())
 * Box(modifier = Modifier.fillMaxWidth(0.7f).height(14.dp).shimmerEffect(CineVaultRadius.sm))
 * ```
 */
fun Modifier.shimmerEffect(
    cornerRadius: Dp = CineVaultRadius.md,
): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val progress by transition.animateFloat(
        initialValue  = 0f,
        targetValue   = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = CineVaultAnimation.DURATION_LONG,
                easing         = LinearEasing,
            ),
        ),
        label = "shimmer_progress",
    )
    drawBehind {
        // The highlight band is as wide as the composable itself.
        // At progress=0 the band is fully off-screen left; at 1 it is fully
        // off-screen right — a clean, seamless left-to-right sweep.
        val sweepWidth = size.width
        val startX     = progress * (size.width + sweepWidth) - sweepWidth
        drawRoundRect(
            brush = Brush.linearGradient(
                colors = listOf(CvSurface, CvSurfaceHi, CvSurface),
                start  = Offset(startX, 0f),
                end    = Offset(startX + sweepWidth, 0f),
            ),
            cornerRadius = CornerRadius(cornerRadius.toPx()),
        )
    }
}

// ---------------------------------------------------------------------------
// Preview
// ---------------------------------------------------------------------------

@Preview(showBackground = true, backgroundColor = 0xFF0A0A0A)
@Composable
private fun ShimmerEffectPreview() {
    CineVaultTheme {
        Box(
            modifier = Modifier
                .width(CineVaultSpacing.xxxl * 4)   // 128 dp
                .height(CineVaultSpacing.xxxl * 6)  // 192 dp
                .shimmerEffect(),
        )
    }
}
