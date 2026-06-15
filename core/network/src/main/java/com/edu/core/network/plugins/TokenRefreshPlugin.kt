package com.edu.core.network.plugins

import io.ktor.client.plugins.api.Send
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.http.HttpHeaders
import io.ktor.util.AttributeKey

private val RefreshAttemptedKey = AttributeKey<Boolean>("TokenRefreshAttempted")

class TokenRefreshConfig {
    var tokenProvider: suspend () -> String? = { null }
    var onRefreshToken: suspend () -> Boolean = { false }
}

val TokenRefreshPlugin = createClientPlugin("TokenRefreshPlugin", ::TokenRefreshConfig) {
    on(Send) { request ->
        val call = proceed(request)
        val alreadyRetried = request.attributes.getOrNull(RefreshAttemptedKey) == true

        if (call.response.status.value == 401 && !alreadyRetried) {
            val refreshed = pluginConfig.onRefreshToken()
            if (refreshed) {
                val newToken = pluginConfig.tokenProvider()
                if (newToken != null) {
                    request.headers.remove(HttpHeaders.Authorization)
                    request.headers.append(HttpHeaders.Authorization, "Bearer $newToken")
                }
                request.attributes.put(RefreshAttemptedKey, true)
                proceed(request)
            } else {
                call
            }
        } else {
            call
        }
    }
}
