package com.edu.feature.search.presentation

import androidx.compose.foundation.layout.PaddingValues
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
 * SearchContent uses BasicTextField, LazyVerticalGrid, and horizontal genre filter
 * scrolling. These need a real Compose runtime to verify layout and interactions.
 *
 * WHAT we test: idle, loading, success (with and without results), and error states
 * all render the right UI, and genre / query actions fire correctly.
 *
 * NOTE: query and selectedGenre are passed as params to SearchContent — we test
 * the rendering given a state, not the typing itself. Typing tests would need
 * testTag on BasicTextField; those are a follow-up.
 */
@RunWith(AndroidJUnit4::class)
class SearchScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // ── Fake data ──────────────────────────────────────────────────────────────

    private val genres = listOf("All", "Action", "Drama", "Sci-Fi", "Comedy")

    private fun fakeMovie(id: Int, title: String) = Movie(
        id = id,
        title = title,
        overview = "Overview of $title",
        posterPath = null,
        backdropPath = null,
        releaseDate = "2025-03-10",
        voteAverage = 8.0,
        voteCount = 500,
        popularity = 300.0,
        adult = false,
        originalLanguage = "en",
        originalTitle = title,
        genreIds = listOf(28),
    )

    private val resultMovies = listOf(
        fakeMovie(1, "Glass Cathedral"),
        fakeMovie(2, "The Lantern Hours"),
    )

    // Helper
    private fun setSearchContent(
        uiState: SearchUiState,
        query: String = "",
        selectedGenre: String = "All",
        onAction: (SearchAction) -> Unit = {},
    ) {
        composeTestRule.setContent {
            CineVaultTheme {
                SearchContent(
                    uiState = uiState,
                    query = query,
                    selectedGenre = selectedGenre,
                    genres = genres,
                    imageBaseUrl = "",
                    onAction = onAction,
                    contentPadding = PaddingValues(),
                )
            }
        }
    }

    // ── Idle state (no query typed yet) ───────────────────────────────────────

    @Test
    fun idleState_showsDiscoveryPrompt() {
        // GIVEN: no search query typed yet
        setSearchContent(uiState = SearchUiState.Idle)

        // THEN: the empty-state prompt is visible to guide the user
        composeTestRule
            .onNodeWithText("Find your next film")
            .assertIsDisplayed()
    }

    @Test
    fun idleState_showsSearchPlaceholder() {
        setSearchContent(uiState = SearchUiState.Idle)

        // The BasicTextField shows its decorationBox placeholder when query is empty
        composeTestRule
            .onNodeWithText("Search movies, actors, directors…")
            .assertIsDisplayed()
    }

    @Test
    fun idleState_showsGenreChips() {
        // GIVEN: idle, genre list provided
        setSearchContent(uiState = SearchUiState.Idle)

        // THEN: genre filter chips are rendered (at least "All")
        composeTestRule
            .onNodeWithText("All")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Action")
            .assertIsDisplayed()
    }

    @Test
    fun idleState_clickGenreChip_firesGenreSelectedAction() {
        val actionsReceived = mutableListOf<SearchAction>()
        setSearchContent(
            uiState = SearchUiState.Idle,
            onAction = { actionsReceived.add(it) },
        )

        // WHEN: user taps the "Drama" genre chip
        composeTestRule
            .onNodeWithText("Drama")
            .performClick()

        // THEN: GenreSelected action carries the tapped genre
        assertTrue(actionsReceived.any { it is SearchAction.GenreSelected && it.genre == "Drama" })
    }

    // ── Loading state ──────────────────────────────────────────────────────────

    @Test
    fun loadingState_doesNotShowDiscoveryPrompt() {
        // GIVEN: query entered and search is in-flight
        setSearchContent(
            uiState = SearchUiState.Loading(query = "lantern", selectedGenre = "All"),
            query = "lantern",
        )

        // THEN: the idle "Find your next film" prompt is gone
        composeTestRule
            .onNodeWithText("Find your next film")
            .assertDoesNotExist()
    }

    // ── Success state — with results ───────────────────────────────────────────

    @Test
    fun successState_withResults_showsMovieTitles() {
        // GIVEN: search returned two results
        setSearchContent(
            uiState = SearchUiState.Success(
                query = "cathedral",
                results = resultMovies,
                selectedGenre = "All",
                isSearching = false,
            ),
            query = "cathedral",
        )

        // THEN: movie titles are rendered in the grid
        composeTestRule
            .onNodeWithText("Glass Cathedral")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("The Lantern Hours")
            .assertIsDisplayed()
    }

    @Test
    fun successState_withResults_showsResultCount() {
        setSearchContent(
            uiState = SearchUiState.Success(
                query = "test",
                results = resultMovies,
                selectedGenre = "All",
                isSearching = false,
            ),
            query = "test",
        )

        // The grid shows "2 RESULTS · SORTED BY RELEVANCE"
        composeTestRule
            .onNodeWithText("2 RESULTS · SORTED BY RELEVANCE")
            .assertIsDisplayed()
    }

    // ── Success state — empty results ──────────────────────────────────────────

    @Test
    fun successState_withEmptyResults_showsNoMatchesFound() {
        // GIVEN: search returned nothing
        setSearchContent(
            uiState = SearchUiState.Success(
                query = "zzqxx",
                results = emptyList(),
                selectedGenre = "All",
                isSearching = false,
            ),
            query = "zzqxx",
        )

        // THEN: the empty result state is shown
        composeTestRule
            .onNodeWithText("No matches found")
            .assertIsDisplayed()
    }

    @Test
    fun successState_withEmptyResults_showsBrowseGenresButton() {
        setSearchContent(
            uiState = SearchUiState.Success(
                query = "zzqxx",
                results = emptyList(),
                selectedGenre = "All",
                isSearching = false,
            ),
            query = "zzqxx",
        )

        composeTestRule
            .onNodeWithText("Browse genres")
            .assertIsDisplayed()
    }

    @Test
    fun successState_withEmptyResults_clickBrowseGenres_firesClearQueryAction() {
        val actionsReceived = mutableListOf<SearchAction>()
        setSearchContent(
            uiState = SearchUiState.Success(
                query = "zzqxx",
                results = emptyList(),
                selectedGenre = "All",
                isSearching = false,
            ),
            query = "zzqxx",
            onAction = { actionsReceived.add(it) },
        )

        // WHEN: user taps "Browse genres" to clear their dead-end search
        composeTestRule
            .onNodeWithText("Browse genres")
            .performClick()

        // THEN: ClearQuery resets the search back to idle
        assertTrue(actionsReceived.contains(SearchAction.ClearQuery))
    }

    // ── Error state ────────────────────────────────────────────────────────────

    @Test
    fun errorState_showsErrorMessage() {
        setSearchContent(
            uiState = SearchUiState.Error(
                query = "lantern",
                selectedGenre = "All",
                message = UiText.DynamicString("No internet connection"),
            ),
            query = "lantern",
        )

        composeTestRule
            .onNodeWithText("No internet connection")
            .assertIsDisplayed()
    }

    @Test
    fun errorState_showsRetryButton() {
        setSearchContent(
            uiState = SearchUiState.Error(
                query = "lantern",
                selectedGenre = "All",
                message = UiText.DynamicString("Timeout"),
            ),
        )

        composeTestRule
            .onNodeWithText("Retry")
            .assertIsDisplayed()
    }

    @Test
    fun errorState_clickRetry_firesQueryChangedWithOriginalQuery() {
        // The onRetry lambda calls: onAction(SearchAction.QueryChanged(uiState.query))
        val actionsReceived = mutableListOf<SearchAction>()
        setSearchContent(
            uiState = SearchUiState.Error(
                query = "lantern",
                selectedGenre = "All",
                message = UiText.DynamicString("Timeout"),
            ),
            query = "lantern",
            onAction = { actionsReceived.add(it) },
        )

        composeTestRule.onNodeWithText("Retry").performClick()

        // THEN: a QueryChanged with the same query is fired to re-trigger the search
        assertTrue(actionsReceived.any { it is SearchAction.QueryChanged && it.query == "lantern" })
    }
}
