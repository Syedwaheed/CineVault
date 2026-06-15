package com.edu.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.edu.core.database.entity.ActorEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActorDao {

    @Query("SELECT * FROM actor_credits WHERE movieId = :movieId ORDER BY `order` ASC")
    fun getCredits(movieId: Int): Flow<List<ActorEntity>>

    @Upsert
    suspend fun upsertCredits(actors: List<ActorEntity>)

    @Query("DELETE FROM actor_credits WHERE movieId = :movieId")
    suspend fun deleteCreditsForMovie(movieId: Int)
}