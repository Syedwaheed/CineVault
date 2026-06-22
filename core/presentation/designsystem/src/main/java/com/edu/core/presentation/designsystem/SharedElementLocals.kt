package com.edu.core.presentation.designsystem

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.compositionLocalOf

@OptIn(ExperimentalSharedTransitionApi::class)
val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope?> { null }

/** Safe nullable wrapper around Navigation 3's LocalNavAnimatedContentScope.
 *  Use this instead of LocalNavAnimatedContentScope.current directly — that throws
 *  when accessed in an OverlayScene (e.g. HomeRoute behind LoginRoute). */
val LocalAnimatedVisibilityScope = compositionLocalOf<AnimatedVisibilityScope?> { null }