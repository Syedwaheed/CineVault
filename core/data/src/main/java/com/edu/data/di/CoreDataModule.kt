package com.edu.data.di

import com.edu.core.domain.storage.SecureDataStorage
import com.edu.data.storage.EncryptedDataStoreSecureStorage
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val coreDataModule = module {
    single<SecureDataStorage> {
        EncryptedDataStoreSecureStorage(androidApplication())
    }
}