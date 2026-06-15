package com.edu.feature.watchlist.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable

/**
 * Top-level composable representing the Watchlist destination in the app's NavHost.
 *
 * Usage inside `:app`'s `entryProvider { }` block:
 * ```kotlin
 * entry<WatchlistRoute> {
 *     WatchlistDestination(
 *         contentPadding = paddingValues,
 *         onNavigateToDetail = { id -> navigator.push(DetailRoute(id)) },
 *         onBrowseMovies = { navigator.selectTab(SearchRoute) },
 *     )
 * }
 * ```
 *
 * Route key: `WatchlistRoute` (defined in `:app:navigation.AppRoute`).
 * Bottom nav: yes — Watchlist tab (index 2).
 */
@Composable
fun WatchlistDestination(
    imageBaseUrl: String,
    onNavigateToDetail: (Int) -> Unit,
    onBrowseMovies: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(),
) {
    WatchlistScreen(
        imageBaseUrl = imageBaseUrl,
        onNavigateToDetail = onNavigateToDetail,
        onBrowseMovies = onBrowseMovies,
        contentPadding = contentPadding,
    )
}
