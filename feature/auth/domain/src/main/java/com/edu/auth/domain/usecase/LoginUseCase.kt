package com.edu.auth.domain.usecase

import com.edu.auth.domain.AuthDataSource
import com.edu.core.domain.auth.Session
import com.edu.core.domain.auth.SessionStorage
import com.edu.core.domain.util.DataError
import com.edu.core.domain.util.EmptyResult
import com.edu.core.domain.util.Result
import com.edu.core.domain.util.SuspendUseCase
import com.edu.core.domain.util.asEmptyDataResult

class LoginUseCase(
    private val authDataSource: AuthDataSource,
    private val sessionStorage: SessionStorage,
) : SuspendUseCase<String, EmptyResult<DataError.NetworkError>> {

    override suspend fun invoke(params: String): EmptyResult<DataError.NetworkError> {
        val sessionResult = authDataSource.createSession(params)
        if (sessionResult is Result.Error) return sessionResult.asEmptyDataResult()
        val sessionId = (sessionResult as Result.Success).data

        val accountResult = authDataSource.accountDetails(sessionId)
        if (accountResult is Result.Error) return accountResult.asEmptyDataResult()
        val account = (accountResult as Result.Success).data

        sessionStorage.storeSession(
            Session(
                sessionId = sessionId,
                accountId = account.id,
                username = account.username,
            )
        )
        return Result.Success(Unit)
    }
}
