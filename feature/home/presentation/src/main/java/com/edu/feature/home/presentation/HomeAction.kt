package com.edu.feature.home.presentation

sealed interface HomeAction {
    data object Retry : HomeAction
    data class MovieClicked(val movieId: Int) : HomeAction
    data object SearchClicked : HomeAction
}
