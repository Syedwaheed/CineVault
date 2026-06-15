package com.edu.data.movie.mapper

import com.edu.core.database.entity.ActorEntity
import com.edu.core.database.entity.MovieEntity
import com.edu.core.database.entity.WatchlistEntity
import com.edu.core.domain.movie.Actor
import com.edu.core.domain.movie.Movie
import com.edu.data.movie.dto.CastDto
import com.edu.data.movie.dto.MovieDto

fun MovieDto.toEntity(): MovieEntity = MovieEntity(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    releaseDate = releaseDate,
    voteAverage = voteAverage,
    voteCount = voteCount,
    popularity = popularity,
    adult = adult,
    originalLanguage = originalLanguage,
    originalTitle = originalTitle,
    genreIds = genreIds,
    runtime = runtime ?: 0,
)

fun MovieEntity.toDomain(): Movie = Movie(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    releaseDate = releaseDate,
    voteAverage = voteAverage,
    voteCount = voteCount,
    popularity = popularity,
    adult = adult,
    originalLanguage = originalLanguage,
    originalTitle = originalTitle,
    genreIds = genreIds,
    runtime = runtime,
    isAvailableOffline = isAvailableOffline,
)

fun Movie.toEntity(): MovieEntity = MovieEntity(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    releaseDate = releaseDate,
    voteAverage = voteAverage,
    voteCount = voteCount,
    popularity = popularity,
    adult = adult,
    originalLanguage = originalLanguage,
    originalTitle = originalTitle,
    genreIds = genreIds,
    runtime = runtime,
    isAvailableOffline = isAvailableOffline,
)

fun MovieEntity.toWatchlistEntity(): WatchlistEntity = WatchlistEntity(
    movieId = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    releaseDate = releaseDate,
    voteAverage = voteAverage,
    originalLanguage = originalLanguage,
    addedAt = System.currentTimeMillis(),
)

fun WatchlistEntity.toDomain(): Movie = Movie(
    id = movieId,
    title = title,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    releaseDate = releaseDate,
    voteAverage = voteAverage,
    voteCount = 0,
    popularity = 0.0,
    adult = false,
    originalLanguage = originalLanguage,
    originalTitle = title,
    genreIds = emptyList(),
    runtime = 0,
    isAvailableOffline = true,
)

fun Movie.toWatchlistEntity(): WatchlistEntity = WatchlistEntity(
    movieId = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    releaseDate = releaseDate,
    voteAverage = voteAverage,
    originalLanguage = originalLanguage,
    addedAt = System.currentTimeMillis(),
)

fun CastDto.toActorEntity(movieId: Int): ActorEntity = ActorEntity(
    movieId = movieId,
    actorId = id,
    name = name,
    character = character,
    profilePath = profilePath,
    order = order,
    knownForDepartment = knownForDepartment,
)

fun ActorEntity.toDomain(): Actor = Actor(
    id = actorId,
    name = name,
    character = character,
    profilePath = profilePath,
    order = order,
    knownForDepartment = knownForDepartment,
)

fun Actor.toEntity(movieId: Int): ActorEntity = ActorEntity(
    movieId = movieId,
    actorId = id,
    name = name,
    character = character,
    profilePath = profilePath,
    order = order,
    knownForDepartment = knownForDepartment,
)
