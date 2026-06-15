package com.edu.auth.domain.usecase

import com.edu.auth.domain.AuthDataSource
import com.edu.core.domain.auth.SessionStorage
import com.edu.core.domain.util.DataError
import com.edu.core.domain.util.EmptyResult
import com.edu.core.domain.util.NoParams
import com.edu.core.domain.util.Result
import com.edu.core.domain.util.SuspendUseCase

class LogoutUseCase(
    private val authDataSource: AuthDataSource,
    private val sessionStorage: SessionStorage,
) : SuspendUseCase<NoParams, EmptyResult<DataError.NetworkError>> {

    override suspend fun invoke(params: NoParams): EmptyResult<DataError.NetworkError> {
        val session = sessionStorage.getSession()
        if (session != null) {
            // Best-effort remote delete; local session is always cleared below.
            authDataSource.deleteSession(session.sessionId)
        }
        sessionStorage.deleteSession()
        return Result.Success(Unit)
    }
}
