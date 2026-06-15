package com.edu.feature.detail.presentation

import com.edu.core.domain.movie.Actor
import com.edu.core.domain.movie.Movie
import com.edu.core.presentation.ui.UiText

sealed interface DetailUiState {
    data object Loading : DetailUiState
    data class Success(
        val movie: Movie,
        val cast: List<Actor>,
        val similarMovies: List<Movie>,
        val isCastLoading: Boolean,
        val isInWatchlist: Boolean = false,
    ) : DetailUiState
    data class Error(val message: UiText) : DetailUiState
}