package com.edu.core.database

import androidx.room.Room
import com.edu.core.database.dao.ActorDao
import com.edu.core.database.dao.MovieDao
import com.edu.core.database.dao.UserDao
import com.edu.core.database.dao.WatchlistDao
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single<CineVaultDatabase> {
        Room.databaseBuilder(
            androidContext(),
            CineVaultDatabase::class.java,
            "cinevault.db",
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    single<MovieDao> { get<CineVaultDatabase>().movieDao() }
    single<WatchlistDao> { get<CineVaultDatabase>().watchlistDao() }
    single<ActorDao> { get<CineVaultDatabase>().actorDao() }
    single<UserDao> { get<CineVaultDatabase>().userDao() }
}