package com.edu.movieapplication.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.edu.auth.presentation.login.LoginScreen
import com.edu.core.domain.auth.SessionStorage
import com.edu.core.network.NetworkConfig
import com.edu.core.presentation.designsystem.CineVaultAnimation
import com.edu.core.presentation.designsystem.CineVaultTheme
import com.edu.core.presentation.designsystem.CvAmber
import com.edu.core.presentation.designsystem.CvAmberSoft
import com.edu.core.presentation.designsystem.CvTextMute
import com.edu.core.presentation.designsystem.LocalSharedTransitionScope
import com.edu.feature.detail.presentation.DetailScreen
import com.edu.feature.home.presentation.HomeScreen
import com.edu.feature.profile.presentation.ProfileDestination
import com.edu.feature.search.presentation.SearchDestination
import com.edu.feature.watchlist.presentation.WatchlistDestination
import org.koin.compose.koinInject

@Composable
fun CineVaultNavHost() {
    val sessionStorage: SessionStorage = koinInject()
    val networkConfig: NetworkConfig = koinInject()

    val navigator = remember {
        CineVaultNavigator(isLoggedIn = { sessionStorage.isLoggedIn() })
    }

    val showBottomBar by remember { derivedStateOf { navigator.showBottomBar } }

    // Scroll-aware bottom bar: collapse to icons-only on scroll-down, expand on scroll-up
    var isBarCollapsed by remember { mutableStateOf(false) }

    // Always expand when returning from a full-screen route or switching tabs
    LaunchedEffect(showBottomBar) { if (showBottomBar) isBarCollapsed = false }
    LaunchedEffect(navigator.topLevelKey) { isBarCollapsed = false }

    val scrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (available.y < -5f) isBarCollapsed = true
                else if (available.y > 5f) isBarCollapsed = false
                return Offset.Zero
            }
        }
    }

    CineVaultTheme(darkTheme = isSystemInDarkTheme()) {
        @OptIn(ExperimentalSharedTransitionApi::class)
        SharedTransitionLayout {
        CompositionLocalProvider(LocalSharedTransitionScope provides this) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollConnection),
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
                        isCollapsed = isBarCollapsed && navigator.topLevelKey is HomeRoute,
                    )
                }
            },
        ) { paddingValues ->

            NavDisplay(
                backStack = navigator.backStack,
                onBack = { navigator.goBack() },
                entryProvider = entryProvider {

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
                        )
                    }

                    entry<SearchRoute> {
                        SearchDestination(
                            imageBaseUrl = networkConfig.imageBaseUrl,
                            contentPadding = paddingValues,
                            onNavigateToDetail = { movieId ->
                                navigator.push(DetailRoute(movieId))
                            },
                        )
                    }

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

                    entry<ProfileRoute> {
                        ProfileDestination(
                            imageBaseUrl = networkConfig.imageBaseUrl,
                            contentPadding = paddingValues,
                            onLogout = {
                                navigator.onLogout()
                            },
                        )
                    }

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


// ─── Glassmorphic bottom navigation bar ──────────────────────────────────────

@Composable
private fun CineVaultBottomBar(
    currentRoute: AppRoute,
    onTabSelected: (AppRoute) -> Unit,
    isCollapsed: Boolean = false,
) {
    val selectedIndex = remember(currentRoute) {
        bottomNavItems.indexOfFirst { it.route == currentRoute }.coerceAtLeast(0)
    }

    // Glass pill shrinks to 1/N of its width (home slot only) when collapsed
    val glassFraction by animateFloatAsState(
        targetValue = if (isCollapsed) 1f / bottomNavItems.size else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium,
        ),
        label = "glass_fraction",
    )
    // Labels and non-home icons fade out independently
    val labelAlpha by animateFloatAsState(
        targetValue = if (isCollapsed) 0f else 1f,
        animationSpec = tween(CineVaultAnimation.DURATION_SHORT, easing = CineVaultAnimation.EaseOut),
        label = "label_alpha",
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
    ) {
        // Outer container sets the fixed bar frame (full width, fixed height, inset padding)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp)
                .height(64.dp),
        ) {
            // ── Layer 1: glass pill background — animates width left→right ──
            Box(
                modifier = Modifier
                    .fillMaxWidth(glassFraction)
                    .fillMaxHeight()
                    .drawBehind {
                        val radius = CornerRadius(32.dp.toPx())
                        drawRoundRect(color = Color(0xD0141414), cornerRadius = radius)
                        drawRoundRect(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color(0x22FFFFFF), Color(0x00FFFFFF)),
                                startY = 0f,
                                endY = size.height * 0.45f,
                            ),
                            cornerRadius = radius,
                        )
                        drawRoundRect(
                            color = Color(0x33FFFFFF),
                            cornerRadius = radius,
                            style = Stroke(width = 1.dp.toPx()),
                        )
                    },
            )

            // ── Layer 2: icons — full-width Row, non-home items fade when collapsed ──
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                bottomNavItems.forEachIndexed { index, item ->
                    val isSelected = index == selectedIndex
                    val isHomeItem = index == 0

                    // Only home stays visible when collapsed; all others fade out
                    val itemAlpha by animateFloatAsState(
                        targetValue = if (isCollapsed && !isHomeItem) 0f else 1f,
                        animationSpec = tween(
                            durationMillis = CineVaultAnimation.DURATION_SHORT,
                            easing = CineVaultAnimation.EaseOut,
                        ),
                        label = "item_alpha_$index",
                    )

                    // Icon punch-and-spring on selection
                    val iconScale = remember { Animatable(1f) }
                    LaunchedEffect(isSelected) {
                        if (isSelected) {
                            iconScale.animateTo(
                                targetValue = 1.35f,
                                animationSpec = tween(CineVaultAnimation.DURATION_SHORT, easing = CineVaultAnimation.EaseOut),
                            )
                            iconScale.animateTo(
                                targetValue = 1f,
                                animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMediumLow),
                            )
                        } else {
                            iconScale.animateTo(
                                targetValue = 1f,
                                animationSpec = tween(CineVaultAnimation.DURATION_SHORT, easing = CineVaultAnimation.EaseOut),
                            )
                        }
                    }

                    val iconTint by animateColorAsState(
                        targetValue = if (isSelected) CvAmber else CvTextMute,
                        animationSpec = tween(CineVaultAnimation.DURATION_SHORT, easing = CineVaultAnimation.EaseOut),
                        label = "icon_tint_$index",
                    )

                    // Amber pill bloom on selection
                    val pillScale = remember { Animatable(if (isSelected) 1f else 0f) }
                    val pillAlpha by animateFloatAsState(
                        targetValue = if (isSelected) 1f else 0f,
                        animationSpec = tween(CineVaultAnimation.DURATION_SHORT, easing = CineVaultAnimation.EaseOut),
                        label = "pill_alpha_$index",
                    )
                    LaunchedEffect(isSelected) {
                        if (isSelected) {
                            pillScale.animateTo(
                                targetValue = 1.25f,
                                animationSpec = tween(CineVaultAnimation.DURATION_SHORT, easing = CineVaultAnimation.EaseOut),
                            )
                            pillScale.animateTo(
                                targetValue = 1f,
                                animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMedium),
                            )
                        } else {
                            pillScale.animateTo(
                                targetValue = 0f,
                                animationSpec = tween(CineVaultAnimation.DURATION_SHORT, easing = CineVaultAnimation.EaseOut),
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .graphicsLayer { alpha = itemAlpha }
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() },
                                onClick = dropUnlessResumed { onTabSelected(item.route) },
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Box(
                            modifier = Modifier.size(width = 52.dp, height = 30.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .graphicsLayer {
                                        alpha = pillAlpha
                                        scaleX = pillScale.value
                                        scaleY = pillScale.value
                                    }
                                    .clip(RoundedCornerShape(15.dp))
                                    .background(CvAmberSoft),
                            )
                            Icon(
                                painter = painterResource(
                                    if (isSelected) item.selectedIcon else item.unselectedIcon,
                                ),
                                contentDescription = item.label,
                                tint = iconTint,
                                modifier = Modifier
                                    .size(22.dp)
                                    .graphicsLayer {
                                        scaleX = iconScale.value
                                        scaleY = iconScale.value
                                    },
                            )
                        }

                        Spacer(Modifier.height(3.dp))

                        Text(
                            text = item.label,
                            style = MaterialTheme.typography.labelSmall,
                            color = iconTint,
                            modifier = Modifier.graphicsLayer { alpha = if (isHomeItem) 1f else labelAlpha },
                        )
                    }
                }
            }
        }
    }
}
// ─── Previews ────────────────────────────────────────────────────────────────

@Preview(showBackground = true, backgroundColor = 0xFF0A0A0A)
@Composable
private fun CineVaultBottomBarPreview() {
    CineVaultTheme {
        CineVaultBottomBar(
            currentRoute = HomeRoute,
            onTabSelected = {},
            isCollapsed = false
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A0A)
@Composable
private fun CineVaultBottomBarCollapsedPreview() {
    CineVaultTheme {
        CineVaultBottomBar(
            currentRoute = HomeRoute,
            onTabSelected = {},
            isCollapsed = true
        )
    }
}
