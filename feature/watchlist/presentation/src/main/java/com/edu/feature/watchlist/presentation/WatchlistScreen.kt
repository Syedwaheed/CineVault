package com.edu.feature.watchlist.presentation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.edu.core.domain.movie.Movie
import com.edu.core.presentation.designsystem.CineVaultAnimation
import com.edu.core.presentation.designsystem.CineVaultRadius
import com.edu.core.presentation.designsystem.CineVaultSpacing
import com.edu.core.presentation.designsystem.CineVaultTheme
import com.edu.core.presentation.designsystem.CvAmber
import com.edu.core.presentation.designsystem.CvBg
import com.edu.core.presentation.designsystem.CvBorder
import com.edu.core.presentation.designsystem.CvDanger
import com.edu.core.presentation.designsystem.CvSurface
import com.edu.core.presentation.designsystem.CvSurfaceElev
import com.edu.core.presentation.designsystem.CvText
import com.edu.core.presentation.designsystem.CvTextDim
import com.edu.core.presentation.designsystem.CvTextMute
import com.edu.core.presentation.designsystem.JetBrainsMonoFontFamily
import com.edu.core.presentation.designsystem.PlayfairDisplayFontFamily
import com.edu.core.presentation.designsystem.components.OfflineBadge
import com.edu.core.presentation.designsystem.components.RatingBadge
import com.edu.core.presentation.designsystem.components.RatingVariant
import com.edu.core.presentation.designsystem.components.shimmerEffect
import com.edu.core.presentation.designsystem.icon.bookmark_border_image
import com.edu.core.presentation.ui.ObserveAsEvent
import com.edu.core.presentation.ui.UiText
import org.koin.androidx.compose.koinViewModel

private val PosterWidth = 60.dp
private val PosterHeight = 90.dp
private const val SHIMMER_COUNT = 5

private val tmdbGenreNames = mapOf(
    28 to "Action", 12 to "Adventure", 16 to "Animation", 35 to "Comedy",
    80 to "Crime", 99 to "Documentary", 18 to "Drama", 10751 to "Family",
    14 to "Fantasy", 36 to "History", 27 to "Horror", 10402 to "Music",
    9648 to "Mystery", 10749 to "Romance", 878 to "Sci-Fi", 53 to "Thriller",
    10752 to "War", 37 to "Western",
)

private fun Movie.releaseYear(): String = releaseDate.take(4)
private fun Movie.primaryGenre(): String? =
    genreIds.firstOrNull()?.let { tmdbGenreNames[it] }

// ─── Stateful screen ─────────────────────────────────────────────────────────

@Composable
fun WatchlistScreen(
    imageBaseUrl: String,
    onNavigateToDetail: (Int) -> Unit,
    onBrowseMovies: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    viewModel: WatchlistViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var pendingUndoMovie by remember { mutableStateOf<Movie?>(null) }

    LaunchedEffect(pendingUndoMovie) {
        val movie = pendingUndoMovie ?: return@LaunchedEffect
        val result = snackbarHostState.showSnackbar(
            message = "\"${movie.title}\" removed",
            actionLabel = "UNDO",
            duration = SnackbarDuration.Short,
        )
        if (result == SnackbarResult.ActionPerformed) {
            viewModel.onAction(WatchlistAction.UndoRemove)
        }
        pendingUndoMovie = null
    }

    ObserveAsEvent(viewModel.uiEffect) { effect ->
        when (effect) {
            is WatchlistEffect.NavigateToDetail -> onNavigateToDetail(effect.movieId)
            is WatchlistEffect.ShowUndoSnackbar -> pendingUndoMovie = effect.movie
            WatchlistEffect.BrowseMovies -> onBrowseMovies()
        }
    }

    WatchlistContent(
        uiState = uiState,
        imageBaseUrl = imageBaseUrl,
        snackbarHostState = snackbarHostState,
        onAction = viewModel::onAction,
        modifier = modifier,
        contentPadding = contentPadding,
    )
}

