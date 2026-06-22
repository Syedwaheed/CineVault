package com.edu.feature.home.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.edu.core.domain.movie.Movie
import com.edu.core.presentation.designsystem.CineVaultTheme
import com.edu.core.presentation.designsystem.components.SyncState
import com.edu.core.presentation.ui.UiText
import kotlinx.collections.immutable.persistentListOf
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * WHY instrumentation test?
 * HomeContent contains LazyColumn, LazyRow, HorizontalPager, and Coil image loading.
 * These all need a real rendering environment — the pager state, scroll positions,
 * and lazy composition cannot be verified from the JVM side.
 *
 * WHAT we test: each HomeUiState (Loading / Success / Error) renders the correct
 * elements and user interactions fire the right HomeAction.
 */
@RunWith(AndroidJUnit4::class)
class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // ── Fake data ──────────────────────────────────────────────────────────────

    private fun fakeMovie(id: Int, title: String) = Movie(
        id = id,
        title = title,
        overview = "Overview of $title",
        posterPath = null,
        backdropPath = null,
        releaseDate = "2025-01-01",
        voteAverage = 8.5,
        voteCount = 1000,
        popularity = 500.0,
        adult = false,
        originalLanguage = "en",
        originalTitle = title,
        genreIds = listOf(28),
    )

    private val trendingMovies = persistentListOf(
        fakeMovie(1, "Glass Cathedral"),
        fakeMovie(2, "The Lantern Hours"),
    )

    private val successState = HomeUiState.Success(
        trendingMovies = trendingMovies,
        topRatedMovies = persistentListOf(fakeMovie(3, "Northwind")),
        nowPlayingMovies = persistentListOf(fakeMovie(4, "Crimson Frequency")),
        upcomingMovies = persistentListOf(fakeMovie(5, "The Last Signal")),
        syncState = SyncState.Idle,
    )

    // Helper
    private fun setHomeContent(
        uiState: HomeUiState,
        onAction: (HomeAction) -> Unit = {},
    ) {
        composeTestRule.setContent {
            CineVaultTheme {
                HomeContent(
                    uiState = uiState,
                    imageBaseUrl = "",
                    onAction = onAction,
                    contentPadding = PaddingValues(),
                )
            }
        }
    }

    // ── Loading state ──────────────────────────────────────────────────────────

    @Test
    fun loadingState_doesNotShowMovieTitles() {
        // GIVEN: data is still being fetched
        setHomeContent(uiState = HomeUiState.Loading)

        // THEN: no real movie title is visible (only shimmer placeholders are shown)
        composeTestRule
            .onNodeWithText("Glass Cathedral")
            .assertDoesNotExist()
    }

    @Test
    fun loadingState_showsSearchButton() {
        // GIVEN: loading
        setHomeContent(uiState = HomeUiState.Loading)

        // THEN: the search icon button in the top bar is always visible regardless of state
        composeTestRule
            .onNodeWithContentDescription("Search")
            .assertIsDisplayed()
    }

    // ── Success state ──────────────────────────────────────────────────────────

    @Test
    fun successState_showsSectionHeaders() {
        // GIVEN: movies loaded successfully across all 4 categories
        setHomeContent(uiState = successState)

        // Sections below the hero carousel may not be composed yet; scroll the list to each.
        composeTestRule.onNodeWithTag("home_content").performScrollToNode(hasText("Trending Now"))
        composeTestRule.onNodeWithText("Trending Now").assertIsDisplayed()
        composeTestRule.onNodeWithTag("home_content").performScrollToNode(hasText("Top Rated"))
        composeTestRule.onNodeWithText("Top Rated").assertIsDisplayed()
        composeTestRule.onNodeWithTag("home_content").performScrollToNode(hasText("Now Playing"))
        composeTestRule.onNodeWithText("Now Playing").assertIsDisplayed()
        composeTestRule.onNodeWithTag("home_content").performScrollToNode(hasText("Upcoming"))
        composeTestRule.onNodeWithText("Upcoming").assertIsDisplayed()
    }

    @Test
    fun successState_showsMovieTitlesInTrendingSection() {
        // GIVEN: trending movies loaded
        setHomeContent(uiState = successState)

        // "Glass Cathedral" appears in both the hero carousel and the trending row;
        // assert at least the first instance is displayed.
        composeTestRule
            .onAllNodesWithText("Glass Cathedral")
            .onFirst()
            .assertIsDisplayed()
    }

    @Test
    fun successState_clickSearch_firesSearchClickedAction() {
        // GIVEN: success state
        val actionsReceived = mutableListOf<HomeAction>()
        setHomeContent(
            uiState = successState,
            onAction = { actionsReceived.add(it) },
        )

        // WHEN: user taps the search icon in the top bar
        composeTestRule
            .onNodeWithContentDescription("Search")
            .performClick()

        // THEN: SearchClicked action is dispatched to the ViewModel
        assertTrue(actionsReceived.contains(HomeAction.SearchClicked))
    }

    // ── Error state ────────────────────────────────────────────────────────────

    @Test
    fun errorState_showsErrorMessage() {
        // GIVEN: the data load failed
        setHomeContent(
            uiState = HomeUiState.Error(UiText.DynamicString("No internet connection")),
        )

        // THEN: the error message is shown to the user
        composeTestRule
            .onNodeWithText("No internet connection")
            .assertIsDisplayed()
    }

    @Test
    fun errorState_showsRetryButton() {
        setHomeContent(
            uiState = HomeUiState.Error(UiText.DynamicString("Something went wrong")),
        )

        composeTestRule
            .onNodeWithText("Retry")
            .assertIsDisplayed()
    }

    @Test
    fun errorState_clickRetry_firesRetryAction() {
        // GIVEN: error state with action tracking
        val actionsReceived = mutableListOf<HomeAction>()
        setHomeContent(
            uiState = HomeUiState.Error(UiText.DynamicString("Timeout")),
            onAction = { actionsReceived.add(it) },
        )

        // WHEN: user taps Retry
        composeTestRule
            .onNodeWithText("Retry")
            .performClick()

        // THEN: Retry action is dispatched so the ViewModel re-fetches movies
        assertTrue(actionsReceived.contains(HomeAction.Retry))
    }
}
