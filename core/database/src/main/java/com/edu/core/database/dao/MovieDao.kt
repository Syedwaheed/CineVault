package com.edu.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.edu.core.database.entity.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Query("SELECT * FROM movies ORDER BY popularity DESC")
    fun getMovies(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movies WHERE id = :id")
    fun getMovieById(id: Int): Flow<MovieEntity?>

    @Query("SELECT * FROM movies WHERE categories LIKE '%' || :category || '%' ORDER BY popularity DESC")
    fun getMoviesByCategory(category: String): Flow<List<MovieEntity>>

    @Upsert
    suspend fun upsertMovies(movies: List<MovieEntity>)

    @Query("DELETE FROM movies")
    suspend fun deleteMovies()
}
