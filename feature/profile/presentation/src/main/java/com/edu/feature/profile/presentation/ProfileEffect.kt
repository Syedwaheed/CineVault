package com.edu.feature.profile.presentation

import com.edu.core.presentation.ui.UiText

sealed interface ProfileEffect {
    data object NavigateToAuth : ProfileEffect
    data class ShowMessage(val message: UiText) : ProfileEffect
}
