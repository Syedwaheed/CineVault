package com.edu.auth.data

import com.edu.auth.data.dto.mapper.toAccount
import com.edu.auth.data.dto.request.CreateSessionRequest
import com.edu.auth.data.dto.request.DeleteSessionRequest
import com.edu.auth.data.dto.response.AccountResponse
import com.edu.auth.data.dto.response.DeleteSessionResponse
import com.edu.auth.data.dto.response.RequestTokenResponse
import com.edu.auth.data.dto.response.SessionResponse
import com.edu.auth.domain.AuthDataSource
import com.edu.core.domain.auth.Account
import com.edu.core.domain.util.DataError
import com.edu.core.domain.util.EmptyResult
import com.edu.core.domain.util.Result
import com.edu.core.domain.util.asEmptyDataResult
import com.edu.core.domain.util.map
import com.edu.core.network.ktorclient.delete
import com.edu.core.network.ktorclient.get
import com.edu.core.network.ktorclient.post
import io.ktor.client.HttpClient

class KtorAuthDataSource(
    private val httpClient: HttpClient
): AuthDataSource {

    override suspend fun createRequestToken(): Result<String, DataError.NetworkError> {
        return httpClient.get<RequestTokenResponse>(
            route = "authentication/token/new"
        ).map { response ->
            response.requestToken
        }
    }

    override fun getAuthorizationUrl(requestToken: String): String {
        return "https://www.themoviedb.org/authenticate/$requestToken"
    }

    override suspend fun createSession(token: String): Result<String, DataError.NetworkError> {
        return httpClient.post<CreateSessionRequest, SessionResponse>(
            route = "authentication/session/new",
            body = CreateSessionRequest(requestToken = token)
        ).map { response ->
            response.sessionId
        }
    }

    override suspend fun accountDetails(sessionId: String): Result<Account, DataError.NetworkError> {
        return httpClient.get<AccountResponse>(
            route = "account",
            queryParameters = mapOf("session_id" to sessionId)
        ).map { response ->
            response.toAccount()
        }
    }

    override suspend fun deleteSession(sessionId: String): EmptyResult<DataError.NetworkError> {
        return httpClient.delete<DeleteSessionRequest, DeleteSessionResponse>(
            route = "authentication/session",
            body = DeleteSessionRequest(sessionId = sessionId)
        ).asEmptyDataResult()
    }
}
