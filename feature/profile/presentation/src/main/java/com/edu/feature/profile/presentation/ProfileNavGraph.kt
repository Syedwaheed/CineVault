package com.edu.feature.profile.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable

/**
 * Top-level composable representing the Profile destination in the app's NavHost.
 *
 * Usage inside `:app`'s `entryProvider { }` block:
 * ```kotlin
 * entry<ProfileRoute> {
 *     ProfileDestination(
 *         imageBaseUrl = networkConfig.imageBaseUrl,
 *         contentPadding = paddingValues,
 *         onLogout = { navigator.navigateToAuthClearingBackstack() },
 *     )
 * }
 * ```
 *
 * Route key: `ProfileRoute` (defined in `:app:navigation.AppRoute`).
 * Bottom nav: yes — Profile tab (index 3).
 */
@Composable
fun ProfileDestination(
    imageBaseUrl: String,
    onLogout: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(),
) {
    ProfileScreen(
        imageBaseUrl = imageBaseUrl,
        onLogout = onLogout,
        contentPadding = contentPadding,
    )
}
