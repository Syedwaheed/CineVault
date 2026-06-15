package com.edu.feature.detail.domain

import com.edu.core.domain.movie.Movie
import com.edu.core.domain.movie.MovieRepository
import com.edu.core.domain.util.FlowUseCase
import kotlinx.coroutines.flow.Flow

class GetSimilarMoviesUseCase(
    private val repository: MovieRepository,
) : FlowUseCase<Int, List<Movie>> {
    override fun invoke(params: Int): Flow<List<Movie>> =
        repository.getSimilarMovies(params)
}