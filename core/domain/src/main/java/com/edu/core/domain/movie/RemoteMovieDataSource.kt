package com.edu.core.domain.movie

import com.edu.core.domain.util.DataError
import com.edu.core.domain.util.EmptyResult
import com.edu.core.domain.util.Result

interface RemoteMovieDataSource {
    // GET methods
    suspend fun getPopularMovies(page: Int): Result<List<Movie>, DataError.NetworkError>
    suspend fun getTopRatedMovies(page: Int): Result<List<Movie>, DataError.NetworkError>
    suspend fun getNowPlayingMovies(page: Int): Result<List<Movie>, DataError.NetworkError>
    suspend fun getUpcomingMovies(page: Int): Result<List<Movie>, DataError.NetworkError>
    suspend fun getMovieDetails(movieId: Int): Result<Movie, DataError.NetworkError>
    suspend fun searchMovies(query: String, page: Int): Result<List<Movie>, DataError.NetworkError>

    // POST methods (require session_id)
    suspend fun rateMovie(
        movieId: Int,
        rating: Double,
        sessionId: String
    ): EmptyResult<DataError.NetworkError>

    suspend fun deleteRating(
        movieId: Int,
        sessionId: String
    ): EmptyResult<DataError.NetworkError>

    suspend fun addToFavorites(
        accountId: Int,
        movieId: Int,
        favorite: Boolean,
        sessionId: String
    ): EmptyResult<DataError.NetworkError>

    suspend fun addToWatchlist(
        accountId: Int,
        movieId: Int,
        watchlist: Boolean,
        sessionId: String
    ): EmptyResult<DataError.NetworkError>
}
