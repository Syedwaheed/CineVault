package com.edu.feature.detail.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.edu.core.domain.movie.Actor
import com.edu.core.domain.movie.Movie
import com.edu.core.presentation.designsystem.CineVaultTheme
import com.edu.core.presentation.ui.UiText
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * WHY instrumentation test?
 * DetailScreen renders a LazyColumn with a backdrop hero image, shared element
 * transitions, a YouTube player overlay, and a cast row. These all need real
 * Compose layout to verify.
 *
 * WHY we test sub-composables (DetailLoadingContent, DetailSuccessContent,
 * DetailErrorContent) instead of DetailScreen directly?
 *
 * DetailScreen is "stateful" — it uses a Koin ViewModel and requires full DI
 * to be set up. That's not what UI tests should do; UI tests verify visual
 * correctness, not DI wiring. By making the three content composables
 * `internal`, we can inject fake state directly and test the UI in isolation.
 *
 * WHAT we test: correct rendering for Loading / Success / Error, back button
 * action, and watchlist toggle action.
 */
@RunWith(AndroidJUnit4::class)
class DetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // ── Fake data ──────────────────────────────────────────────────────────────

    private val fakeMovie = Movie(
        id = 42,
        title = "Glass Cathedral",
        overview = "An architect designs a building that cannot exist.",
        posterPath = null,
        backdropPath = null,
        releaseDate = "2025-01-15",
        voteAverage = 9.1,
        voteCount = 2847,
        popularity = 980.0,
        adult = false,
        originalLanguage = "en",
        originalTitle = "Glass Cathedral",
        genreIds = listOf(878, 53),
        runtime = 132,
    )

    private val fakeActor = Actor(
        id = 1,
        name = "Elena Hartwell",
        character = "The Architect",
        profilePath = null,
        order = 0,
        knownForDepartment = "Acting",
    )

    private val successState = DetailUiState.Success(
        movie = fakeMovie,
        cast = listOf(fakeActor),
        similarMovies = emptyList(),
        isCastLoading = false,
        isInWatchlist = false,
    )

    // ── Loading state ──────────────────────────────────────────────────────────

    @Test
    fun loadingState_showsBackButton() {
        // GIVEN: movie detail is loading (user navigated to the screen, data in flight)
        composeTestRule.setContent {
            CineVaultTheme {
                DetailLoadingContent(onBack = {})
            }
        }

        // THEN: the back arrow is visible so the user can navigate away even during loading
        // The IconButton has contentDescription = "Back" (set in DetailLoadingContent)
        composeTestRule
            .onNodeWithContentDescription("Back")
            .assertIsDisplayed()
    }

    @Test
    fun loadingState_clickBack_firesOnBackCallback() {
        // GIVEN: track back callback
        var backCalled = false
        composeTestRule.setContent {
            CineVaultTheme {
                DetailLoadingContent(onBack = { backCalled = true })
            }
        }

        // WHEN: user taps Back while loading
        composeTestRule
            .onNodeWithContentDescription("Back")
            .performClick()

        // THEN: the callback fires (which the screen converts to DetailAction.Back)
        assertTrue(backCalled)
    }

    // ── Success state ──────────────────────────────────────────────────────────

    @Test
    fun successState_showsMovieTitle() {
        // GIVEN: movie data loaded
        composeTestRule.setContent {
            CineVaultTheme {
                DetailSuccessContent(
                    state = successState,
                    imageBaseUrl = "",
                    onBack = {},
                    onWatchTrailer = {},
                    onWatchlist = {},
                    onSimilarMovieClick = {},
                )
            }
        }

        // THEN: the movie title is displayed in the detail layout
        composeTestRule
            .onNodeWithText("Glass Cathedral")
            .assertIsDisplayed()
    }

    @Test
    fun successState_showsMovieOverview() {
        composeTestRule.setContent {
            CineVaultTheme {
                DetailSuccessContent(
                    state = successState,
                    imageBaseUrl = "",
                    onBack = {},
                    onWatchTrailer = {},
                    onWatchlist = {},
                    onSimilarMovieClick = {},
                )
            }
        }

        composeTestRule
            .onNodeWithText("An architect designs a building that cannot exist.")
            .assertIsDisplayed()
    }

    @Test
    fun successState_showsBackButton() {
        composeTestRule.setContent {
            CineVaultTheme {
                DetailSuccessContent(
                    state = successState,
                    imageBaseUrl = "",
                    onBack = {},
                    onWatchTrailer = {},
                    onWatchlist = {},
                    onSimilarMovieClick = {},
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription("Back")
            .assertIsDisplayed()
    }

    @Test
    fun successState_clickBack_firesOnBackCallback() {
        var backCalled = false
        composeTestRule.setContent {
            CineVaultTheme {
                DetailSuccessContent(
                    state = successState,
                    imageBaseUrl = "",
                    onBack = { backCalled = true },
                    onWatchTrailer = {},
                    onWatchlist = {},
                    onSimilarMovieClick = {},
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription("Back")
            .performClick()

        assertTrue(backCalled)
    }

    @Test
    fun successState_showsCastActorName() {
        // GIVEN: cast has loaded
        composeTestRule.setContent {
            CineVaultTheme {
                DetailSuccessContent(
                    state = successState,
                    imageBaseUrl = "",
                    onBack = {},
                    onWatchTrailer = {},
                    onWatchlist = {},
                    onSimilarMovieClick = {},
                )
            }
        }

        // THEN: the actor's name appears in the cast row
        composeTestRule
            .onNodeWithText("Elena Hartwell")
            .assertIsDisplayed()
    }

    @Test
    fun successState_clickWatchlist_firesOnWatchlistCallback() {
        // GIVEN: movie is not yet in watchlist
        var watchlistToggled = false
        composeTestRule.setContent {
            CineVaultTheme {
                DetailSuccessContent(
                    state = successState.copy(isInWatchlist = false),
                    imageBaseUrl = "",
                    onBack = {},
                    onWatchTrailer = {},
                    onWatchlist = { watchlistToggled = true },
                    onSimilarMovieClick = {},
                )
            }
        }

        // The bookmark icon button has contentDescription "Add to watchlist"
        composeTestRule
            .onNodeWithContentDescription("Add to watchlist")
            .performClick()

        assertTrue(watchlistToggled)
    }

    // ── Error state ────────────────────────────────────────────────────────────

    @Test
    fun errorState_showsErrorMessage() {
        // GIVEN: detail fetch failed
        composeTestRule.setContent {
            CineVaultTheme {
                DetailErrorContent(
                    message = "Movie not found",
                    onBack = {},
                )
            }
        }

        composeTestRule
            .onNodeWithText("Movie not found")
            .assertIsDisplayed()
    }

    @Test
    fun errorState_showsGoBackButton() {
        // DetailErrorContent uses a Button with text "Go back" (not an icon button)
        composeTestRule.setContent {
            CineVaultTheme {
                DetailErrorContent(
                    message = "Something went wrong",
                    onBack = {},
                )
            }
        }

        composeTestRule
            .onNodeWithText("Go back")
            .assertIsDisplayed()
    }

    @Test
    fun errorState_clickGoBack_firesOnBackCallback() {
        var backCalled = false
        composeTestRule.setContent {
            CineVaultTheme {
                DetailErrorContent(
                    message = "Error",
                    onBack = { backCalled = true },
                )
            }
        }

        composeTestRule
            .onNodeWithText("Go back")
            .performClick()

        assertTrue(backCalled)
    }
}
