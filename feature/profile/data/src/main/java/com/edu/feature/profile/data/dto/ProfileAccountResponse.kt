package com.edu.feature.profile.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileAccountResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("username")
    val username: String,
    @SerialName("include_adult")
    val includeAdult: Boolean,
    @SerialName("iso_639_1")
    val language: String,
    @SerialName("iso_3166_1")
    val country: String,
    @SerialName("avatar")
    val avatar: Avatar,
) {
    @Serializable
    data class Avatar(
        @SerialName("tmdb")
        val tmdb: Tmdb,
    ) {
        @Serializable
        data class Tmdb(
            @SerialName("avatar_path")
            val avatarPath: String?,
        )
    }
}
