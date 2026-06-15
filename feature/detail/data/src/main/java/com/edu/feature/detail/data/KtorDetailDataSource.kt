package com.edu.feature.detail.data

import com.edu.core.database.dao.ActorDao
import com.edu.core.database.dao.MovieDao
import com.edu.core.domain.util.DataError
import com.edu.core.domain.util.EmptyResult
import com.edu.core.domain.util.Result
import com.edu.core.domain.util.map
import com.edu.core.network.ktorclient.safeCall
import com.edu.data.movie.dto.CreditsResponseDto
import com.edu.data.movie.dto.MovieDto
import com.edu.data.movie.dto.VideoListResponseDto
import com.edu.data.movie.mapper.toActorEntity
import com.edu.data.movie.mapper.toEntity
import com.edu.feature.detail.domain.DetailRemoteDataSource
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

// Fetches detail-specific remote data (full movie detail + credits) and writes
// both to the shared Room database. The UI reads from Room via MovieRepository —
// it never calls this class directly.
class KtorDetailDataSource(
    private val client: HttpClient,
    private val movieDao: MovieDao,
    private val actorDao: ActorDao,
) : DetailRemoteDataSource {

    override suspend fun syncMovieDetail(movieId: Int): EmptyResult<DataError.NetworkError> =
        coroutineScope {
            val detailDeferred = async {
                safeCall<MovieDto> { client.get(url("movie/$movieId")) }
            }
            val creditsDeferred = async {
                safeCall<CreditsResponseDto> { client.get(url("movie/$movieId/credits")) }
            }

            val detailResult = detailDeferred.await()
            val creditsResult = creditsDeferred.await()

            if (detailResult is Result.Error) return@coroutineScope detailResult

            if (detailResult is Result.Success) {
                // Upsert the full movie entity — this brings runtime into Room
                movieDao.upsertMovies(listOf(detailResult.data.toEntity()))
            }

            if (creditsResult is Result.Success) {
                val actorEntities = creditsResult.data.cast
                    .take(20)
                    .map { it.toActorEntity(movieId) }
                actorDao.upsertCredits(actorEntities)
            }

            Result.Success(Unit)
        }

    override suspend fun fetchTrailerKey(movieId: Int): Result<String?, DataError.NetworkError> =
        safeCall<VideoListResponseDto> {
            client.get(url("movie/$movieId/videos"))
        }.map { response ->
            response.results
                .filter { it.site == "YouTube" && it.type == "Trailer" && it.official }
                .ifEmpty {
                    // fall back to any YouTube trailer if no official one exists
                    response.results.filter { it.site == "YouTube" && it.type == "Trailer" }
                }
                .firstOrNull()
                ?.key
        }

    private fun url(path: String) = "${BuildConfig.BASE_URL}$path"
}