package com.edu.core.domain.movie

import kotlinx.coroutines.flow.Flow

interface WatchlistRepository {
    fun getWatchlist(): Flow<List<Movie>>
    fun isInWatchlist(movieId: Int): Flow<Boolean>
    suspend fun addToWatchlist(movie: Movie)
    suspend fun removeFromWatchlist(movieId: Int)
}
