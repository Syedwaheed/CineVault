package com.edu.core.network.plugins

import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.http.HttpHeaders

class AuthPluginConfig {
    var tokenProvider: suspend () -> String? = { null }
}

val AuthPlugin = createClientPlugin("AuthPlugin", ::AuthPluginConfig) {
    val config = pluginConfig
    onRequest { request, _ ->
        val token = config.tokenProvider()
        if (token != null) {
            request.headers.append(HttpHeaders.Authorization, "Bearer $token")
        }
    }
}
