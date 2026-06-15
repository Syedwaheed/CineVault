package com.edu.auth.domain

import com.edu.core.domain.auth.Account
import com.edu.core.domain.util.DataError
import com.edu.core.domain.util.EmptyResult
import com.edu.core.domain.util.Result

interface AuthDataSource {
    suspend fun createRequestToken(): Result<String, DataError.NetworkError>
    fun getAuthorizationUrl(requestToken: String): String
    suspend fun accountDetails(sessionId: String): Result<Account, DataError.NetworkError>
    suspend fun createSession(token: String): Result<String, DataError.NetworkError>
    suspend fun deleteSession(sessionId: String): EmptyResult<DataError.NetworkError>

}