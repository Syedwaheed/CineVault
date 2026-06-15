package com.edu.core.network

import com.edu.core.domain.network.TokenProvider
import com.edu.core.network.plugins.AuthPlugin
import com.edu.core.network.plugins.TokenRefreshPlugin
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private const val TIMEOUT_MS = 30_000L

fun buildHttpClient(
    baseUrl: String,
    tokenProvider: TokenProvider,
    logger: (String) -> Unit = ::println,
): HttpClient = HttpClient(CIO) {

    install(HttpTimeout) {
        requestTimeoutMillis = TIMEOUT_MS
        connectTimeoutMillis = TIMEOUT_MS
        socketTimeoutMillis = TIMEOUT_MS
    }

    install(ContentNegotiation) {
        json(Json {
            isLenient = true
            ignoreUnknownKeys = true
        })
    }

    install(Logging) {
        this.logger = object : Logger {
            override fun log(message: String) = logger(message)
        }
        level = LogLevel.BODY
    }

    install(AuthPlugin) {
        this.tokenProvider = tokenProvider::getAccessToken
    }

    install(TokenRefreshPlugin) {
        this.tokenProvider = tokenProvider::getAccessToken
        this.onRefreshToken = tokenProvider::refreshAccessToken
    }

    defaultRequest {
        url(baseUrl)
        contentType(ContentType.Application.Json)
    }

}.also { client ->
    client.attributes.put(BaseUrlKey, baseUrl)
}