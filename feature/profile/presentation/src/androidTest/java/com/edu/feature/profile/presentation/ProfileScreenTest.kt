package com.edu.feature.profile.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.edu.core.domain.auth.Account
import com.edu.core.domain.auth.UserStats
import com.edu.core.presentation.designsystem.CineVaultTheme
import com.edu.core.presentation.ui.UiText
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProfileScreenTest {

    // This is the entry point for every Compose UI test.
    // It starts a Compose environment on the device so you can render and interact with composables.
    @get:Rule
    val composeTestRule = createComposeRule()

    // ── Helpers ───────────────────────────────────────────────────────────────

    private val fakeAccount = Account(
        id = 1,
        name = "Waheed Shah",
        username = "waheed",
        includeAdult = false,
        language = "en",
        country = "PK",
        avatarPath = null,
    )

    private val fakeStats = UserStats(watchlistCount = 12, moviesWatched = 85)

    // setContent{} renders your composable, just like Android renders it on a real screen.
    private fun setProfileContent(
        uiState: ProfileUiState,
        onAction: (ProfileAction) -> Unit = {},
    ) {
        composeTestRule.setContent {
            CineVaultTheme {
                ProfileContent(
                    uiState = uiState,
                    imageBaseUrl = "",
                    snackbarHostState = remember { SnackbarHostState() },
                    contentPadding = PaddingValues(),
                    onAction = onAction,
                )
            }
        }
    }

    // ── Tests ─────────────────────────────────────────────────────────────────

    @Test
    fun loadingState_showsNoProfileName() {
        // GIVEN: the screen is loading
        setProfileContent(uiState = ProfileUiState.Loading)

        // THEN: the user's name is not shown yet (shimmer placeholders are displayed instead)
        composeTestRule
            .onNodeWithText(fakeAccount.name)
            .assertDoesNotExist()
    }

    @Test
    fun successState_showsUsernameAndStats() {
        // GIVEN: profile loaded successfully
        setProfileContent(
            uiState = ProfileUiState.Success(
                account = fakeAccount,
                stats = fakeStats,
            )
        )

        // THEN: the display name is visible
        composeTestRule
            .onNodeWithText("Waheed Shah")
            .assertIsDisplayed()

        // AND: the username is visible
        composeTestRule
            .onNodeWithText("@waheed")
            .assertIsDisplayed()

        // AND: watchlist count is visible
        composeTestRule
            .onNodeWithText("12")
            .assertIsDisplayed()

        // AND: watched count is visible
        composeTestRule
            .onNodeWithText("85")
            .assertIsDisplayed()
    }

    @Test
    fun successState_clickSignOut_showsConfirmationDialog() {
        // GIVEN: profile is in success state
        setProfileContent(
            uiState = ProfileUiState.Success(
                account = fakeAccount,
                stats = fakeStats,
            )
        )

        // WHEN: user clicks "Sign out"
        composeTestRule
            .onNodeWithText("Sign out")
            .performClick()

        // THEN: the confirmation dialog appears
        composeTestRule
            .onNodeWithText("Sign out?")
            .assertIsDisplayed()
    }

    @Test
    fun logoutDialog_clickConfirm_triggersConfirmLogoutAction() {
        // GIVEN: track which actions are fired
        val actionsReceived = mutableListOf<ProfileAction>()

        setProfileContent(
            uiState = ProfileUiState.Success(account = fakeAccount, stats = fakeStats),
            onAction = { actionsReceived.add(it) },
        )

        // WHEN: user opens the dialog and confirms
        // first click: the settings row (no Button role)
        composeTestRule.onNodeWithText("Sign out").performClick()
        composeTestRule.onNodeWithText("Sign out?").assertIsDisplayed()
        // second click: target the dialog confirm button via its testTag
        composeTestRule
            .onNodeWithTag("confirm_logout_button")
            .performClick()

        // THEN: ConfirmLogout action was fired
        assertTrue(actionsReceived.contains(ProfileAction.ConfirmLogout))
    }

    @Test
    fun errorState_showsErrorMessageAndRetryButton() {
        // GIVEN: profile failed to load
        setProfileContent(
            uiState = ProfileUiState.Error(
                message = UiText.DynamicString("Failed to load profile")
            )
        )

        // THEN: the error message is visible
        composeTestRule
            .onNodeWithText("Failed to load profile")
            .assertIsDisplayed()

        // AND: the retry button is visible
        composeTestRule
            .onNodeWithText("Retry")
            .assertIsDisplayed()
    }

    @Test
    fun errorState_clickRetry_triggersRetryAction() {
        // GIVEN: track which actions are fired
        val actionsReceived = mutableListOf<ProfileAction>()

        setProfileContent(
            uiState = ProfileUiState.Error(UiText.DynamicString("Something went wrong")),
            onAction = { actionsReceived.add(it) },
        )

        // WHEN: user clicks Retry
        composeTestRule
            .onNodeWithText("Retry")
            .performClick()

        // THEN: Retry action was fired
        assertTrue(actionsReceived.contains(ProfileAction.Retry))
    }
}