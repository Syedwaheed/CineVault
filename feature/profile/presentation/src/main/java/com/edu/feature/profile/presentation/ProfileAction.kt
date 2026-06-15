package com.edu.feature.profile.presentation

sealed interface ProfileAction {
    data object InitiateLogout : ProfileAction
    data object ConfirmLogout : ProfileAction
    data object AvatarClick : ProfileAction
    data object Retry : ProfileAction
    data class SettingClick(val type: SettingType) : ProfileAction
}

enum class SettingType {
    NOTIFICATIONS,
    THEME,
    LANGUAGE,
    ABOUT,
}
