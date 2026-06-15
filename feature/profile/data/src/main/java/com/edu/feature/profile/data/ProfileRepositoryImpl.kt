package com.edu.feature.profile.data

import com.edu.core.database.dao.UserDao
import com.edu.core.database.dao.WatchlistDao
import com.edu.core.domain.auth.Account
import com.edu.core.domain.auth.ProfileRepository
import com.edu.core.domain.auth.SessionStorage
import com.edu.core.domain.auth.UserStats
import com.edu.core.domain.util.Result
import com.edu.feature.profile.data.mapper.toAccount
import com.edu.feature.profile.data.mapper.toUserEntity
import com.edu.feature.profile.domain.ProfileDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ProfileRepositoryImpl(
    private val profileDataSource: ProfileDataSource,
    private val userDao: UserDao,
    private val watchlistDao: WatchlistDao,
    private val sessionStorage: SessionStorage,
) : ProfileRepository {

    override fun getProfile(): Flow<Account> = channelFlow {
        // Trigger a background network refresh on every call; Room emits the result automatically.
        launch {
            val session = sessionStorage.getSession() ?: return@launch
            val result = profileDataSource.getAccountDetails(session.sessionId)
            if (result is Result.Success) {
                userDao.upsertUser(result.data.toUserEntity())
            }
        }
        // Room is the source of truth — always emits cached value immediately, then refreshed value.
        userDao.getUser()
            .filterNotNull()
            .map { it.toAccount() }
            .collect { send(it) }
    }

    override fun getUserStats(): Flow<UserStats> =
        watchlistDao.getWatchlist().map { items ->
            UserStats(
                watchlistCount = items.size,
                moviesWatched = 0,
            )
        }

    override suspend fun logout() {
        sessionStorage.deleteSession()
        userDao.deleteUser()
        watchlistDao.clearWatchlist()
    }
}
