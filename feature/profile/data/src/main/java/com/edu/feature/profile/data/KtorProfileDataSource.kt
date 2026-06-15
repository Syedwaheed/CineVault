package com.edu.feature.profile.data

import com.edu.core.domain.auth.Account
import com.edu.core.domain.util.DataError
import com.edu.core.domain.util.Result
import com.edu.core.domain.util.map
import com.edu.core.network.ktorclient.get
import com.edu.feature.profile.data.dto.ProfileAccountResponse
import com.edu.feature.profile.data.mapper.toAccount
import com.edu.feature.profile.domain.ProfileDataSource
import io.ktor.client.HttpClient

class KtorProfileDataSource(
    private val httpClient: HttpClient,
) : ProfileDataSource {

    override suspend fun getAccountDetails(sessionId: String): Result<Account, DataError.NetworkError> =
        httpClient.get<ProfileAccountResponse>(
            route = "account",
            queryParameters = mapOf("session_id" to sessionId),
        ).map { it.toAccount() }
}
