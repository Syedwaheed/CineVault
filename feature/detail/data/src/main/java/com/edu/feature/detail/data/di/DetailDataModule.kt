package com.edu.feature.detail.data.di

import com.edu.core.database.dao.ActorDao
import com.edu.core.database.dao.MovieDao
import com.edu.feature.detail.data.KtorDetailDataSource
import com.edu.feature.detail.domain.DetailRemoteDataSource
import io.ktor.client.HttpClient
import org.koin.dsl.module

val detailDataModule = module {
    single<DetailRemoteDataSource> {
        KtorDetailDataSource(
            client = get<HttpClient>(),
            movieDao = get<MovieDao>(),
            actorDao = get<ActorDao>(),
        )
    }
}