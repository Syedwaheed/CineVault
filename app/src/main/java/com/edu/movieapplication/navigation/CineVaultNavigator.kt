package com.edu.movieapplication.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList

/**
 * Manages per-tab navigation backstacks and enforces the auth intercept rule:
 * any route that implements [RequiresLogin] is silently redirected to [LoginRoute]
 * when the user is not logged in, with the intended destination stored so it can
 * be resumed after a successful login.
 *
 * All state is snapshot-backed ([mutableStateListOf], [mutableStateOf]) so
 * Compose recomposes automatically when it changes.
 */
class CineVaultNavigator(
    private val isLoggedIn: () -> Boolean,
) {
    // One backstack per top-level tab. Home is always present (exit-through-home).
    private val tabStacks: LinkedHashMap<AppRoute, SnapshotStateList<AppRoute>> = linkedMapOf(
        HomeRoute to mutableStateListOf(HomeRoute)
    )

    /** The currently visible bottom-nav tab. Drives the selected indicator. */
    var topLevelKey: AppRoute by mutableStateOf(HomeRoute)
        private set

    /** Flattened view of all tab stacks — fed directly to NavDisplay. */
    val backStack: SnapshotStateList<AppRoute> = mutableStateListOf(HomeRoute)

    // ─── Public queries ───────────────────────────────────────────────────────

    val currentRoute: AppRoute
        get() = backStack.lastOrNull() ?: HomeRoute

    /** Bottom bar hides on Login and Detail — neither belongs in the tab chrome. */
    val showBottomBar: Boolean
        get() = currentRoute !is LoginRoute && currentRoute !is DetailRoute

    // ─── Navigation actions ───────────────────────────────────────────────────

    /**
     * Switch to a bottom-nav tab.
     * If the tab requires login and the user is a guest, push Login on the current
     * tab's stack instead (so the bottom bar stays visible with the original tab selected).
     */
    fun selectTab(tab: AppRoute) {
        if (tab is RequiresLogin && !isLoggedIn()) {
            tabStacks[topLevelKey]?.add(LoginRoute(redirectToKey = tab))
            updateBackStack()
            return
        }
        if (tab == topLevelKey) return // already here — no-op

        if (tabStacks[tab] == null) {
            tabStacks[tab] = mutableStateListOf(tab)
        } else {
            // Move the existing stack to the end so it flattens last (= on top).
            tabStacks.apply { remove(tab)?.let { put(tab, it) } }
        }
        topLevelKey = tab
        updateBackStack()
    }

    /**
     * Push any non-tab destination (e.g. DetailRoute, LoginRoute triggered by an in-screen action).
     * Auth intercept applies here too.
     */
    fun push(route: AppRoute) {
        if (route is RequiresLogin && !isLoggedIn()) {
            tabStacks[topLevelKey]?.add(LoginRoute(redirectToKey = route))
        } else {
            tabStacks[topLevelKey]?.add(route)
        }
        updateBackStack()
    }

    /**
     * Called by LoginScreen when login succeeds.
     * Removes the LoginRoute from the stack, then resumes the pending destination.
     */
    fun onLoginSuccess(redirectTo: AppRoute?) {
        // Pop the Login screen
        tabStacks[topLevelKey]?.removeLastOrNull()

        when {
            redirectTo == null -> { /* just go back to what was underneath */ }
            redirectTo in TOP_LEVEL_ROUTES -> selectTab(redirectTo)   // tab switch that was blocked
            else -> tabStacks[topLevelKey]?.add(redirectTo)           // push the intended screen
        }
        updateBackStack()
    }

    /**
     * Called after logout. Clears all tab stacks and presents Login on top of Home.
     * After a successful login, [onLoginSuccess] navigates back to Home with no redirect.
     */
    fun onLogout() {
        tabStacks.clear()
        tabStacks[HomeRoute] = mutableStateListOf(HomeRoute, LoginRoute())
        topLevelKey = HomeRoute
        updateBackStack()
    }

    /** Standard back press. Exits the tab if at its root; then falls back through remaining tabs. */
    fun goBack() {
        val currentStack = tabStacks[topLevelKey] ?: return
        if (currentStack.size > 1) {
            currentStack.removeLastOrNull()
        } else {
            // At the root of this tab — remove it and switch to the previous one.
            tabStacks.remove(topLevelKey)
            topLevelKey = tabStacks.keys.lastOrNull() ?: HomeRoute
        }
        updateBackStack()
    }

    // ─── Private helpers ──────────────────────────────────────────────────────

    private fun updateBackStack() {
        backStack.apply {
            clear()
            addAll(tabStacks.flatMap { it.value })
        }
    }
}