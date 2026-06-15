package com.edu.core.domain.auth

import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getProfile(): Flow<Account>
    fun getUserStats(): Flow<UserStats>
    suspend fun logout()
}
