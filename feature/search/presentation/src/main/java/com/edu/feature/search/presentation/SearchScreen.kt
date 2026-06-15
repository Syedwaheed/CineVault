package com.edu.feature.search.presentation

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import com.edu.core.domain.movie.Movie
import com.edu.core.presentation.designsystem.CineVaultAnimation
import com.edu.core.presentation.designsystem.CineVaultRadius
import com.edu.core.presentation.designsystem.CineVaultSpacing
import com.edu.core.presentation.designsystem.CineVaultTheme
import com.edu.core.presentation.designsystem.CvAmber
import com.edu.core.presentation.designsystem.CvBg
import com.edu.core.presentation.designsystem.CvBorder
import com.edu.core.presentation.designsystem.CvSurface
import com.edu.core.presentation.designsystem.CvText
import com.edu.core.presentation.designsystem.CvTextDim
import com.edu.core.presentation.designsystem.CvTextMute
import com.edu.core.presentation.designsystem.JetBrainsMonoFontFamily
import com.edu.core.presentation.designsystem.LocalSharedTransitionScope
import com.edu.core.presentation.designsystem.PlayfairDisplayFontFamily
import com.edu.core.presentation.designsystem.components.GenreChip
import com.edu.core.presentation.designsystem.components.MovieCard
import com.edu.core.presentation.designsystem.components.MovieCardShimmer
import com.edu.core.presentation.designsystem.components.shimmerEffect
import com.edu.core.presentation.designsystem.icon.close_image
import com.edu.core.presentation.designsystem.icon.play_arrow_image
import com.edu.core.presentation.designsystem.icon.search_image
import com.edu.core.presentation.ui.ObserveAsEvent
import com.edu.core.presentation.ui.UiText
import org.koin.androidx.compose.koinViewModel

private const val GRID_SHIMMER_COUNT = 8

private val tmdbGenreNames = mapOf(
    28 to "Action", 12 to "Adventure", 16 to "Animation", 35 to "Comedy",
    80 to "Crime", 99 to "Documentary", 18 to "Drama", 10751 to "Family",
    14 to "Fantasy", 36 to "History", 27 to "Horror", 10402 to "Music",
    9648 to "Mystery", 10749 to "Romance", 878 to "Sci-Fi", 53 to "Thriller",
    10752 to "War", 37 to "Western",
)

private fun Movie.primaryGenre(): String =
    genreIds.firstOrNull()?.let { tmdbGenreNames[it] } ?: ""

private fun Movie.releaseYear(): String = releaseDate.take(4)

private fun Movie.posterUrl(imageBaseUrl: String) =
    if (!posterPath.isNullOrEmpty()) "${imageBaseUrl}w500$posterPath" else ""

// ─── Stateful screen ──────────────────────────────────────────────────────────

@Composable
fun SearchScreen(
    onNavigateToDetail: (Int) -> Unit,
    imageBaseUrl: String,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    viewModel: SearchViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val query by viewModel.query.collectAsStateWithLifecycle()
    val selectedGenre by viewModel.selectedGenre.collectAsStateWithLifecycle()

    ObserveAsEvent(viewModel.uiEffect) { effect ->
        when (effect) {
            is SearchEffect.NavigateToDetail -> onNavigateToDetail(effect.movieId)
        }
    }

    SearchContent(
        uiState = uiState,
        query = query,
        selectedGenre = selectedGenre,
        genres = viewModel.genres,
        imageBaseUrl = imageBaseUrl,
        onAction = viewModel::onAction,
        modifier = modifier,
        contentPadding = contentPadding,
    )
}

// ─── Stateless content ────────────────────────────────────────────────────────

