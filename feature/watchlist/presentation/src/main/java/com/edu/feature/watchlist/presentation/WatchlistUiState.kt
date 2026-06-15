package com.edu.feature.watchlist.presentation

import com.edu.core.domain.movie.Movie
import com.edu.core.presentation.ui.UiText

sealed interface WatchlistUiState {
    data object Loading : WatchlistUiState
    data class Success(val movies: List<Movie>) : WatchlistUiState
    data object Empty : WatchlistUiState
    data class Error(val message: UiText) : WatchlistUiState
}
