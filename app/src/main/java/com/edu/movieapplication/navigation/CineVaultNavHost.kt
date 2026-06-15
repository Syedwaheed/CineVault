package com.edu.movieapplication.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.edu.auth.presentation.login.LoginScreen
import com.edu.core.domain.auth.SessionStorage
import com.edu.core.network.NetworkConfig
import com.edu.core.presentation.designsystem.CineVaultAnimation
import com.edu.core.presentation.designsystem.CineVaultTheme
import com.edu.core.presentation.designsystem.LocalSharedTransitionScope
import com.edu.feature.detail.presentation.DetailScreen
import com.edu.feature.home.presentation.HomeScreen
import com.edu.feature.profile.presentation.ProfileDestination
import com.edu.feature.search.presentation.SearchDestination
import com.edu.feature.watchlist.presentation.WatchlistDestination
import org.koin.compose.koinInject
import androidx.compose.ui.res.painterResource

@Composable
fun CineVaultNavHost() {
    val sessionStorage: SessionStorage = koinInject()
    val networkConfig: NetworkConfig = koinInject()

    // Navigator is remembered for the lifetime of this composition (= Activity lifetime).
    // isLoggedIn lambda is always evaluated fresh so post-login state is reflected immediately.
    val navigator = remember {
        CineVaultNavigator(isLoggedIn = { sessionStorage.isLoggedIn() })
    }

    val showBottomBar by remember { derivedStateOf { navigator.showBottomBar } }

    CineVaultTheme(darkTheme = isSystemInDarkTheme()) {
        @OptIn(ExperimentalSharedTransitionApi::class)
        SharedTransitionLayout {
        CompositionLocalProvider(LocalSharedTransitionScope provides this) {
        Scaffold(
            contentWindowInsets = WindowInsets(0),
            bottomBar = {
                AnimatedVisibility(
                    visible = showBottomBar,
                    enter = slideInVertically(tween(CineVaultAnimation.DURATION_MEDIUM)) { it } +
                            fadeIn(tween(CineVaultAnimation.DURATION_MEDIUM)),
                    exit = slideOutVertically(tween(CineVaultAnimation.DURATION_MEDIUM)) { it } +
                            fadeOut(tween(CineVaultAnimation.DURATION_MEDIUM)),
                ) {
                    CineVaultBottomBar(
                        currentRoute = navigator.topLevelKey,
                        onTabSelected = navigator::selectTab,
                    )
                }
            },
        ) { paddingValues ->

            // ── NavDisplay renders whatever is at the top of the flattened backstack ──
            NavDisplay(
                backStack = navigator.backStack,
                onBack = { navigator.goBack() },
                entryProvider = entryProvider {

                    // ─── Home tab ─────────────────────────────────────────────
                    entry<HomeRoute> {
                        HomeScreen(
                            contentPadding = paddingValues,
                            imageBaseUrl = networkConfig.imageBaseUrl,
                            onNavigateToDetail = { movieId ->
                                navigator.push(DetailRoute(movieId))
                            },
                            onNavigateToSearch = {
                                navigator.selectTab(SearchRoute)
                            },
                            onRequireAuth = {
                                navigator.push(LoginRoute())
                            },
                        )
                    }

                    // ─── Search tab ───────────────────────────────────────────
                    entry<SearchRoute> {
                        SearchDestination(
                            imageBaseUrl = networkConfig.imageBaseUrl,
                            contentPadding = paddingValues,
                            onNavigateToDetail = { movieId ->
                                navigator.push(DetailRoute(movieId))
                            },
                        )
                    }

                    // ─── Watchlist tab ────────────────────────────────────────────
                    entry<WatchlistRoute> {
                        WatchlistDestination(
                            imageBaseUrl = networkConfig.imageBaseUrl,
                            contentPadding = paddingValues,
                            onNavigateToDetail = { movieId ->
                                navigator.push(DetailRoute(movieId))
                            },
                            onBrowseMovies = {
                                navigator.selectTab(SearchRoute)
                            },
                        )
                    }

                    // ─── Profile tab (requires login — navigator handles redirect) ──
                    entry<ProfileRoute> {
                        ProfileDestination(
                            imageBaseUrl = networkConfig.imageBaseUrl,
                            contentPadding = paddingValues,
                            onLogout = {
                                navigator.onLogout()
                            },
                        )
                    }

                    // ─── Detail (full-screen, no bottom bar) ──────────────────
                    entry<DetailRoute> { key ->
                        DetailScreen(
                            movieId = key.movieId,
                            imageBaseUrl = networkConfig.imageBaseUrl,
                            onNavigateBack = { navigator.goBack() },
                            onNavigateToDetail = { movieId -> navigator.push(DetailRoute(movieId)) },
                            onRequireAuth = {
                                navigator.push(LoginRoute(redirectToKey = DetailRoute(key.movieId)))
                            },
                        )
                    }

                    // ─── Login (full-screen, no bottom bar) ───────────────────
                    entry<LoginRoute> { key ->
                        LoginScreen(
                            onNavigateToHome = {
                                navigator.onLoginSuccess(key.redirectToKey)
                            },
                        )
                    }
                },
            )
        }
        }  // CompositionLocalProvider
        }  // SharedTransitionLayout
    }
}

// ─── Bottom navigation bar ────────────────────────────────────────────────────

@Composable
private fun CineVaultBottomBar(
    currentRoute: AppRoute,
    onTabSelected: (AppRoute) -> Unit,
) {
    NavigationBar {
        bottomNavItems.forEach { item ->
            val isSelected = currentRoute == item.route
            NavigationBarItem(
                selected = isSelected,
                onClick = dropUnlessResumed { onTabSelected(item.route) },
                icon = {
                    Icon(
                        painter = painterResource(
                            if (isSelected) item.selectedIcon else item.unselectedIcon
                        ),
                        contentDescription = item.label,
                    )
                },
                label = { Text(item.label) },
            )
        }
    }
}