// ─── Stateless content ────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchlistContent(
    uiState: WatchlistUiState,
    imageBaseUrl: String,
    snackbarHostState: SnackbarHostState,
    onAction: (WatchlistAction) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    val movieCount = if (uiState is WatchlistUiState.Success) uiState.movies.size else 0

    Scaffold(
        modifier = modifier,
        containerColor = CvBg,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Watchlist",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontFamily = PlayfairDisplayFontFamily,
                                color = CvText,
                                fontWeight = FontWeight.SemiBold,
                            ),
                        )
                        if (uiState is WatchlistUiState.Success) {
                            Text(
                                text = "${movieCount} title${if (movieCount == 1) "" else "s"}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontFamily = JetBrainsMonoFontFamily,
                                    color = CvTextMute,
                                ),
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CvBg,
                    scrolledContainerColor = CvBg,
                    navigationIconContentColor = Color.Unspecified,
                    titleContentColor = Color.Unspecified,
                    actionIconContentColor = Color.Unspecified
                ),
            )
        },
    ) { innerPadding ->
        val combinedPadding = PaddingValues(
            top = innerPadding.calculateTopPadding(),
            bottom = maxOf(
                innerPadding.calculateBottomPadding(),
                contentPadding.calculateBottomPadding(),
            ),
        )

        when (uiState) {
            WatchlistUiState.Loading -> LoadingContent(combinedPadding)
            WatchlistUiState.Empty -> EmptyContent(
                onBrowseMovies = { onAction(WatchlistAction.BrowseMovies) },
                contentPadding = combinedPadding,
            )
            is WatchlistUiState.Success -> SuccessContent(
                movies = uiState.movies,
                imageBaseUrl = imageBaseUrl,
                onAction = onAction,
                contentPadding = combinedPadding,
            )
            is WatchlistUiState.Error -> ErrorContent(
                message = uiState.message,
                contentPadding = combinedPadding,
            )
        }
    }
}

// ─── Loading ──────────────────────────────────────────────────────────────────

@Composable
private fun LoadingContent(contentPadding: PaddingValues) {
    LazyColumn(contentPadding = contentPadding) {
        items(SHIMMER_COUNT) {
            WatchlistItemShimmer()
        }
    }
}

@Composable
private fun WatchlistItemShimmer(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = CineVaultSpacing.lg, vertical = CineVaultSpacing.md),
        horizontalArrangement = Arrangement.spacedBy(CineVaultSpacing.md),
    ) {
        Box(
            modifier = Modifier
                .width(PosterWidth)
                .height(PosterHeight)
                .shimmerEffect(CineVaultRadius.sm),
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(CineVaultSpacing.sm),
        ) {
            Box(modifier = Modifier.fillMaxWidth(0.8f).height(16.dp).shimmerEffect(CineVaultRadius.sm))
            Box(modifier = Modifier.fillMaxWidth(0.5f).height(12.dp).shimmerEffect(CineVaultRadius.sm))
            Box(modifier = Modifier.fillMaxWidth(0.4f).height(12.dp).shimmerEffect(CineVaultRadius.sm))
            Spacer(modifier = Modifier.height(CineVaultSpacing.xs))
            Box(modifier = Modifier.width(48.dp).height(20.dp).shimmerEffect(CineVaultRadius.pill))
        }
    }
}

// ─── Empty ────────────────────────────────────────────────────────────────────

@Composable
private fun EmptyContent(
    onBrowseMovies: () -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(horizontal = CineVaultSpacing.xxxl),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier.size(132.dp),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = CvAmber.copy(alpha = 0.08f),
                            shape = RoundedCornerShape(66.dp),
                        ),
                )
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(CvSurface, shape = RoundedCornerShape(CineVaultRadius.lg))
                        .clip(RoundedCornerShape(CineVaultRadius.lg))
                        .padding(1.dp)
                        .background(CvSurface, shape = RoundedCornerShape(CineVaultRadius.lg)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = bookmark_border_image,
                        contentDescription = null,
                        tint = CvAmber,
                        modifier = Modifier.size(56.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.height(CineVaultSpacing.xxl))

            Text(
                text = "Your vault is empty",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontFamily = PlayfairDisplayFontFamily,
                    color = CvText,
                    fontWeight = FontWeight.SemiBold,
                ),
            )

            Spacer(modifier = Modifier.height(CineVaultSpacing.sm))

            Text(
                text = "Save movies to watch later. Tap the bookmark icon on any title to add it here.",
                style = MaterialTheme.typography.bodyMedium.copy(color = CvTextDim),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(CineVaultSpacing.xxl))

            Button(
                onClick = onBrowseMovies,
                colors = ButtonDefaults.buttonColors(
                    containerColor = CvAmber,
                    contentColor = CvBg,
                ),
                shape = RoundedCornerShape(CineVaultRadius.pill),
            ) {
                Text(
                    text = "Browse movies",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                )
            }
        }
    }
}

