package com.edu.data.movie.repository

import com.edu.core.database.dao.WatchlistDao
import com.edu.core.domain.movie.Movie
import com.edu.core.domain.movie.WatchlistRepository
import com.edu.data.movie.mapper.toDomain
import com.edu.data.movie.mapper.toWatchlistEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WatchlistRepositoryImpl(
    private val watchlistDao: WatchlistDao,
) : WatchlistRepository {

    override fun getWatchlist(): Flow<List<Movie>> =
        watchlistDao.getWatchlist().map { entities -> entities.map { it.toDomain() } }

    override fun isInWatchlist(movieId: Int): Flow<Boolean> =
        watchlistDao.getWatchlistItem(movieId).map { it != null }

    override suspend fun addToWatchlist(movie: Movie) {
        watchlistDao.upsertWatchlistItem(movie.toWatchlistEntity())
    }

    override suspend fun removeFromWatchlist(movieId: Int) {
        watchlistDao.deleteWatchlistItem(movieId)
    }
}
