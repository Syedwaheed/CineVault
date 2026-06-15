package com.edu.feature.watchlist.presentation

import com.edu.core.domain.movie.Movie

sealed interface WatchlistEffect {
    data class NavigateToDetail(val movieId: Int) : WatchlistEffect
    data class ShowUndoSnackbar(val movie: Movie) : WatchlistEffect
    data object BrowseMovies : WatchlistEffect
}
