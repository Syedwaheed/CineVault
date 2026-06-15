package com.edu.feature.home.domain

import com.edu.core.domain.movie.MovieRepository
import com.edu.core.domain.movie.SyncStatus
import com.edu.core.domain.util.FlowUseCase
import com.edu.core.domain.util.NoParams
import kotlinx.coroutines.flow.Flow

class SyncMoviesUseCase(
    private val repository: MovieRepository,
) : FlowUseCase<NoParams, SyncStatus> {
    override fun invoke(params: NoParams): Flow<SyncStatus> =
        repository.syncMovies()
}
