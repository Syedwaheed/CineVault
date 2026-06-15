package com.edu.data.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.edu.core.domain.storage.SecureDataStorage
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlin.io.encoding.Base64
import kotlin.jvm.java


private val Context.sessionDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "secure_session"
)

class EncryptedDataStoreSecureStorage(
    context: Context
) : SecureDataStorage {
    private val sessionDataStore = context.sessionDataStore
    private val aead: Aead

    init {
        AeadConfig.register()
        val keysetHandle = AndroidKeysetManager.Builder()
            .withSharedPref(context, "session_key", "session_pref")
            .withKeyTemplate(KeyTemplates.get("AES256_GCM"))
            .withMasterKeyUri("android-keystore://session_key")
            .build()
            .keysetHandle
        aead = keysetHandle.getPrimitive(Aead::class.java)
    }


    override suspend fun putString(key: String, value: String) {
        val encryptedValue = encrypt(value)
        sessionDataStore.edit { prefs->
            prefs[stringPreferencesKey(key)] = encryptedValue
        }
    }


    override suspend fun getString(key: String): String? {
        val pref = sessionDataStore.data.first()
        val encrypted = pref[stringPreferencesKey(key)] ?: return null
        return decrypt(encrypted)
    }

    override suspend fun putInt(key: String, value: Int) {
        sessionDataStore.edit { prefs ->
            prefs[intPreferencesKey(key)] = value

        }
    }

    override suspend fun getInt(key: String): Int? {
        val pref = sessionDataStore.data.first()
        return pref[intPreferencesKey(key)]
    }

    override suspend fun remove(key: String) {
        sessionDataStore.edit { prefs ->
            prefs.remove(stringPreferencesKey(key))
            prefs.remove(intPreferencesKey(key))
        }
    }

    override suspend fun clear() {
        sessionDataStore.edit { prefs ->
            prefs.clear()
        }
    }

    override fun contains(key: String): Boolean {
        return runBlocking {
            val prefs = sessionDataStore.data.first()
            prefs.contains(stringPreferencesKey(key)) || prefs.contains(intPreferencesKey(key))
        }


    }

    private fun encrypt(value: String): String {
        val encryptValue = aead.encrypt(value.toByteArray(), null)
        return Base64.encode(encryptValue)
    }

    private fun decrypt(encryptedValue: String): String? {
        return try {
            val decoded = Base64.decode(encryptedValue)
            val decrypted = aead.decrypt(decoded, null)
            String(decrypted)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}