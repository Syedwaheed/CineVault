package com.edu.core.presentation.designsystem.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.edu.core.presentation.designsystem.CineVaultAnimation
import com.edu.core.presentation.designsystem.CineVaultSpacing
import com.edu.core.presentation.designsystem.CineVaultTheme
import com.edu.core.presentation.designsystem.CvDanger
import com.edu.core.presentation.designsystem.icon.cloud_image

// ---------------------------------------------------------------------------
// SyncState — display-only sealed interface
//
// Lives in the design system so SyncStatusIndicator is self-contained.
// Feature screens map their domain SyncStatus (from :core:data) to SyncState
// before passing it into a CenterAlignedTopAppBar's trailing slot.
//
// Domain → Display mapping (done in the ViewModel / Screen):
//   domain.Idle    → SyncState.Idle
//   domain.Syncing → SyncState.Syncing
//   domain.Success → SyncState.Idle   (success is a transient state)
//   domain.Error   → SyncState.Error
// ---------------------------------------------------------------------------

/**
 * Display-only sync states for [SyncStatusIndicator].
 * See [SyncStatusIndicator] for usage.
 */
sealed interface SyncState {
    /** No ongoing sync; indicator is invisible. */
    data object Idle : SyncState

    /** Sync in progress; spinner is shown. */
    data object Syncing : SyncState

    /** Last sync failed; error icon is shown. */
    data object Error : SyncState
}

// ---------------------------------------------------------------------------
// SyncStatusIndicator
// ---------------------------------------------------------------------------

/**
 * Compact sync indicator designed for the trailing slot of a
 * [androidx.compose.material3.CenterAlignedTopAppBar] (CLAUDE.md rule).
 *
 * States:
 * - [SyncState.Idle]    → 24 dp invisible spacer (slot preserved for stable layout)
 * - [SyncState.Syncing] → pulsing [CircularProgressIndicator] in primary amber
 * - [SyncState.Error]   → cloud-off icon in [com.edu.core.presentation.designsystem.CvDanger]
 *
 * Transitions between states use [com.edu.core.presentation.designsystem.CineVaultAnimation.shortSpec] (150 ms fade +
 * vertical slide) to avoid jarring jumps in the AppBar.
 *
 * Usage:
 * ```
 * CenterAlignedTopAppBar(
 *     actions = {
 *         SyncStatusIndicator(state = syncState)
 *     }
 * )
 * ```
 */
@Composable
fun SyncStatusIndicator(
    state: SyncState,
    modifier: Modifier = Modifier,
) {
    AnimatedContent(
        targetState  = state,
        transitionSpec = {
            (fadeIn(animationSpec = CineVaultAnimation.shortSpec()) +
                    slideInVertically(animationSpec = CineVaultAnimation.shortSpec()) { -it / 2 })
                .togetherWith(
                    fadeOut(animationSpec = CineVaultAnimation.shortSpec()) +
                            slideOutVertically(animationSpec = CineVaultAnimation.shortSpec()) { it / 2 },
                )
        },
        label    = "syncStatus",
        modifier = modifier,
    ) { syncState ->
        Box(
            contentAlignment = Alignment.Center,
            modifier         = Modifier.size(24.dp),
        ) {
            when (syncState) {
                SyncState.Idle    -> { /* invisible — 24 dp slot keeps the AppBar stable */ }
                SyncState.Syncing -> SyncingSpinner()
                SyncState.Error   -> SyncErrorIcon()
            }
        }
    }
}

// ── Private sub-composables ───────────────────────────────────────────────

@Composable
private fun SyncingSpinner() {
    val transition = rememberInfiniteTransition(label = "syncPulse")
    val alpha by transition.animateFloat(
        initialValue  = 0.35f,
        targetValue   = 1.00f,
        animationSpec = infiniteRepeatable(
            animation  = tween(durationMillis = CineVaultAnimation.DURATION_MEDIUM),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "syncAlpha",
    )
    CircularProgressIndicator(
        modifier   = Modifier.size(18.dp),
        color      = MaterialTheme.colorScheme.primary.copy(alpha = alpha),
        strokeWidth = 2.dp,
        strokeCap  = StrokeCap.Round,
        trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
    )
}

@Composable
private fun SyncErrorIcon() {
    Icon(
        imageVector        = cloud_image,
        contentDescription = "Sync error — tap to retry",
        tint               = CvDanger,
        modifier           = Modifier.size(20.dp),
    )
}

// ---------------------------------------------------------------------------
// Previews
// ---------------------------------------------------------------------------

@Preview(showBackground = true, backgroundColor = 0xFF0A0A0A)
@Composable
private fun SyncStatusIndicatorPreview() {
    CineVaultTheme {
        Column(
            modifier = Modifier.padding(CineVaultSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(CineVaultSpacing.lg),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SyncStatusIndicator(state = SyncState.Idle)
            SyncStatusIndicator(state = SyncState.Syncing)
            SyncStatusIndicator(state = SyncState.Error)
        }
    }
}