@Composable
fun SearchContent(
    uiState: SearchUiState,
    query: String,
    selectedGenre: String,
    genres: List<String>,
    imageBaseUrl: String,
    onAction: (SearchAction) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    val bottomPad = contentPadding.calculateBottomPadding()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CvBg)
            .statusBarsPadding(),
    ) {
        Spacer(modifier = Modifier.height(CineVaultSpacing.sm))

        SearchBar(
            query = query,
            onQueryChange = { onAction(SearchAction.QueryChanged(it)) },
            onClearQuery = { onAction(SearchAction.ClearQuery) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = CineVaultSpacing.lg),
        )

        Spacer(modifier = Modifier.height(CineVaultSpacing.md))

        GenreFilterRow(
            genres = genres,
            selectedGenre = selectedGenre,
            onGenreSelected = { onAction(SearchAction.GenreSelected(it)) },
        )

        when (uiState) {
            SearchUiState.Idle -> IdleContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = bottomPad),
            )
            is SearchUiState.Loading -> LoadingGrid(
                contentPadding = PaddingValues(
                    start = CineVaultSpacing.lg,
                    end = CineVaultSpacing.lg,
                    top = CineVaultSpacing.lg,
                    bottom = bottomPad + CineVaultSpacing.lg,
                ),
                modifier = Modifier.fillMaxSize(),
            )
            is SearchUiState.Success -> if (uiState.results.isEmpty()) {
                EmptyContent(
                    onBrowseGenres = { onAction(SearchAction.ClearQuery) },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = bottomPad),
                )
            } else {
                ResultsGrid(
                    results = uiState.results,
                    imageBaseUrl = imageBaseUrl,
                    onMovieClick = { onAction(SearchAction.MovieClicked(it)) },
                    contentPadding = PaddingValues(
                        start = CineVaultSpacing.lg,
                        end = CineVaultSpacing.lg,
                        top = CineVaultSpacing.lg,
                        bottom = bottomPad + CineVaultSpacing.lg,
                    ),
                    modifier = Modifier.fillMaxSize(),
                )
            }
            is SearchUiState.Error -> ErrorContent(
                message = uiState.message,
                onRetry = { onAction(SearchAction.QueryChanged(uiState.query)) },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = CineVaultSpacing.lg,
                        vertical = CineVaultSpacing.lg,
                    )
                    .padding(bottom = bottomPad),
            )
        }
    }
}

// ─── Search bar ───────────────────────────────────────────────────────────────

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClearQuery: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(CineVaultSpacing.sm),
        modifier = modifier
            .clip(RoundedCornerShape(CineVaultRadius.pill))
            .background(CvSurface)
            .border(
                width = 1.dp,
                color = CvBorder,
                shape = RoundedCornerShape(CineVaultRadius.pill),
            )
            .padding(horizontal = 14.dp, vertical = 10.dp),
    ) {
        Icon(
            imageVector = search_image,
            contentDescription = null,
            tint = CvTextDim,
            modifier = Modifier.size(18.dp),
        )
        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.weight(1f),
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = CvText,
                fontSize = 14.sp,
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            cursorBrush = SolidColor(CvAmber),
            decorationBox = { innerTextField ->
                Box {
                    if (query.isEmpty()) {
                        Text(
                            text = "Search movies, actors, directors…",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = CvTextMute,
                                fontSize = 14.sp,
                            ),
                        )
                    }
                    innerTextField()
                }
            },
        )
        if (query.isNotEmpty()) {
            IconButton(
                onClick = onClearQuery,
                modifier = Modifier.size(20.dp),
            ) {
                Icon(
                    imageVector = close_image,
                    contentDescription = "Clear",
                    tint = CvTextDim,
                    modifier = Modifier.size(16.dp),
                )
            }
        }
    }
}

// ─── Genre filter row ─────────────────────────────────────────────────────────

@Composable
private fun GenreFilterRow(
    genres: List<String>,
    selectedGenre: String,
    onGenreSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = CineVaultSpacing.lg)
            .padding(bottom = CineVaultSpacing.xs),
        horizontalArrangement = Arrangement.spacedBy(CineVaultSpacing.sm),
    ) {
        genres.forEach { genre ->
            GenreChip(
                label = genre,
                selected = genre == selectedGenre,
                onClick = { onGenreSelected(genre) },
            )
        }
    }
}

