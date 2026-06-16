package com.edu.core.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.edu.core.database.converter.IntListConverter
import com.edu.core.database.dao.ActorDao
import com.edu.core.database.dao.MovieDao
import com.edu.core.database.dao.UserDao
import com.edu.core.database.dao.WatchlistDao
import com.edu.core.database.entity.ActorEntity
import com.edu.core.database.entity.MovieEntity
import com.edu.core.database.entity.UserEntity
import com.edu.core.database.entity.WatchlistEntity

@Database(
    entities = [MovieEntity::class, WatchlistEntity::class, ActorEntity::class, UserEntity::class],
    version = 3,
    exportSchema = true,
    autoMigrations = [AutoMigration(from = 2, to = 3)],
)
@TypeConverters(IntListConverter::class)
abstract class CineVaultDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun watchlistDao(): WatchlistDao
    abstract fun actorDao(): ActorDao
    abstract fun userDao(): UserDao
}