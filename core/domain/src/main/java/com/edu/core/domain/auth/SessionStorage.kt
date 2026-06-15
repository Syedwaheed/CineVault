package com.edu.core.domain.auth

interface SessionStorage {
    suspend fun storeSession(session: Session)
    suspend fun getSession(): Session?
    suspend fun deleteSession()
    fun isLoggedIn(): Boolean
}