package com.edu.auth.data.di

import com.edu.auth.data.KtorAuthDataSource
import com.edu.auth.data.SessionStorageImpl
import com.edu.auth.data.SessionTokenProvider
import com.edu.auth.domain.AuthDataSource
import com.edu.core.domain.auth.SessionStorage
import com.edu.core.domain.network.TokenProvider
import com.edu.core.network.NetworkConfig
import io.ktor.client.HttpClient
import org.koin.dsl.module

val authDataModule = module {
    single<SessionStorage> {
        SessionStorageImpl(secureStorage = get())
    }
    single<TokenProvider> {
        SessionTokenProvider(
            apiReadAccessToken = get<NetworkConfig>().apiReadAccessToken,
            sessionStorage = get(),
        )
    }
    single<AuthDataSource> {
        KtorAuthDataSource(httpClient = get<HttpClient>())
    }
}
