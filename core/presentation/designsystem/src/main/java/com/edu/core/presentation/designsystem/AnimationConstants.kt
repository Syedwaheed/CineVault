package com.edu.core.presentation.designsystem

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween

// ---------------------------------------------------------------------------
// CineVaultAnimation — all animation constants for the design system.
//
// Rules (from CLAUDE.md):
//  • Never hardcode duration values inline — always reference these constants.
//  • Screen enter/exit: fade + slide, 300 ms, EaseInOutCubic.
//  • Hero carousel: infinite auto-scroll, 4 000 ms interval, crossfade.
//  • Shared element transitions between MovieCard → DetailScreen: 350 ms.
//  • Use animateAsState / AnimatedVisibility / AnimatedContent; never manual
//    LaunchedEffect + delay for UI animations.
// ---------------------------------------------------------------------------
object CineVaultAnimation {

    // -------------------------------------------------------------------------
    // Durations (milliseconds)
    // -------------------------------------------------------------------------

    /** 150 ms — micro interactions: ripple, icon swap. */
    const val DURATION_SHORT  = 150

    /** 300 ms — screen enter / exit transitions (CLAUDE.md spec). */
    const val DURATION_MEDIUM = 300

    /** 500 ms — large surface reveals, skeleton fade-in. */
    const val DURATION_LONG   = 500

    /** 350 ms — shared element MovieCard → DetailScreen transition. */
    const val DURATION_SHARED_ELEMENT = 350

    /** 4 000 ms — hero carousel auto-scroll interval (CLAUDE.md spec). */
    const val CAROUSEL_INTERVAL_MS = 4_000

    // -------------------------------------------------------------------------
    // Easing curves
    // -------------------------------------------------------------------------

    /**
     * EaseInOutCubic — the primary easing for all CineVault transitions.
     * CSS equivalent: cubic-bezier(0.645, 0.045, 0.355, 1.000)
     */
    val EaseInOutCubic = CubicBezierEasing(0.645f, 0.045f, 0.355f, 1.000f)

    /** EaseOutCubic — decelerate into final position; for enter transitions. */
    val EaseOut = CubicBezierEasing(0.215f, 0.610f, 0.355f, 1.000f)

    /** EaseInCubic — accelerate away; for exit transitions. */
    val EaseIn = CubicBezierEasing(0.550f, 0.055f, 0.675f, 0.190f)

    /** Linear — progress indicators, skeleton shimmer loops. */
    val Linear = CubicBezierEasing(0f, 0f, 1f, 1f)

    // -------------------------------------------------------------------------
    // Pre-built tween specs (convenience — avoids per-site tween{} boilerplate)
    // -------------------------------------------------------------------------

    /** Standard screen transition spec: 300 ms + EaseInOutCubic. */
    fun <T> screenTransitionSpec() = tween<T>(
        durationMillis = DURATION_MEDIUM,
        easing         = EaseInOutCubic,
    )

    /** Short micro-interaction spec: 150 ms + EaseOut. */
    fun <T> shortSpec() = tween<T>(
        durationMillis = DURATION_SHORT,
        easing         = EaseOut,
    )

    /** Shared element spec: 350 ms + EaseInOutCubic. */
    fun <T> sharedElementSpec() = tween<T>(
        durationMillis = DURATION_SHARED_ELEMENT,
        easing         = EaseInOutCubic,
    )
}