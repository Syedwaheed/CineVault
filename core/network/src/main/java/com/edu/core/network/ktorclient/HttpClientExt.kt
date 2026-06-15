package com.edu.core.network.ktorclient

import com.edu.core.domain.util.DataError
import com.edu.core.domain.util.Result
import com.edu.core.network.BaseUrlKey
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerializationException

suspend inline fun <reified Response : Any> HttpClient.get(
    route: String,
    queryParameters: Map<String, Any?> = emptyMap()
): Result<Response, DataError.NetworkError> = safeCall {
    get {
        url(constructRoute(route))
        queryParameters.forEach { (key, value) -> parameter(key, value) }
    }
}

suspend inline fun <reified Response : Any> HttpClient.delete(
    route: String,
    queryParameters: Map<String, Any?> = emptyMap()
): Result<Response, DataError.NetworkError> = safeCall {
    delete {
        url(constructRoute(route))
        queryParameters.forEach { (key, value) -> parameter(key, value) }
    }
}

suspend inline fun <reified Request : Any, reified Response : Any> HttpClient.post(
    route: String,
    body: Request
): Result<Response, DataError.NetworkError> = safeCall {
    post {
        url(constructRoute(route))
        setBody(body)
    }
}

suspend inline fun <reified Request : Any, reified Response : Any> HttpClient.delete(
    route: String,
    body: Request,
    queryParameters: Map<String, Any?> = emptyMap()
): Result<Response, DataError.NetworkError> = safeCall {
    delete {
        url(constructRoute(route))
        queryParameters.forEach { (key, value) -> parameter(key, value) }
        setBody(body)
    }
}

suspend inline fun <reified T> safeCall(
    execute: () -> HttpResponse
): Result<T, DataError.NetworkError> {
    val response = try {
        execute()
    } catch (e: UnresolvedAddressException) {
        return Result.Error(DataError.NetworkError.NO_INTERNET)
    } catch (e: SerializationException) {
        return Result.Error(DataError.NetworkError.SERIALIZATION)
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        return Result.Error(DataError.NetworkError.UNKNOWN)
    }
    return responseToResult(response)
}

suspend inline fun <reified T> responseToResult(
    response: HttpResponse
): Result<T, DataError.NetworkError> = when (response.status.value) {
    in 200..299 -> Result.Success(response.body())
    401 -> Result.Error(DataError.NetworkError.UNAUTHORIZED)
    404 -> Result.Error(DataError.NetworkError.NOT_FOUND)
    408 -> Result.Error(DataError.NetworkError.REQUEST_TIMEOUT)
    409 -> Result.Error(DataError.NetworkError.CONFLICT)
    in 500..599 -> Result.Error(DataError.NetworkError.SERVER_ERROR)
    else -> Result.Error(DataError.NetworkError.UNKNOWN)
}

fun HttpClient.constructRoute(route: String): String {
    val baseUrl = attributes[BaseUrlKey]
    return when {
        route.contains(baseUrl) -> route
        route.startsWith("/") -> baseUrl + route.drop(1)
        else -> baseUrl + route
    }
}
