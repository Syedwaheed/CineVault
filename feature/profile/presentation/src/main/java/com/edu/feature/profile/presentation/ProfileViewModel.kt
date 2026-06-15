package com.edu.feature.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.core.presentation.ui.UiText
import com.edu.feature.profile.domain.GetProfileUseCase
import com.edu.feature.profile.domain.GetUserStatsUseCase
import com.edu.feature.profile.domain.LogoutProfileUseCase
import com.edu.core.domain.util.NoParams
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getProfile: GetProfileUseCase,
    private val getUserStats: GetUserStatsUseCase,
    private val logout: LogoutProfileUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<ProfileEffect>()
    val uiEffect: SharedFlow<ProfileEffect> = _uiEffect.asSharedFlow()

    init {
        observeProfile()
    }

    fun onAction(action: ProfileAction) {
        when (action) {
            ProfileAction.InitiateLogout -> Unit  // dialog handled in UI
            ProfileAction.ConfirmLogout -> performLogout()
            ProfileAction.AvatarClick -> Unit
            ProfileAction.Retry -> observeProfile()
            is ProfileAction.SettingClick -> viewModelScope.launch {
                _uiEffect.emit(
                    ProfileEffect.ShowMessage(UiText.DynamicString("${action.type.label} — coming soon"))
                )
            }
        }
    }

    private fun observeProfile() {
        viewModelScope.launch {
            combine(
                getProfile(NoParams),
                getUserStats(NoParams),
            ) { account, stats ->
                ProfileUiState.Success(account = account, stats = stats)
            }
                .catch { e ->
                    _uiState.value = ProfileUiState.Error(
                        UiText.DynamicString(e.message ?: "Failed to load profile")
                    )
                }
                .collect { state ->
                    _uiState.value = state
                }
        }
    }

    private fun performLogout() {
        viewModelScope.launch {
            logout(NoParams)
            _uiEffect.emit(ProfileEffect.NavigateToAuth)
        }
    }
}

private val SettingType.label: String
    get() = when (this) {
        SettingType.NOTIFICATIONS -> "Notifications"
        SettingType.THEME -> "Theme"
        SettingType.LANGUAGE -> "Language"
        SettingType.ABOUT -> "About"
    }
