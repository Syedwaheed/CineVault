package com.edu.data.movie.source

import com.edu.core.domain.movie.Movie
import com.edu.core.domain.movie.RemoteMovieDataSource
import com.edu.core.domain.util.DataError
import com.edu.core.domain.util.EmptyResult
import com.edu.core.domain.util.Result
import com.edu.core.domain.util.asEmptyDataResult
import com.edu.core.domain.util.map
import com.edu.core.network.ktorclient.safeCall
import com.edu.data.BuildConfig
import com.edu.data.movie.dto.FavoriteRequestDto
import com.edu.data.movie.dto.MovieDto
import com.edu.data.movie.dto.MovieListResponseDto
import com.edu.data.movie.dto.RatingRequestDto
import com.edu.data.movie.dto.StatusResponseDto
import com.edu.data.movie.dto.WatchlistRequestDto
import com.edu.data.movie.mapper.toDomain
import com.edu.data.movie.mapper.toEntity

import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class KtorRemoteMovieDataSource(
    private val client: HttpClient,
) : RemoteMovieDataSource {

    override suspend fun getPopularMovies(page: Int): Result<List<Movie>, DataError.NetworkError> =
        safeCall<MovieListResponseDto> {
            client.get(url("movie/popular")) {
                parameter("page", page)
            }
        }.map { it.results.map { dto -> dto.toEntity().toDomain() } }

    override suspend fun getTopRatedMovies(page: Int): Result<List<Movie>, DataError.NetworkError> =
        safeCall<MovieListResponseDto> {
            client.get(url("movie/top_rated")) {
                parameter("page", page)
            }
        }.map { it.results.map { dto -> dto.toEntity().toDomain() } }

    override suspend fun getNowPlayingMovies(page: Int): Result<List<Movie>, DataError.NetworkError> =
        safeCall<MovieListResponseDto> {
            client.get(url("movie/now_playing")) {
                parameter("page", page)
            }
        }.map { it.results.map { dto -> dto.toEntity().toDomain() } }

    override suspend fun getUpcomingMovies(page: Int): Result<List<Movie>, DataError.NetworkError> =
        safeCall<MovieListResponseDto> {
            client.get(url("movie/upcoming")) {
                parameter("page", page)
            }
        }.map { it.results.map { dto -> dto.toEntity().toDomain() } }

    override suspend fun getMovieDetails(movieId: Int): Result<Movie, DataError.NetworkError> =
        safeCall<MovieDto> {
            client.get(url("movie/$movieId"))
        }.map { it.toEntity().toDomain() }

    override suspend fun searchMovies(
        query: String,
        page: Int,
    ): Result<List<Movie>, DataError.NetworkError> =
        safeCall<MovieListResponseDto> {
            client.get(url("search/movie")) {
                parameter("query", query)
                parameter("page", page)
            }
        }.map { it.results.map { dto -> dto.toEntity().toDomain() } }

    override suspend fun rateMovie(
        movieId: Int,
        rating: Double,
        sessionId: String,
    ): EmptyResult<DataError.NetworkError> =
        safeCall<StatusResponseDto> {
            client.post(url("movie/$movieId/rating")) {
                parameter("session_id", sessionId)
                contentType(ContentType.Application.Json)
                setBody(RatingRequestDto(value = rating))
            }
        }.asEmptyDataResult()

    override suspend fun deleteRating(
        movieId: Int,
        sessionId: String,
    ): EmptyResult<DataError.NetworkError> =
        safeCall<StatusResponseDto> {
            client.delete(url("movie/$movieId/rating")) {
                parameter("session_id", sessionId)
            }
        }.asEmptyDataResult()

    override suspend fun addToFavorites(
        accountId: Int,
        movieId: Int,
        favorite: Boolean,
        sessionId: String,
    ): EmptyResult<DataError.NetworkError> =
        safeCall<StatusResponseDto> {
            client.post(url("account/$accountId/favorite")) {
                parameter("session_id", sessionId)
                contentType(ContentType.Application.Json)
                setBody(FavoriteRequestDto(mediaType = "movie", mediaId = movieId, favorite = favorite))
            }
        }.asEmptyDataResult()

    override suspend fun addToWatchlist(
        accountId: Int,
        movieId: Int,
        watchlist: Boolean,
        sessionId: String,
    ): EmptyResult<DataError.NetworkError> =
        safeCall<StatusResponseDto> {
            client.post(url("account/$accountId/watchlist")) {
                parameter("session_id", sessionId)
                contentType(ContentType.Application.Json)
                setBody(WatchlistRequestDto(mediaType = "movie", mediaId = movieId, watchlist = watchlist))
            }
        }.asEmptyDataResult()

    private fun url(path: String): String = "${BuildConfig.BASE_URL}$path"
}
