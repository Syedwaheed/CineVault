package com.edu.core.domain.network

interface TokenProvider {
    suspend fun getAccessToken(): String?
    suspend fun refreshAccessToken(): Boolean
}