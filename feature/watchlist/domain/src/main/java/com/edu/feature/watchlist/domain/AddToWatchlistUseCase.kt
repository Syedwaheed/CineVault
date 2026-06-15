package com.edu.feature.watchlist.domain

import com.edu.core.domain.movie.Movie
import com.edu.core.domain.movie.WatchlistRepository
import com.edu.core.domain.util.SuspendUseCase

class AddToWatchlistUseCase(
    private val repository: WatchlistRepository,
) : SuspendUseCase<Movie, Unit> {
    override suspend fun invoke(params: Movie) =
        repository.addToWatchlist(params)
}
