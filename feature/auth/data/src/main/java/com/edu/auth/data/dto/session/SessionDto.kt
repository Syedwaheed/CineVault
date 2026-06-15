package com.edu.auth.data.dto.session

import kotlinx.serialization.Serializable

@Serializable
data class SessionDto(
    val sessionId: String,
    val accountId: Int,
    val username: String
)
