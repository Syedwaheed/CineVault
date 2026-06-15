package com.edu.core.network.di

import com.edu.core.domain.network.TokenProvider
import com.edu.core.network.NetworkConfig
import com.edu.core.network.buildHttpClient
import io.ktor.client.HttpClient
import org.koin.dsl.module

val networkModule = module {
    single<HttpClient> {
        val config = get<NetworkConfig>()
        buildHttpClient(
            baseUrl = config.baseUrl,
            tokenProvider = get<TokenProvider>(),
        )
    }
}
