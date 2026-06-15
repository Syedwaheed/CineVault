package com.edu.feature.search.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable

/**
 * Top-level composable representing the Search destination in the app's NavHost.
 *
 * Usage inside `:app`'s `entryProvider { }` block:
 * ```kotlin
 * entry<SearchRoute> {
 *     SearchDestination(
 *         imageBaseUrl = networkConfig.imageBaseUrl,
 *         contentPadding = paddingValues,
 *         onNavigateToDetail = { id -> navigator.push(DetailRoute(id)) },
 *     )
 * }
 * ```
 *
 * Route key: `SearchRoute` (defined in `:app:navigation.AppRoute`).
 * Bottom nav: yes — Search tab (index 1).
 */
@Composable
fun SearchDestination(
    imageBaseUrl: String,
    onNavigateToDetail: (Int) -> Unit,
    contentPadding: PaddingValues = PaddingValues(),
) {
    SearchScreen(
        imageBaseUrl = imageBaseUrl,
        onNavigateToDetail = onNavigateToDetail,
        contentPadding = contentPadding,
    )
}
