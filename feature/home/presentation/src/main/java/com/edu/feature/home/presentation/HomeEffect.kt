package com.edu.feature.home.presentation

sealed interface HomeEffect {
    data class NavigateToDetail(val movieId: Int) : HomeEffect
    data object NavigateToSearch : HomeEffect
}
