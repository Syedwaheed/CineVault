package com.edu.core.domain.storage

interface SecureDataStorage {
    suspend fun putString(key: String, value: String)
    suspend fun getString(key: String): String?
    suspend fun putInt(key: String, value: Int)
    suspend fun getInt(key: String): Int?
    suspend fun remove(key: String)
    suspend fun clear()
    fun contains(key: String) : Boolean
}