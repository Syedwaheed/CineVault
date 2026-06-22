package com.edu.auth.presentation.login

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.edu.core.presentation.designsystem.CineVaultTheme
import com.edu.core.presentation.ui.UiText
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * WHY instrumentation test here?
 * LoginContent renders animated states (AnimatedContent), reads string resources,
 * and uses Material3 theme colors. All of these need a real Android environment —
 * they cannot run on the JVM unit test side.
 *
 * WHAT we test: each LoginUiState produces the correct visible UI, and user
 * interactions fire the correct LoginAction. We do NOT test the ViewModel or
 * any navigation — those are unit-tested separately.
 */
@RunWith(AndroidJUnit4::class)
class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Helper: render LoginContent in our theme with a given state
    private fun setLoginContent(
        uiState: LoginUiState,
        onAction: (LoginAction) -> Unit = {},
    ) {
        composeTestRule.setContent {
            CineVaultTheme {
                LoginContent(uiState = uiState, onAction = onAction)
            }
        }
    }

    // ── Idle state ─────────────────────────────────────────────────────────────

    @Test
    fun idleState_showsBrandTitle() {
        // GIVEN: the screen has just launched, no login attempt made yet
        setLoginContent(uiState = LoginUiState.Idle)

        // THEN: the "CineVault" brand name is split across two Text nodes ("Cine" + "Vault")
        // We assert "Vault" since it has the distinctive amber style
        composeTestRule
            .onNodeWithText("Vault")
            .assertIsDisplayed()
    }

    @Test
    fun idleState_showsContinueWithTmdbButton() {
        // GIVEN: idle state
        setLoginContent(uiState = LoginUiState.Idle)

        // THEN: the primary CTA button is visible
        // The string "Continue with TMDB" comes from R.string.login_continue_with_tmdb
        composeTestRule
            .onNodeWithText("Continue with TMDB")
            .assertIsDisplayed()
    }

    @Test
    fun idleState_clickContinueButton_firesContinueWithTmdbAction() {
        // GIVEN: track which actions are fired
        val actionsReceived = mutableListOf<LoginAction>()
        setLoginContent(
            uiState = LoginUiState.Idle,
            onAction = { actionsReceived.add(it) },
        )

        // WHEN: user taps the button
        composeTestRule
            .onNodeWithText("Continue with TMDB")
            .performClick()

        // THEN: the correct action is dispatched
        assertTrue(actionsReceived.contains(LoginAction.ContinueWithTmdb))
    }

    // ── WaitingForApproval state ───────────────────────────────────────────────

    @Test
    fun waitingForApproval_showsApprovalInstructions() {
        // GIVEN: token fetched, browser opened, waiting for user to approve
        setLoginContent(uiState = LoginUiState.WaitingForApproval)

        // THEN: the instructions card title is visible
        // "Approve in your browser" comes from R.string.login_approval_title
        composeTestRule
            .onNodeWithText("Approve in your browser")
            .assertIsDisplayed()
    }

    @Test
    fun waitingForApproval_showsIveApprovedButton() {
        setLoginContent(uiState = LoginUiState.WaitingForApproval)

        composeTestRule
            .onNodeWithText("I've approved it")
            .assertIsDisplayed()
    }

    @Test
    fun waitingForApproval_clickCancel_firesCancelAction() {
        val actionsReceived = mutableListOf<LoginAction>()
        setLoginContent(
            uiState = LoginUiState.WaitingForApproval,
            onAction = { actionsReceived.add(it) },
        )

        // WHEN: user changes their mind and taps Cancel
        composeTestRule
            .onNodeWithText("Cancel")
            .performClick()

        // THEN: Cancel action is fired so the ViewModel can reset the flow
        assertTrue(actionsReceived.contains(LoginAction.Cancel))
    }

    // ── Loading states ─────────────────────────────────────────────────────────

    @Test
    fun fetchingToken_showsConnectingMessage() {
        // GIVEN: waiting for the API to hand us a request token
        setLoginContent(uiState = LoginUiState.FetchingToken)

        // THEN: the button is in loading mode and shows "Connecting…"
        composeTestRule
            .onNodeWithText("Connecting…")
            .assertIsDisplayed()
    }

    @Test
    fun loggingIn_showsSigningInMessage() {
        setLoginContent(uiState = LoginUiState.LoggingIn)

        composeTestRule
            .onNodeWithText("Signing in…")
            .assertIsDisplayed()
    }

    // ── Error state ────────────────────────────────────────────────────────────

    @Test
    fun errorState_showsSomethingWentWrongTitle() {
        // GIVEN: a network or auth error occurred
        setLoginContent(
            uiState = LoginUiState.Error(UiText.DynamicString("No internet connection")),
        )

        // THEN: the error card title is shown
        composeTestRule
            .onNodeWithText("Something went wrong")
            .assertIsDisplayed()
    }

    @Test
    fun errorState_showsErrorDetailMessage() {
        setLoginContent(
            uiState = LoginUiState.Error(UiText.DynamicString("No internet connection")),
        )

        composeTestRule
            .onNodeWithText("No internet connection")
            .assertIsDisplayed()
    }

    @Test
    fun errorState_showsTryAgainButton() {
        setLoginContent(
            uiState = LoginUiState.Error(UiText.DynamicString("Timeout")),
        )

        composeTestRule
            .onNodeWithText("Try again")
            .assertIsDisplayed()
    }

    @Test
    fun errorState_clickTryAgain_firesRetryClickedAction() {
        val actionsReceived = mutableListOf<LoginAction>()
        setLoginContent(
            uiState = LoginUiState.Error(UiText.DynamicString("Timeout")),
            onAction = { actionsReceived.add(it) },
        )

        composeTestRule
            .onNodeWithText("Try again")
            .performClick()

        assertTrue(actionsReceived.contains(LoginAction.RetryClicked))
    }
}
