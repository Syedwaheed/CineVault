package com.edu.movieapplication.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * Marker interface — any route implementing this triggers the auth intercept before navigation.
 */
interface RequiresLogin

@Serializable
sealed class AppRoute : NavKey

// ─── Bottom nav tabs ──────────────────────────────────────────────────────────

@Serializable data object HomeRoute : AppRoute()

@Serializable data object SearchRoute : AppRoute()

/** Requires TMDB session — navigating here without auth redirects to Login. */
@Serializable data object WatchlistRoute : AppRoute(), RequiresLogin

/** Requires TMDB session — navigating here without auth redirects to Login. */
@Serializable data object ProfileRoute : AppRoute(), RequiresLogin

// ─── Deep destinations ────────────────────────────────────────────────────────

@Serializable data class DetailRoute(val movieId: Int) : AppRoute()

// ─── Auth ─────────────────────────────────────────────────────────────────────

/**
 * @param redirectToKey where to navigate after a successful login.
 *   null = stay on current screen (e.g. if user manually tapped "Sign in").
 */
@Serializable data class LoginRoute(val redirectToKey: AppRoute? = null) : AppRoute()

// ─── Convenience sets ─────────────────────────────────────────────────────────

val TOP_LEVEL_ROUTES: Set<AppRoute> = setOf(HomeRoute, SearchRoute, WatchlistRoute, ProfileRoute)