// ─── Results grid ─────────────────────────────────────────────────────────────

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun ResultsGrid(
    results: List<Movie>,
    imageBaseUrl: String,
    onMovieClick: (Int) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    val sharedTransitionScope = LocalSharedTransitionScope.current
    val animatedContentScope = LocalNavAnimatedContentScope.current as? AnimatedVisibilityScope

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(14.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        modifier = modifier,
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Text(
                text = "${results.size} RESULTS · SORTED BY RELEVANCE",
                style = MaterialTheme.typography.labelMedium.copy(
                    color = CvTextDim,
                    fontFamily = JetBrainsMonoFontFamily,
                    fontSize = 11.sp,
                ),
                modifier = Modifier.padding(bottom = CineVaultSpacing.xs),
            )
        }

        items(results, key = { it.id }) { movie ->
            val posterModifier = if (sharedTransitionScope != null && animatedContentScope != null) {
                with(sharedTransitionScope) {
                    Modifier.sharedElement(
                        sharedContentState = rememberSharedContentState(key = "poster_${movie.id}"),
                        animatedVisibilityScope = animatedContentScope,
                        boundsTransform = { _, _ -> CineVaultAnimation.sharedElementSpec() },
                    )
                }
            } else {
                Modifier
            }

            MovieCard(
                title = movie.title,
                posterUrl = movie.posterUrl(imageBaseUrl),
                rating = movie.voteAverage.toFloat(),
                year = movie.releaseYear().toIntOrNull() ?: 0,
                genre = movie.primaryGenre(),
                isAvailableOffline = movie.isAvailableOffline,
                onClick = { onMovieClick(movie.id) },
                posterModifier = posterModifier,
            )
        }
    }
}

// ─── Loading grid (shimmer) ───────────────────────────────────────────────────

@Composable
private fun LoadingGrid(
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(14.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        userScrollEnabled = false,
        modifier = modifier,
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.45f)
                    .height(12.dp)
                    .shimmerEffect(CineVaultRadius.sm),
            )
        }
        items(GRID_SHIMMER_COUNT) {
            MovieCardShimmer()
        }
    }
}

// ─── Idle (no query) ──────────────────────────────────────────────────────────

@Composable
private fun IdleContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(CineVaultSpacing.sm),
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(CvSurface),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = search_image,
                    contentDescription = null,
                    tint = CvTextMute,
                    modifier = Modifier.size(36.dp),
                )
            }
            Spacer(modifier = Modifier.height(CineVaultSpacing.sm))
            Text(
                text = "Find your next film",
                style = MaterialTheme.typography.headlineSmall.copy(
                    color = CvText,
                    fontFamily = PlayfairDisplayFontFamily,
                ),
            )
            Text(
                text = "Search movies, actors, directors",
                style = MaterialTheme.typography.bodyMedium.copy(color = CvTextDim),
            )
        }
    }
}

// ─── Empty (no results) ───────────────────────────────────────────────────────

@Composable
private fun EmptyContent(
    onBrowseGenres: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.padding(top = 60.dp),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Film circle with search badge
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(CvSurface)
                    .border(width = 1.dp, color = CvBorder, shape = CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = play_arrow_image,
                    contentDescription = null,
                    tint = CvTextMute,
                    modifier = Modifier.size(48.dp),
                )
                // Search badge — bottom right
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .align(Alignment.BottomEnd)
                        .padding(4.dp)
                        .clip(CircleShape)
                        .background(CvBg)
                        .border(width = 2.dp, color = CvSurface, shape = CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = search_image,
                        contentDescription = null,
                        tint = CvTextDim,
                        modifier = Modifier.size(16.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.height(CineVaultSpacing.xl))

            Text(
                text = "No matches found",
                style = MaterialTheme.typography.headlineSmall.copy(
                    color = CvText,
                    fontFamily = PlayfairDisplayFontFamily,
                ),
            )

            Spacer(modifier = Modifier.height(CineVaultSpacing.sm))

            Text(
                text = "Try a different keyword, or browse a\ncategory to discover something new.",
                style = MaterialTheme.typography.bodyMedium.copy(color = CvTextDim),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.padding(horizontal = CineVaultSpacing.xxxl),
            )

            Spacer(modifier = Modifier.height(CineVaultSpacing.xxl))

            Button(
                onClick = onBrowseGenres,
                colors = ButtonDefaults.buttonColors(
                    containerColor = CvSurface,
                    contentColor = CvText,
                ),
                shape = RoundedCornerShape(CineVaultRadius.pill),
            ) {
                Text(
                    text = "Browse genres",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium),
                )
            }
        }
    }
}

// ─── Error ────────────────────────────────────────────────────────────────────

