package com.edu.data.di

import com.edu.core.domain.movie.MovieRepository
import com.edu.core.domain.movie.RemoteMovieDataSource
import com.edu.core.domain.movie.WatchlistRepository
import com.edu.data.movie.repository.MovieRepositoryImpl
import com.edu.data.movie.repository.WatchlistRepositoryImpl
import com.edu.data.movie.source.KtorRemoteMovieDataSource
import io.ktor.client.HttpClient
import org.koin.dsl.module

val dataModule = module {

    single<RemoteMovieDataSource> {
        KtorRemoteMovieDataSource(client = get<HttpClient>())
    }

    single<MovieRepository> {
        MovieRepositoryImpl(
            remoteDataSource = get(),
            movieDao = get(),
            actorDao = get(),
        )
    }

    single<WatchlistRepository> {
        WatchlistRepositoryImpl(watchlistDao = get())
    }
}
