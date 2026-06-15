package com.edu.feature.search.presentation.di

import com.edu.feature.search.domain.SearchMoviesUseCase
import com.edu.feature.search.presentation.SearchViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val searchModule = module {
    factory { SearchMoviesUseCase(get()) }
    viewModel { SearchViewModel(get()) }
}
