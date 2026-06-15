package com.edu.feature.profile.presentation.di

import com.edu.feature.profile.domain.GetProfileUseCase
import com.edu.feature.profile.domain.GetUserStatsUseCase
import com.edu.feature.profile.domain.LogoutProfileUseCase
import com.edu.feature.profile.presentation.ProfileViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val profileModule = module {
    factory { GetProfileUseCase(get()) }
    factory { GetUserStatsUseCase(get()) }
    factory { LogoutProfileUseCase(get()) }

    viewModel {
        ProfileViewModel(
            getProfile = get(),
            getUserStats = get(),
            logout = get(),
        )
    }
}
