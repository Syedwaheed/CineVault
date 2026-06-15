package com.edu.movieapplication.navigation

import androidx.annotation.DrawableRes
import com.edu.movieapplication.R

data class BottomNavItem(
    val route: AppRoute,
    val label: String,
    @param:DrawableRes val selectedIcon: Int,
    @param:DrawableRes val unselectedIcon: Int,
)

val bottomNavItems: List<BottomNavItem> = listOf(
    BottomNavItem(
        route = HomeRoute,
        label = "Home",
        selectedIcon = R.drawable.ic_home_filled,
        unselectedIcon = R.drawable.ic_home_outlined,
    ),
    BottomNavItem(
        route = SearchRoute,
        label = "Search",
        selectedIcon = R.drawable.ic_search_outlined,
        unselectedIcon = R.drawable.ic_search_outlined,
    ),
    BottomNavItem(
        route = WatchlistRoute,
        label = "Watchlist",
        selectedIcon = R.drawable.ic_bookmarks_filled,
        unselectedIcon = R.drawable.ic_bookmarks_outlined,
    ),
    BottomNavItem(
        route = ProfileRoute,
        label = "Profile",
        selectedIcon = R.drawable.ic_person_filled,
        unselectedIcon = R.drawable.ic_person_outlined,
    ),
)