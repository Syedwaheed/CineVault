package com.edu.feature.detail.presentation

sealed interface DetailAction {
    data object Back : DetailAction
    data class SimilarMovieClicked(val movieId: Int) : DetailAction
    data object ToggleWatchlist : DetailAction
    data object WatchTrailer : DetailAction
}