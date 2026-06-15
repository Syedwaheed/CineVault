package com.edu.feature.watchlist.domain

import com.edu.core.domain.movie.WatchlistRepository
import com.edu.core.domain.util.SuspendUseCase

class RemoveFromWatchlistUseCase(
    private val repository: WatchlistRepository,
) : SuspendUseCase<Int, Unit> {
    override suspend fun invoke(params: Int) =
        repository.removeFromWatchlist(params)
}
