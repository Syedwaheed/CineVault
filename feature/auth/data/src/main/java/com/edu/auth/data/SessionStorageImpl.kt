package com.edu.auth.data

import com.edu.auth.data.dto.mapper.toSession
import com.edu.auth.data.dto.mapper.toSessionDto
import com.edu.auth.data.dto.session.SessionDto
import com.edu.core.domain.auth.Session
import com.edu.core.domain.auth.SessionStorage
import com.edu.core.domain.storage.SecureDataStorage
import kotlinx.serialization.json.Json

class SessionStorageImpl(
    private val secureStorage: SecureDataStorage
): SessionStorage {

    override suspend fun storeSession(session: Session) {
        val dto = session.toSessionDto()
        val json = Json.encodeToString(dto)
        secureStorage.putString(AUTH_SESSION_ID, json)
    }

    override suspend fun getSession(): Session? {
        val json = secureStorage.getString(AUTH_SESSION_ID) ?: return null
        return try {
            Json.decodeFromString<SessionDto>(json).toSession()
        }catch (e: Exception){
            e.printStackTrace()
            null
        }
    }

    override suspend fun deleteSession() {
        secureStorage.remove(AUTH_SESSION_ID)
    }

    override fun isLoggedIn(): Boolean {
        return secureStorage.contains(AUTH_SESSION_ID)
    }

    companion object {
        private const val AUTH_SESSION_ID = "session_id"
    }
}