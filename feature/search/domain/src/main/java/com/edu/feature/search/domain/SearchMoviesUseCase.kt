package com.edu.feature.search.domain

import com.edu.core.domain.movie.Movie
import com.edu.core.domain.movie.MovieRepository
import com.edu.core.domain.util.FlowUseCase
import kotlinx.coroutines.flow.Flow

class SearchMoviesUseCase(
    private val repository: MovieRepository,
) : FlowUseCase<String, List<Movie>> {
    override fun invoke(params: String): Flow<List<Movie>> =
        repository.searchMovies(params)
}
