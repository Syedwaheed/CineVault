package com.edu.data.movie.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDto(
    val id: Int,
    val title: String,
    val overview: String,
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("backdrop_path") val backdropPath: String? = null,
    @SerialName("release_date") val releaseDate: String = "",
    @SerialName("vote_average") val voteAverage: Double = 0.0,
    @SerialName("vote_count") val voteCount: Int = 0,
    val popularity: Double = 0.0,
    val adult: Boolean = false,
    @SerialName("original_language") val originalLanguage: String = "",
    @SerialName("original_title") val originalTitle: String = "",
    @SerialName("genre_ids") val genreIds: List<Int> = emptyList(),
    val runtime: Int? = null,
)

@Serializable
data class CastDto(
    val id: Int,
    val name: String,
    val character: String,
    @SerialName("profile_path") val profilePath: String? = null,
    val order: Int = 0,
    @SerialName("known_for_department") val knownForDepartment: String = "",
)

@Serializable
data class CreditsResponseDto(
    val id: Int = 0,
    val cast: List<CastDto> = emptyList(),
)

@Serializable
data class VideoDto(
    val id: String = "",
    val key: String = "",
    val site: String = "",
    val type: String = "",
    val official: Boolean = false,
)

@Serializable
data class VideoListResponseDto(
    val id: Int = 0,
    val results: List<VideoDto> = emptyList(),
)

@Serializable
data class MovieListResponseDto(
    val page: Int = 1,
    val results: List<MovieDto> = emptyList(),
    @SerialName("total_pages") val totalPages: Int = 0,
    @SerialName("total_results") val totalResults: Int = 0,
)

@Serializable
internal data class RatingRequestDto(
    val value: Double,
)

@Serializable
internal data class FavoriteRequestDto(
    @SerialName("media_type") val mediaType: String,
    @SerialName("media_id") val mediaId: Int,
    val favorite: Boolean,
)

@Serializable
internal data class WatchlistRequestDto(
    @SerialName("media_type") val mediaType: String,
    @SerialName("media_id") val mediaId: Int,
    val watchlist: Boolean,
)

@Serializable
internal data class StatusResponseDto(
    @SerialName("status_code") val statusCode: Int = 0,
    @SerialName("status_message") val statusMessage: String = "",
    val success: Boolean = false,
)
