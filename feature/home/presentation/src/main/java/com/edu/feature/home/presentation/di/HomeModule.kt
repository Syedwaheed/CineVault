package com.edu.feature.home.presentation.di

import com.edu.feature.home.domain.GetTopRatedMoviesUseCase
import com.edu.feature.home.domain.GetTrendingMoviesUseCase
import com.edu.feature.home.domain.SyncMoviesUseCase
import com.edu.feature.home.presentation.HomeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val homeModule = module {
    factory { GetTrendingMoviesUseCase(get()) }
    factory { GetTopRatedMoviesUseCase(get()) }
    factory { SyncMoviesUseCase(get()) }
    viewModel { HomeViewModel(get(), get(), get()) }
}
