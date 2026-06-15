package com.edu.core.domain.movie

import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getTrendingMovies(): Flow<List<Movie>>
    fun getTopRatedMovies(): Flow<List<Movie>>
    fun getMovieById(id: Int): Flow<Movie?>
    fun searchMovies(query: String): Flow<List<Movie>>
    fun syncMovies(): Flow<SyncStatus>
    fun getCredits(movieId: Int): Flow<List<Actor>>
    fun getSimilarMovies(movieId: Int): Flow<List<Movie>>
}