// ─── Success ──────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SuccessContent(
    movies: List<Movie>,
    imageBaseUrl: String,
    onAction: (WatchlistAction) -> Unit,
    contentPadding: PaddingValues,
) {
    LazyColumn(contentPadding = contentPadding) {
        item {
            Text(
                text = "${movies.size} ${if (movies.size == 1) "TITLE" else "TITLES"} · SORTED BY RECENT",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontFamily = JetBrainsMonoFontFamily,
                    color = CvTextMute,
                ),
                modifier = Modifier.padding(
                    horizontal = CineVaultSpacing.lg,
                    vertical = CineVaultSpacing.sm,
                ),
            )
        }

        items(movies, key = { it.id }) { movie ->
            val dismissState = rememberSwipeToDismissBoxState(
                confirmValueChange = { value ->
                    if (value == SwipeToDismissBoxValue.EndToStart) {
                        onAction(WatchlistAction.RemoveMovie(movie))
                        true
                    } else false
                },
                positionalThreshold = { totalDistance -> totalDistance * 0.35f },
            )

            val bgColor by animateColorAsState(
                targetValue = when (dismissState.targetValue) {
                    SwipeToDismissBoxValue.EndToStart -> CvDanger
                    else -> CvSurfaceElev
                },
                animationSpec = tween(CineVaultAnimation.DURATION_SHORT),
                label = "swipeBg",
            )

            SwipeToDismissBox(
                state = dismissState,
                enableDismissFromStartToEnd = false,
                enableDismissFromEndToStart = true,
                backgroundContent = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(bgColor)
                            .padding(end = CineVaultSpacing.xl),
                        contentAlignment = Alignment.CenterEnd,
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Text(
                                text = "✕",
                                style = MaterialTheme.typography.titleLarge.copy(color = Color.White),
                            )
                            Text(
                                text = "REMOVE",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color.White,
                                    fontFamily = JetBrainsMonoFontFamily,
                                ),
                            )
                        }
                    }
                },
            ) {
                WatchlistItemRow(
                    movie = movie,
                    imageBaseUrl = imageBaseUrl,
                    onClick = { onAction(WatchlistAction.MovieClicked(movie.id)) },
                )
            }
        }

        item {
            Text(
                text = "← Swipe left to remove",
                style = MaterialTheme.typography.bodySmall.copy(color = CvTextMute),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(CineVaultSpacing.xl),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            )
        }
    }
}

@Composable
private fun WatchlistItemRow(
    movie: Movie,
    imageBaseUrl: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(CvBg)
            .clickable(onClick = onClick)
            .padding(horizontal = CineVaultSpacing.lg, vertical = CineVaultSpacing.md),
        horizontalArrangement = Arrangement.spacedBy(CineVaultSpacing.md),
        verticalAlignment = Alignment.Top,
    ) {
        Box(
            modifier = Modifier
                .width(PosterWidth)
                .height(PosterHeight)
                .clip(RoundedCornerShape(CineVaultRadius.sm))
                .background(CvSurface),
        ) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(movie.posterPath?.let { "${imageBaseUrl}w185$it" })
                    .crossfade(CineVaultAnimation.DURATION_MEDIUM)
                    .build(),
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                loading = { Box(modifier = Modifier.fillMaxSize().shimmerEffect()) },
                error = { Box(modifier = Modifier.fillMaxSize().background(CvSurfaceElev)) },
            )
            OfflineBadge(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(CineVaultSpacing.xs),
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .height(PosterHeight),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(CineVaultSpacing.xs)) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = CvText,
                        fontWeight = FontWeight.SemiBold,
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )

                val year = movie.releaseYear()
                val genre = movie.primaryGenre()
                val meta = listOfNotNull(
                    year.takeIf { it.isNotEmpty() },
                    genre,
                ).joinToString(" · ")

                if (meta.isNotEmpty()) {
                    Text(
                        text = meta,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontFamily = JetBrainsMonoFontFamily,
                            color = CvTextDim,
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            RatingBadge(
                rating = movie.voteAverage.toFloat(),
                variant = RatingVariant.Pill,
            )
        }

        Box(
            modifier = Modifier.size(CineVaultSpacing.lg),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "›",
                style = MaterialTheme.typography.titleLarge.copy(color = CvTextMute),
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(CvBorder),
    )
}

