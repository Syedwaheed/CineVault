package com.edu.feature.watchlist.presentation.di

import com.edu.feature.watchlist.domain.AddToWatchlistUseCase
import com.edu.feature.watchlist.domain.GetWatchlistUseCase
import com.edu.feature.watchlist.domain.RemoveFromWatchlistUseCase
import com.edu.feature.watchlist.presentation.WatchlistViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val watchlistModule = module {
    factory { GetWatchlistUseCase(get()) }
    factory { RemoveFromWatchlistUseCase(get()) }
    factory { AddToWatchlistUseCase(get()) }

    viewModel {
        WatchlistViewModel(
            getWatchlist = get(),
            removeFromWatchlist = get(),
            addToWatchlist = get(),
        )
    }
}
