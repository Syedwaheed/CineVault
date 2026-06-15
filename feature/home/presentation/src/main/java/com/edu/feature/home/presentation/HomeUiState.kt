package com.edu.feature.home.presentation

import com.edu.core.domain.movie.Movie
import com.edu.core.presentation.designsystem.components.SyncState
import com.edu.core.presentation.ui.UiText

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(
        val trendingMovies: List<Movie>,
        val topRatedMovies: List<Movie>,
        val syncState: SyncState,
    ) : HomeUiState
    data class Error(val message: UiText) : HomeUiState
}
