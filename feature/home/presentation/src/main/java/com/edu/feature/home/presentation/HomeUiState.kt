package com.edu.feature.home.presentation

import com.edu.core.domain.movie.Movie
import com.edu.core.presentation.designsystem.components.SyncState
import com.edu.core.presentation.ui.UiText
import kotlinx.collections.immutable.ImmutableList

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(
        val trendingMovies: ImmutableList<Movie>,
        val topRatedMovies: ImmutableList<Movie>,
        val nowPlayingMovies: ImmutableList<Movie>,
        val upcomingMovies: ImmutableList<Movie>,
        val syncState: SyncState,
    ) : HomeUiState
    data class Error(val message: UiText) : HomeUiState
}