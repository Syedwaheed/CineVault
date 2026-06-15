package com.edu.feature.search.presentation

import com.edu.core.domain.movie.Movie
import com.edu.core.presentation.ui.UiText

sealed interface SearchUiState {
    data object Idle : SearchUiState

    data class Loading(
        val query: String,
        val selectedGenre: String,
    ) : SearchUiState

    data class Success(
        val query: String,
        val results: List<Movie>,
        val selectedGenre: String,
        val isSearching: Boolean,
    ) : SearchUiState

    data class Error(
        val query: String,
        val selectedGenre: String,
        val message: UiText,
    ) : SearchUiState
}
