package com.edu.feature.home.domain

import com.edu.core.domain.movie.Movie
import com.edu.core.domain.movie.MovieRepository
import com.edu.core.domain.util.FlowUseCase
import com.edu.core.domain.util.NoParams
import kotlinx.coroutines.flow.Flow

class GetTrendingMoviesUseCase(
    private val repository: MovieRepository,
) : FlowUseCase<NoParams, List<Movie>> {
    override fun invoke(params: NoParams): Flow<List<Movie>> =
        repository.getTrendingMovies()
}
