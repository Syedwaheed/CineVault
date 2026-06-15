package com.edu.feature.detail.presentation.di

import com.edu.feature.detail.domain.GetCreditsUseCase
import com.edu.feature.detail.domain.GetMovieDetailUseCase
import com.edu.feature.detail.domain.GetSimilarMoviesUseCase
import com.edu.feature.detail.domain.SyncDetailUseCase
import com.edu.feature.detail.presentation.DetailViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val detailModule = module {
    factory { GetMovieDetailUseCase(get()) }
    factory { GetCreditsUseCase(get()) }
    factory { GetSimilarMoviesUseCase(get()) }
    factory { SyncDetailUseCase(get()) }

    viewModel { params ->
        DetailViewModel(
            movieId = params.get(),
            getMovieDetail = get(),
            getCredits = get(),
            getSimilarMovies = get(),
            syncDetail = get(),
            remoteDataSource = get(),
            watchlistRepository = get(),
        )
    }
}
