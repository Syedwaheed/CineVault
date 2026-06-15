package com.edu.feature.search.presentation

sealed interface SearchEffect {
    data class NavigateToDetail(val movieId: Int) : SearchEffect
}
