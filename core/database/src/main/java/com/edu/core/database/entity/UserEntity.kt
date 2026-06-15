package com.edu.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey val tmdbAccountId: Int,
    val username: String,
    val name: String,
    val avatarPath: String?,
    val includeAdult: Boolean,
    val region: String,
)
