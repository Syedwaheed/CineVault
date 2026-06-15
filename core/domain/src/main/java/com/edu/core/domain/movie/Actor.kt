package com.edu.core.domain.movie

data class Actor(
    val id: Int,
    val name: String,
    val character: String,
    val profilePath: String?,
    val order: Int,
    val knownForDepartment: String,
)
