package com.edu.feature.watchlist.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.edu.core.domain.movie.Movie
import com.edu.core.presentation.designsystem.CineVaultTheme
import com.edu.core.presentation.ui.UiText
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * WHY instrumentation test?
 * WatchlistContent contains a Scaffold with TopAppBar, a SwipeToDismissBox
 * per movie item, and an animated color transition on swipe. These are all
 * UI-only concerns that need a real Compose environment to verify.
 *
 * WHAT we test: each WatchlistUiState renders the correct elements, and
 * user taps fire the correct WatchlistAction. Swipe-to-dismiss is not tested
 * here because it requires touch gesture simulation via TouchInjectionScope,
 * which is covered in a separate advanced test class.
 */
@RunWith(AndroidJUnit4::class)
class WatchlistScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // ── Fake data ──────────────────────────────────────────────────────────────

    private fun fakeMovie(id: Int, title: String) = Movie(
        id = id,
        title = title,
        overview = "Overview of $title",
        posterPath = null,
        backdropPath = null,
        releaseDate = "2025-01-15",
        voteAverage = 8.5,
        voteCount = 1000,
        popularity = 500.0,
        adult = false,
        originalLanguage = "en",
        originalTitle = title,
        genreIds = listOf(18),
    )

    private val watchlistMovies = listOf(
        fakeMovie(1, "Glass Cathedral"),
        fakeMovie(2, "The Lantern Hours"),
        fakeMovie(3, "Northwind"),
    )

    // Helper: WatchlistContent requires a SnackbarHostState — we provide a
    // fresh one each time so snackbars from one test don't leak into another.
    private fun setWatchlistContent(
        uiState: WatchlistUiState,
        onAction: (WatchlistAction) -> Unit = {},
    ) {
        composeTestRule.setContent {
            CineVaultTheme {
                WatchlistContent(
                    uiState = uiState,
                    imageBaseUrl = "",
                    snackbarHostState = remember { SnackbarHostState() },
                    onAction = onAction,
                    contentPadding = PaddingValues(),
                )
            }
        }
    }

    // ── Loading state ──────────────────────────────────────────────────────────

    @Test
    fun loadingState_showsWatchlistTitle() {
        // GIVEN: data is still being fetched from the DB
        setWatchlistContent(uiState = WatchlistUiState.Loading)

        // THEN: the TopAppBar title "Watchlist" is visible in any state
        composeTestRule
            .onNodeWithText("Watchlist")
            .assertIsDisplayed()
    }

    @Test
    fun loadingState_doesNotShowMovieTitles() {
        setWatchlistContent(uiState = WatchlistUiState.Loading)

        // Loading shows shimmer placeholders, not real movie titles
        composeTestRule
            .onNodeWithText("Glass Cathedral")
            .assertDoesNotExist()
    }

    // ── Empty state ────────────────────────────────────────────────────────────

    @Test
    fun emptyState_showsEmptyVaultMessage() {
        // GIVEN: user's watchlist is empty
        setWatchlistContent(uiState = WatchlistUiState.Empty)

        // THEN: the empty state message is shown
        composeTestRule
            .onNodeWithText("Your vault is empty")
            .assertIsDisplayed()
    }

    @Test
    fun emptyState_showsBrowseMoviesButton() {
        setWatchlistContent(uiState = WatchlistUiState.Empty)

        composeTestRule
            .onNodeWithText("Browse movies")
            .assertIsDisplayed()
    }

    @Test
    fun emptyState_clickBrowseMovies_firesBrowseMoviesAction() {
        val actionsReceived = mutableListOf<WatchlistAction>()
        setWatchlistContent(
            uiState = WatchlistUiState.Empty,
            onAction = { actionsReceived.add(it) },
        )

        // WHEN: user taps the "Browse movies" CTA to discover films
        composeTestRule
            .onNodeWithText("Browse movies")
            .performClick()

        // THEN: BrowseMovies triggers navigation to the Home tab
        assertTrue(actionsReceived.contains(WatchlistAction.BrowseMovies))
    }

    // ── Success state ──────────────────────────────────────────────────────────

    @Test
    fun successState_showsAllMovieTitles() {
        // GIVEN: watchlist has 3 saved movies
        setWatchlistContent(uiState = WatchlistUiState.Success(watchlistMovies))

        // THEN: all titles are visible in the list
        composeTestRule.onNodeWithText("Glass Cathedral").assertIsDisplayed()
        composeTestRule.onNodeWithText("The Lantern Hours").assertIsDisplayed()
        composeTestRule.onNodeWithText("Northwind").assertIsDisplayed()
    }

    @Test
    fun successState_showsCountInTopBar() {
        // GIVEN: 3 saved movies
        setWatchlistContent(uiState = WatchlistUiState.Success(watchlistMovies))

        // THEN: the subtitle under "Watchlist" shows "3 titles"
        composeTestRule
            .onNodeWithText("3 titles")
            .assertIsDisplayed()
    }

    @Test
    fun successState_singularTitle_showsCorrectGrammar() {
        // Edge case: "1 title" not "1 titles"
        setWatchlistContent(
            uiState = WatchlistUiState.Success(listOf(fakeMovie(1, "Only Movie"))),
        )

        composeTestRule
            .onNodeWithText("1 title")
            .assertIsDisplayed()
    }

    @Test
    fun successState_clickMovie_firesMovieClickedAction() {
        val actionsReceived = mutableListOf<WatchlistAction>()
        setWatchlistContent(
            uiState = WatchlistUiState.Success(watchlistMovies),
            onAction = { actionsReceived.add(it) },
        )

        // WHEN: user taps the first movie row
        composeTestRule
            .onNodeWithText("Glass Cathedral")
            .performClick()

        // THEN: MovieClicked is dispatched with the correct movie ID
        assertTrue(actionsReceived.any { it is WatchlistAction.MovieClicked && it.movieId == 1 })
    }

    // ── Error state ────────────────────────────────────────────────────────────

    @Test
    fun errorState_showsErrorMessage() {
        setWatchlistContent(
            uiState = WatchlistUiState.Error(UiText.DynamicString("Database error")),
        )

        composeTestRule
            .onNodeWithText("Database error")
            .assertIsDisplayed()
    }
}
