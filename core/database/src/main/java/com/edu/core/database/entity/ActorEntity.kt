package com.edu.core.database.entity

import androidx.room.Entity

@Entity(
    tableName = "actor_credits",
    primaryKeys = ["movieId", "actorId"],
)
data class ActorEntity(
    val movieId: Int,
    val actorId: Int,
    val name: String,
    val character: String,
    val profilePath: String?,
    val order: Int,
    val knownForDepartment: String,
)