// ─── Error ────────────────────────────────────────────────────────────────────

@Composable
private fun ErrorContent(
    message: UiText,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(CineVaultSpacing.lg),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = message.asString(),
            style = MaterialTheme.typography.bodyLarge.copy(color = CvTextDim),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
        )
    }
}

// ─── Previews ─────────────────────────────────────────────────────────────────

private val previewMovies = listOf(
    Movie(
        id = 1, title = "Glass Cathedral", overview = "An architect designs a building that doesn't exist.",
        posterPath = null, backdropPath = null, releaseDate = "2025-01-15",
        voteAverage = 9.1, voteCount = 2847, popularity = 980.0, adult = false,
        originalLanguage = "en", originalTitle = "Glass Cathedral",
        genreIds = listOf(878, 53), isAvailableOffline = true,
    ),
    Movie(
        id = 2, title = "The Lantern Hours", overview = "A clockmaker discovers letters from the future.",
        posterPath = null, backdropPath = null, releaseDate = "2024-06-10",
        voteAverage = 8.7, voteCount = 1203, popularity = 750.0, adult = false,
        originalLanguage = "en", originalTitle = "The Lantern Hours",
        genreIds = listOf(18, 9648), isAvailableOffline = true,
    ),
    Movie(
        id = 3, title = "Northwind", overview = "A solo expedition across the frozen Beaufort Sea.",
        posterPath = null, backdropPath = null, releaseDate = "2025-03-20",
        voteAverage = 8.5, voteCount = 986, popularity = 620.0, adult = false,
        originalLanguage = "en", originalTitle = "Northwind",
        genreIds = listOf(12, 18), isAvailableOffline = true,
    ),
)

@Preview(showBackground = true, backgroundColor = 0xFF0A0A0A, name = "Watchlist — Loading")
@Composable
private fun WatchlistLoadingPreview() {
    CineVaultTheme {
        WatchlistContent(
            uiState = WatchlistUiState.Loading,
            imageBaseUrl = "",
            snackbarHostState = remember { SnackbarHostState() },
            onAction = {},
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A0A, name = "Watchlist — Empty")
@Composable
private fun WatchlistEmptyPreview() {
    CineVaultTheme {
        WatchlistContent(
            uiState = WatchlistUiState.Empty,
            imageBaseUrl = "",
            snackbarHostState = remember { SnackbarHostState() },
            onAction = {},
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A0A, name = "Watchlist — Success")
@Composable
private fun WatchlistSuccessPreview() {
    CineVaultTheme {
        WatchlistContent(
            uiState = WatchlistUiState.Success(previewMovies),
            imageBaseUrl = "",
            snackbarHostState = remember { SnackbarHostState() },
            onAction = {},
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A0A, name = "Watchlist — Error")
@Composable
private fun WatchlistErrorPreview() {
    CineVaultTheme {
        WatchlistContent(
            uiState = WatchlistUiState.Error(UiText.DynamicString("Something went wrong")),
            imageBaseUrl = "",
            snackbarHostState = remember { SnackbarHostState() },
            onAction = {},
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A0A, name = "Watchlist — Item Row")
@Composable
private fun WatchlistItemPreview() {
    CineVaultTheme {
        WatchlistItemRow(
            movie = previewMovies.first(),
            imageBaseUrl = "",
            onClick = {},
        )
    }
}
