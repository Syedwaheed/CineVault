package com.edu.auth.presentation.di

import com.edu.auth.domain.usecase.GetAuthorizationUrlUseCase
import com.edu.auth.domain.usecase.GetRequestTokenUseCase
import com.edu.auth.domain.usecase.IsLoggedInUseCase
import com.edu.auth.domain.usecase.LoginUseCase
import com.edu.auth.domain.usecase.LogoutUseCase
import com.edu.auth.presentation.login.LoginViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val authModule = module {
    factory { GetRequestTokenUseCase(get()) }
    factory { GetAuthorizationUrlUseCase(get()) }
    factory { LoginUseCase(get(), get()) }
    factory { LogoutUseCase(get(), get()) }
    factory { IsLoggedInUseCase(get()) }

    viewModel { LoginViewModel(get(), get(), get()) }
}