@Composable
private fun ErrorContent(
    message: UiText,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(CineVaultSpacing.lg),
        ) {
            Text(
                text = message.asString(),
                style = MaterialTheme.typography.bodyLarge.copy(color = CvTextDim),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            )
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = CvAmber,
                    contentColor = CvBg,
                ),
                shape = RoundedCornerShape(CineVaultRadius.pill),
            ) {
                Text(
                    text = "Retry",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                )
            }
        }
    }
}

// ─── Previews ─────────────────────────────────────────────────────────────────

private val previewGenres = listOf("All", "Drama", "Sci-Fi", "Action", "Comedy", "Mystery")

private val previewMovies = listOf(
    Movie(
        id = 1, title = "Glass Cathedral", overview = "An architect designs a building that does not exist.",
        posterPath = null, backdropPath = null, releaseDate = "2025-01-15",
        voteAverage = 9.1, voteCount = 2847, popularity = 980.0, adult = false,
        originalLanguage = "en", originalTitle = "Glass Cathedral", genreIds = listOf(878, 53),
    ),
    Movie(
        id = 2, title = "The Lantern Hours", overview = "A clockmaker discovers letters from the future.",
        posterPath = null, backdropPath = null, releaseDate = "2024-06-10",
        voteAverage = 8.7, voteCount = 1203, popularity = 750.0, adult = false,
        originalLanguage = "en", originalTitle = "The Lantern Hours", genreIds = listOf(18, 9648),
    ),
    Movie(
        id = 3, title = "Northwind", overview = "A solo expedition across the frozen Beaufort Sea.",
        posterPath = null, backdropPath = null, releaseDate = "2025-03-20",
        voteAverage = 8.5, voteCount = 986, popularity = 620.0, adult = false,
        originalLanguage = "en", originalTitle = "Northwind", genreIds = listOf(12, 18),
    ),
    Movie(
        id = 4, title = "Crimson Frequency", overview = "A radio host receives transmissions from the past.",
        posterPath = null, backdropPath = null, releaseDate = "2024-09-05",
        voteAverage = 7.8, voteCount = 754, popularity = 430.0, adult = false,
        originalLanguage = "en", originalTitle = "Crimson Frequency", genreIds = listOf(9648, 53),
    ),
)

@Preview(showBackground = true, backgroundColor = 0xFF0A0A0A, name = "Search — Idle")
@Composable
private fun SearchIdlePreview() {
    CineVaultTheme {
        SearchContent(
            uiState = SearchUiState.Idle,
            query = "",
            selectedGenre = "All",
            genres = previewGenres,
            imageBaseUrl = "",
            onAction = {},
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A0A, name = "Search — Loading")
@Composable
private fun SearchLoadingPreview() {
    CineVaultTheme {
        SearchContent(
            uiState = SearchUiState.Loading(query = "Lantern", selectedGenre = "All"),
            query = "Lantern",
            selectedGenre = "All",
            genres = previewGenres,
            imageBaseUrl = "",
            onAction = {},
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A0A, name = "Search — Results")
@Composable
private fun SearchResultsPreview() {
    CineVaultTheme {
        SearchContent(
            uiState = SearchUiState.Success(
                query = "Lantern",
                results = previewMovies,
                selectedGenre = "All",
                isSearching = false,
            ),
            query = "Lantern",
            selectedGenre = "All",
            genres = previewGenres,
            imageBaseUrl = "",
            onAction = {},
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A0A, name = "Search — Empty")
@Composable
private fun SearchEmptyPreview() {
    CineVaultTheme {
        SearchContent(
            uiState = SearchUiState.Success(
                query = "zzqxx",
                results = emptyList(),
                selectedGenre = "All",
                isSearching = false,
            ),
            query = "zzqxx",
            selectedGenre = "All",
            genres = previewGenres,
            imageBaseUrl = "",
            onAction = {},
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A0A, name = "Search — Error")
@Composable
private fun SearchErrorPreview() {
    CineVaultTheme {
        SearchContent(
            uiState = SearchUiState.Error(
                query = "Lantern",
                selectedGenre = "All",
                message = UiText.DynamicString("No internet connection"),
            ),
            query = "Lantern",
            selectedGenre = "All",
            genres = previewGenres,
            imageBaseUrl = "",
            onAction = {},
        )
    }
}
