package com.edu.movieapplication

import android.app.Application
import com.edu.auth.data.di.authDataModule
import com.edu.feature.detail.data.di.detailDataModule
import com.edu.feature.profile.data.di.profileDataModule
import com.edu.auth.presentation.di.authModule
import com.edu.core.database.databaseModule
import com.edu.core.network.NetworkConfig
import com.edu.core.network.di.networkModule
import com.edu.data.di.coreDataModule
import com.edu.data.di.dataModule
import com.edu.feature.detail.presentation.di.detailModule
import com.edu.feature.home.presentation.di.homeModule
import com.edu.feature.profile.presentation.di.profileModule
import com.edu.feature.search.presentation.di.searchModule
import com.edu.feature.watchlist.presentation.di.watchlistModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

val appModule = module {
    single {
        NetworkConfig(
            baseUrl = BuildConfig.BASE_URL,
            imageBaseUrl = BuildConfig.IMAGE_BASE_URL,
            apiReadAccessToken = BuildConfig.API_READ_ACCESS_TOKEN,
        )
    }
}

class CineVaultApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CineVaultApp)
            modules(
                appModule,
                networkModule,
                databaseModule,
                coreDataModule,
                dataModule,
                authDataModule,
                detailDataModule,
                profileDataModule,
                authModule,
                homeModule,
                searchModule,
                watchlistModule,
                profileModule,
                detailModule,
            )
        }
    }
}