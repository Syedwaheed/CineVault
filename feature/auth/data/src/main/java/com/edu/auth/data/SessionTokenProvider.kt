package com.edu.auth.data

import com.edu.core.domain.auth.SessionStorage
import com.edu.core.domain.network.TokenProvider

class SessionTokenProvider(
    private val apiReadAccessToken: String,
    private val sessionStorage: SessionStorage,
) : TokenProvider {

    override suspend fun getAccessToken(): String = apiReadAccessToken

    override suspend fun refreshAccessToken(): Boolean = false

    suspend fun getSessionId(): String? = sessionStorage.getSession()?.sessionId
}