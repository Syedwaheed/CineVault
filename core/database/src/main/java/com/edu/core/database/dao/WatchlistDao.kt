package com.edu.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.edu.core.database.entity.WatchlistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchlistDao {

    @Query("SELECT * FROM watchlist ORDER BY addedAt DESC")
    fun getWatchlist(): Flow<List<WatchlistEntity>>

    @Query("SELECT * FROM watchlist WHERE movieId = :movieId")
    fun getWatchlistItem(movieId: Int): Flow<WatchlistEntity?>

    @Upsert
    suspend fun upsertWatchlistItem(item: WatchlistEntity)

    @Query("DELETE FROM watchlist WHERE movieId = :movieId")
    suspend fun deleteWatchlistItem(movieId: Int)

    @Query("DELETE FROM watchlist")
    suspend fun clearWatchlist()
}
