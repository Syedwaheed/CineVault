package com.edu.feature.watchlist.presentation

import com.edu.core.domain.movie.Movie

sealed interface WatchlistAction {
    data class MovieClicked(val movieId: Int) : WatchlistAction
    data class RemoveMovie(val movie: Movie) : WatchlistAction
    data object UndoRemove : WatchlistAction
    data object BrowseMovies : WatchlistAction
}
