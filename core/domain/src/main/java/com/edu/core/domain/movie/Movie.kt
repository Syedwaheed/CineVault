package com.edu.core.domain.movie

data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val backdropPath: String?,
    val releaseDate: String,
    val voteAverage: Double,
    val voteCount: Int,
    val popularity: Double,
    val adult: Boolean,
    val originalLanguage: String,
    val originalTitle: String,
    val genreIds: List<Int>,
    val runtime: Int = 0,
    val isAvailableOffline: Boolean = false,
)