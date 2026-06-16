package com.edu.data.movie.repository

import com.edu.core.database.dao.ActorDao
import com.edu.core.database.dao.MovieDao
import com.edu.core.domain.movie.Actor
import com.edu.core.domain.movie.Movie
import com.edu.core.domain.movie.MovieRepository
import com.edu.core.domain.movie.RemoteMovieDataSource
import com.edu.core.domain.movie.SyncStatus
import com.edu.core.domain.util.Result
import com.edu.data.movie.mapper.toDomain
import com.edu.data.movie.mapper.toEntity
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

// BENCHMARK: candidate for Macrobenchmark baseline profile
class MovieRepositoryImpl(
    private val remoteDataSource: RemoteMovieDataSource,
    private val movieDao: MovieDao,
    private val actorDao: ActorDao,
) : MovieRepository {

    override fun getTrendingMovies(): Flow<List<Movie>> =
        movieDao.getMoviesByCategory("popular").map { entities -> entities.map { it.toDomain() } }

    override fun getTopRatedMovies(): Flow<List<Movie>> =
        movieDao.getMoviesByCategory("top_rated").map { entities ->
            entities.sortedByDescending { it.voteAverage }.map { it.toDomain() }
        }

    override fun getNowPlayingMovies(): Flow<List<Movie>> =
        movieDao.getMoviesByCategory("now_playing").map { entities -> entities.map { it.toDomain() } }

    override fun getUpcomingMovies(): Flow<List<Movie>> =
        movieDao.getMoviesByCategory("upcoming").map { entities ->
            entities.sortedBy { it.releaseDate }.map { it.toDomain() }
        }

    override fun getMovieById(id: Int): Flow<Movie?> =
        movieDao.getMovieById(id).map { it?.toDomain() }

    override fun searchMovies(query: String): Flow<List<Movie>> = flow {
        val cached = movieDao.getMovies().first()
            .filter {
                it.title.contains(query, ignoreCase = true) ||
                it.originalTitle.contains(query, ignoreCase = true)
            }
            .map { it.toDomain() }
        if (cached.isNotEmpty()) emit(cached)

        when (val result = remoteDataSource.searchMovies(query, page = 1)) {
            is Result.Success -> {
                movieDao.upsertMovies(result.data.distinctBy { it.id }.map { it.toEntity() })
                emit(result.data)
            }
            is Result.Error -> {
                if (cached.isEmpty()) throw Exception(result.error.toString())
            }
        }
    }

    // Reads credits from Room — written by feature:detail:data's KtorDetailDataSource
    override fun getCredits(movieId: Int): Flow<List<Actor>> =
        actorDao.getCredits(movieId).map { entities -> entities.map { it.toDomain() } }

    // Derives similar movies from Room by genre overlap — fully offline
    override fun getSimilarMovies(movieId: Int): Flow<List<Movie>> =
        movieDao.getMovieById(movieId)
            .combine(movieDao.getMovies()) { target, all ->
                if (target == null) return@combine emptyList()
                val targetGenres = target.genreIds.toSet()
                all
                    .filter { it.id != movieId && it.genreIds.any { id -> id in targetGenres } }
                    .sortedByDescending { it.voteAverage }
                    .take(10)
                    .map { it.toDomain() }
            }

    override fun syncMovies(): Flow<SyncStatus> = flow {
        emit(SyncStatus.Syncing)
        try {
            val popularResult     = remoteDataSource.getPopularMovies(page = 1)
            val topRatedResult    = remoteDataSource.getTopRatedMovies(page = 1)
            val nowPlayingResult  = remoteDataSource.getNowPlayingMovies(page = 1)
            val upcomingResult    = remoteDataSource.getUpcomingMovies(page = 1)

            val firstError = listOf(popularResult, topRatedResult, nowPlayingResult, upcomingResult)
                .firstOrNull { it is Result.Error<*> } as? Result.Error<*>

            if (firstError != null) {
                emit(SyncStatus.Error(firstError.error.toString()))
                return@flow
            }

            // Merge all categories for the same movie (a movie can be in multiple lists)
            val categoryMap = mutableMapOf<Int, Pair<Movie, MutableSet<String>>>()
            mapOf(
                "popular"     to (popularResult    as? Result.Success)?.data.orEmpty(),
                "top_rated"   to (topRatedResult   as? Result.Success)?.data.orEmpty(),
                "now_playing" to (nowPlayingResult as? Result.Success)?.data.orEmpty(),
                "upcoming"    to (upcomingResult   as? Result.Success)?.data.orEmpty(),
            ).forEach { (category, movies) ->
                movies.forEach { movie ->
                    categoryMap.getOrPut(movie.id) { movie to mutableSetOf() }.second.add(category)
                }
            }

            val entities = categoryMap.values.map { (movie, cats) ->
                movie.toEntity().copy(categories = cats.joinToString(","))
            }

            movieDao.upsertMovies(entities)
            emit(SyncStatus.Success)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            emit(SyncStatus.Error(e.message ?: "Sync failed"))
        }
    }
}
