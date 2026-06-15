package com.edu.auth.domain.usecase

import com.edu.auth.domain.AuthDataSource
import com.edu.core.domain.util.DataError
import com.edu.core.domain.util.NoParams
import com.edu.core.domain.util.Result
import com.edu.core.domain.util.SuspendUseCase

class GetRequestTokenUseCase(
    private val authDataSource: AuthDataSource,
) : SuspendUseCase<NoParams, Result<String, DataError.NetworkError>> {
    override suspend fun invoke(params: NoParams): Result<String, DataError.NetworkError> =
        authDataSource.createRequestToken()
}
