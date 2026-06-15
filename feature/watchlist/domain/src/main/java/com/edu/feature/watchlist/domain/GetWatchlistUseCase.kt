package com.edu.feature.watchlist.domain

import com.edu.core.domain.movie.Movie
import com.edu.core.domain.movie.WatchlistRepository
import com.edu.core.domain.util.FlowUseCase
import com.edu.core.domain.util.NoParams
import kotlinx.coroutines.flow.Flow

class GetWatchlistUseCase(
    private val repository: WatchlistRepository,
) : FlowUseCase<NoParams, List<Movie>> {
    override fun invoke(params: NoParams): Flow<List<Movie>> =
        repository.getWatchlist()
}
