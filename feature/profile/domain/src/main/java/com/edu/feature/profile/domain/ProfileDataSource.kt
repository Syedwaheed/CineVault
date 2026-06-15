package com.edu.feature.profile.domain

import com.edu.core.domain.auth.Account
import com.edu.core.domain.util.DataError
import com.edu.core.domain.util.Result

interface ProfileDataSource {
    suspend fun getAccountDetails(sessionId: String): Result<Account, DataError.NetworkError>
}
