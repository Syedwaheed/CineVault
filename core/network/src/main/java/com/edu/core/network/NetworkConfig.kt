package com.edu.core.network

import io.ktor.util.AttributeKey

data class NetworkConfig(
    val baseUrl: String,
    val imageBaseUrl: String,
    val apiReadAccessToken: String,
)

internal val BaseUrlKey = AttributeKey<String>("BaseUrl")