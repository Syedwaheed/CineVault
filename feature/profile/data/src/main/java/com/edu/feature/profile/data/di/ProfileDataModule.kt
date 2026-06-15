package com.edu.feature.profile.data.di

import com.edu.core.domain.auth.ProfileRepository
import com.edu.feature.profile.data.KtorProfileDataSource
import com.edu.feature.profile.data.ProfileRepositoryImpl
import com.edu.feature.profile.domain.ProfileDataSource
import io.ktor.client.HttpClient
import org.koin.dsl.module

val profileDataModule = module {
    single<ProfileDataSource> { KtorProfileDataSource(get<HttpClient>()) }

    single<ProfileRepository> {
        ProfileRepositoryImpl(
            profileDataSource = get(),
            userDao = get(),
            watchlistDao = get(),
            sessionStorage = get(),
        )
    }
}
