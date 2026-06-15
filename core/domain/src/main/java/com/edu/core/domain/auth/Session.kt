package com.edu.core.domain.auth

data class Session(
    val sessionId: String,
    val accountId: Int,
    val username: String
)
