package com.edu.feature.home.presentation

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

import com.edu.core.domain.movie.Movie
import com.edu.core.presentation.designsystem.CineVaultAnimation
import com.edu.core.presentation.designsystem.CineVaultRadius
import com.edu.core.presentation.designsystem.CineVaultSpacing
import com.edu.core.presentation.designsystem.CineVaultTheme
import com.edu.core.presentation.designsystem.LocalSharedTransitionScope
import com.edu.core.presentation.designsystem.CvAmber
import com.edu.core.presentation.designsystem.CvBg
import com.edu.core.presentation.designsystem.CvSurface
import com.edu.core.presentation.designsystem.CvText
import com.edu.core.presentation.designsystem.CvTextDim
import com.edu.core.presentation.designsystem.CvTextMute
import com.edu.core.presentation.designsystem.JetBrainsMonoFontFamily
import com.edu.core.presentation.designsystem.PlayfairDisplayFontFamily
import com.edu.core.presentation.designsystem.components.CineVaultLogo
import com.edu.core.presentation.designsystem.components.MovieCard
import com.edu.core.presentation.designsystem.components.MovieCardShimmer
import com.edu.core.presentation.designsystem.components.RatingBadge
import com.edu.core.presentation.designsystem.components.RatingVariant
import com.edu.core.presentation.designsystem.components.SectionHeader
import com.edu.core.presentation.designsystem.components.SyncState
import com.edu.core.presentation.designsystem.components.SyncStatusIndicator
import com.edu.core.presentation.designsystem.components.shimmerEffect
import com.edu.core.presentation.designsystem.icon.search_image
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import com.edu.core.presentation.ui.ObserveAsEvent
import com.edu.core.presentation.ui.UiText
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

private const val HERO_COUNT = 5
private const val SHIMMER_CARD_COUNT = 5
private val CARD_WIDTH = 124.dp
private val HERO_HEIGHT = 420.dp

// ─── Genre ID → name map (TMDB standard IDs) ─────────────────────────────────
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

private fun Movie.backdropUrl(imageBaseUrl: String) =
    if (!backdropPath.isNullOrEmpty()) "${imageBaseUrl}w780$backdropPath" else ""

// ─── Stateful screen ──────────────────────────────────────────────────────────

@Composable
fun HomeScreen(
    onNavigateToDetail: (Int) -> Unit,
    onNavigateToSearch: () -> Unit,
    onRequireAuth: () -> Unit,
    imageBaseUrl: String,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    viewModel: HomeViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ObserveAsEvent(viewModel.uiEffect) { effect ->
        when (effect) {
            is HomeEffect.NavigateToDetail -> onNavigateToDetail(effect.movieId)
            HomeEffect.NavigateToSearch -> onNavigateToSearch()
        }
    }

    HomeContent(
        uiState = uiState,
        imageBaseUrl = imageBaseUrl,
        onAction = viewModel::onAction,
        modifier = modifier,
        contentPadding = contentPadding,
    )
}

// ─── Stateless content ────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    uiState: HomeUiState,
    imageBaseUrl: String,
    onAction: (HomeAction) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CvBg),
    ) {
        CenterAlignedTopAppBar(
            title = {},
            navigationIcon = {
                CineVaultLogo(modifier = Modifier.padding(start = CineVaultSpacing.md))
            },
            actions = {
                IconButton(onClick = { onAction(HomeAction.SearchClicked) }) {
                    Icon(
                        imageVector = search_image,
                        contentDescription = "Search",
                        tint = CvText,
                    )
                }
                val syncState = if (uiState is HomeUiState.Success) uiState.syncState
                else SyncState.Idle
                SyncStatusIndicator(
                    state = syncState,
                    modifier = Modifier.padding(end = CineVaultSpacing.sm),
                )
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = CvBg,
                scrolledContainerColor = CvBg,
            ),
        )

        when (uiState) {
            HomeUiState.Loading -> LoadingContent(contentPadding)
            is HomeUiState.Success -> SuccessContent(uiState, imageBaseUrl, onAction, contentPadding)
            is HomeUiState.Error -> ErrorContent(uiState.message, onAction, contentPadding)
        }
    }
}

// ─── Loading ──────────────────────────────────────────────────────────────────

@Composable
private fun LoadingContent(contentPadding: PaddingValues) {
    LazyColumn(
        contentPadding = PaddingValues(bottom = contentPadding.calculateBottomPadding()),
    ) {
        item { HeroShimmer() }
        item {
            Spacer(modifier = Modifier.height(CineVaultSpacing.xxl))
            ShimmerSection()
        }
        item {
            Spacer(modifier = Modifier.height(CineVaultSpacing.xxl))
            ShimmerSection()
        }
    }
}

@Composable
private fun HeroShimmer() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(HERO_HEIGHT)
            .shimmerEffect(cornerRadius = 0.dp),
    )
}

@Composable
private fun ShimmerSection() {
    Column {
        // Header placeholder
        Box(
            modifier = Modifier
                .padding(horizontal = CineVaultSpacing.lg)
                .width(120.dp)
                .height(18.dp)
                .shimmerEffect(CineVaultRadius.sm),
        )
        Spacer(modifier = Modifier.height(CineVaultSpacing.md))
        LazyRow(
            contentPadding = PaddingValues(horizontal = CineVaultSpacing.lg),
            horizontalArrangement = Arrangement.spacedBy(CineVaultSpacing.md),
        ) {
            items(SHIMMER_CARD_COUNT) {
                MovieCardShimmer(modifier = Modifier.width(CARD_WIDTH))
            }
        }
    }
}

// ─── Success ──────────────────────────────────────────────────────────────────

@Composable
private fun SuccessContent(
    state: HomeUiState.Success,
    imageBaseUrl: String,
    onAction: (HomeAction) -> Unit,
    contentPadding: PaddingValues,
) {
    val heroMovies = state.trendingMovies.take(HERO_COUNT)

    LazyColumn(
        contentPadding = PaddingValues(bottom = contentPadding.calculateBottomPadding()),
    ) {
        item {
            if (heroMovies.isEmpty()) {
                HeroShimmer()
            } else {
                HeroCarousel(
                    movies = heroMovies,
                    imageBaseUrl = imageBaseUrl,
                    onMovieClick = { onAction(HomeAction.MovieClicked(it)) },
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(CineVaultSpacing.xxl))
            MovieSection(
                title = "Trending Now",
                movies = state.trendingMovies,
                imageBaseUrl = imageBaseUrl,
                onMovieClick = { onAction(HomeAction.MovieClicked(it)) },
            )
        }

        item {
            Spacer(modifier = Modifier.height(CineVaultSpacing.xxl))
            MovieSection(
                title = "Top Rated",
                movies = state.topRatedMovies,
                imageBaseUrl = imageBaseUrl,
                onMovieClick = { onAction(HomeAction.MovieClicked(it)) },
            )
        }

        item { Spacer(modifier = Modifier.height(CineVaultSpacing.xl)) }
    }
}

// ─── Hero carousel ────────────────────────────────────────────────────────────

@Composable
private fun HeroCarousel(
    movies: List<Movie>,
    imageBaseUrl: String,
    onMovieClick: (Int) -> Unit,
) {
    val pagerState = rememberPagerState(pageCount = { movies.size })

    LaunchedEffect(pagerState.pageCount) {
        if (pagerState.pageCount == 0) return@LaunchedEffect
        while (true) {
            delay(CineVaultAnimation.CAROUSEL_INTERVAL_MS.toLong())
            val next = (pagerState.currentPage + 1) % pagerState.pageCount
            pagerState.animateScrollToPage(
                page = next,
                animationSpec = CineVaultAnimation.screenTransitionSpec<Float>(),
            )
        }
    }

    Box {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
        ) { page ->
            HeroItem(
                movie = movies[page],
                imageBaseUrl = imageBaseUrl,
                onClick = { onMovieClick(movies[page].id) },
            )
        }

        // Page dots
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = CineVaultSpacing.lg),
            horizontalArrangement = Arrangement.spacedBy(CineVaultSpacing.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            repeat(movies.size) { index ->
                val isActive = pagerState.currentPage == index
                val dotW by animateDpAsState(
                    targetValue = if (isActive) 20.dp else 6.dp,
                    animationSpec = tween(CineVaultAnimation.DURATION_SHORT),
                    label = "dotW$index",
                )
                val dotColor by animateColorAsState(
                    targetValue = if (isActive) CvAmber else Color.White.copy(alpha = 0.3f),
                    animationSpec = tween(CineVaultAnimation.DURATION_SHORT),
                    label = "dotColor$index",
                )
                Box(
                    modifier = Modifier
                        .height(6.dp)
                        .width(dotW)
                        .clip(RoundedCornerShape(3.dp))
                        .background(dotColor),
                )
            }
        }
    }
}

@Composable
private fun HeroItem(
    movie: Movie,
    imageBaseUrl: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(HERO_HEIGHT)
            .clickable(onClick = onClick),
    ) {
        // Backdrop image
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(movie.backdropUrl(imageBaseUrl).ifEmpty { movie.posterUrl(imageBaseUrl) })
                .crossfade(CineVaultAnimation.DURATION_MEDIUM)
                .build(),
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            loading = { Box(modifier = Modifier.fillMaxSize().shimmerEffect(cornerRadius = 0.dp)) },
            error = { Box(modifier = Modifier.fillMaxSize().background(CvSurface)) },
        )

        // Cinematic gradient overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    drawHeroGradient()
                },
        )

        // Content
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(CineVaultSpacing.lg)
                .padding(bottom = CineVaultSpacing.xxxl),
        ) {
            Text(
                text = "★  FEATURED THIS WEEK",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = CvAmber,
                    fontFamily = JetBrainsMonoFontFamily,
                    letterSpacing = 1.5.sp,
                ),
            )

            Spacer(modifier = Modifier.height(CineVaultSpacing.sm))

            Text(
                text = movie.title,
                style = MaterialTheme.typography.displaySmall.copy(
                    color = Color.White,
                    fontFamily = PlayfairDisplayFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 38.sp,
                    letterSpacing = (-0.5).sp,
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(modifier = Modifier.height(CineVaultSpacing.md))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(CineVaultSpacing.sm),
            ) {
                RatingBadge(
                    rating = movie.voteAverage.toFloat(),
                    variant = RatingVariant.Pill,
                )
                MetaDot()
                Text(
                    text = movie.releaseYear(),
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = CvTextDim,
                        fontFamily = JetBrainsMonoFontFamily,
                    ),
                )
                val genre = movie.primaryGenre()
                if (genre.isNotEmpty()) {
                    MetaDot()
                    Text(
                        text = genre,
                        style = MaterialTheme.typography.labelMedium.copy(color = CvTextDim),
                    )
                }
            }
        }
    }
}

private fun DrawScope.drawHeroGradient() {
    drawRect(
        brush = Brush.verticalGradient(
            0f to Color(0x66_0A0A0A),
            0.3f to Color.Transparent,
            0.55f to Color.Transparent,
            1f to Color(0xFF_0A0A0A),
        ),
    )
}

@Composable
private fun MetaDot() {
    Box(
        modifier = Modifier
            .size(3.dp)
            .clip(CircleShape)
            .background(CvTextMute),
    )
}

// ─── Movie section (header + row) ─────────────────────────────────────────────

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun MovieSection(
    title: String,
    movies: List<Movie>,
    imageBaseUrl: String,
    onMovieClick: (Int) -> Unit,
) {
    val sharedTransitionScope = LocalSharedTransitionScope.current
    // LocalNavAnimatedContentScope defaults to null outside NavDisplay entries (e.g. previews)
    val animatedContentScope = LocalNavAnimatedContentScope.current as? AnimatedVisibilityScope

    Column {
        SectionHeader(title = title)

        Spacer(modifier = Modifier.height(CineVaultSpacing.md))

        if (movies.isEmpty()) {
            LazyRow(
                contentPadding = PaddingValues(horizontal = CineVaultSpacing.lg),
                horizontalArrangement = Arrangement.spacedBy(CineVaultSpacing.md),
            ) {
                items(SHIMMER_CARD_COUNT) {
                    MovieCardShimmer(modifier = Modifier.width(CARD_WIDTH))
                }
            }
        } else {
            LazyRow(
                contentPadding = PaddingValues(horizontal = CineVaultSpacing.lg),
                horizontalArrangement = Arrangement.spacedBy(CineVaultSpacing.md),
            ) {
                items(movies, key = { it.id }) { movie ->
                    val posterModifier = if (sharedTransitionScope != null && animatedContentScope != null) {
                        with(sharedTransitionScope) {
                            Modifier.sharedElement(
                                sharedContentState = rememberSharedContentState(key = "poster_${movie.id}"),
                                animatedVisibilityScope = animatedContentScope,
                                boundsTransform = { _, _ -> CineVaultAnimation.sharedElementSpec() },
                            )
                        }
                    } else Modifier

                    MovieCard(
                        title = movie.title,
                        posterUrl = movie.posterUrl(imageBaseUrl),
                        rating = movie.voteAverage.toFloat(),
                        year = movie.releaseYear().toIntOrNull() ?: 0,
                        genre = movie.primaryGenre(),
                        isAvailableOffline = movie.isAvailableOffline,
                        onClick = { onMovieClick(movie.id) },
                        modifier = Modifier.width(CARD_WIDTH),
                        posterModifier = posterModifier,
                    )
                }
            }
        }
    }
}

// ─── Error ────────────────────────────────────────────────────────────────────

@Composable
private fun ErrorContent(
    message: UiText,
    onAction: (HomeAction) -> Unit,
    contentPadding: PaddingValues,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(CineVaultSpacing.lg),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(CineVaultSpacing.lg),
        ) {
            Text(
                text = message.asString(),
                style = MaterialTheme.typography.bodyLarge.copy(color = CvTextDim),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )
            Button(
                onClick = { onAction(HomeAction.Retry) },
                colors = ButtonDefaults.buttonColors(containerColor = CvAmber, contentColor = CvBg),
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

private val previewMovies = listOf(
    Movie(
        id = 1, title = "Glass Cathedral", overview = "An architect designs a building that doesn't exist.",
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
)

@Preview(showBackground = true, backgroundColor = 0xFF0A0A0A, name = "Home — Loading")
@Composable
private fun HomeLoadingPreview() {
    CineVaultTheme {
        HomeContent(
            uiState = HomeUiState.Loading,
            imageBaseUrl = "",
            onAction = {},
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A0A, name = "Home — Success (empty, syncing)")
@Composable
private fun HomeSuccessEmptyPreview() {
    CineVaultTheme {
        HomeContent(
            uiState = HomeUiState.Success(
                trendingMovies = emptyList(),
                topRatedMovies = emptyList(),
                syncState = SyncState.Syncing,
            ),
            imageBaseUrl = "",
            onAction = {},
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A0A, name = "Home — Success")
@Composable
private fun HomeSuccessPreview() {
    CineVaultTheme {
        HomeContent(
            uiState = HomeUiState.Success(
                trendingMovies = previewMovies,
                topRatedMovies = previewMovies.sortedByDescending { it.voteAverage },
                syncState = SyncState.Idle,
            ),
            imageBaseUrl = "",
            onAction = {},
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A0A, name = "Home — Error")
@Composable
private fun HomeErrorPreview() {
    CineVaultTheme {
        HomeContent(
            uiState = HomeUiState.Error(UiText.DynamicString("No internet connection")),
            imageBaseUrl = "",
            onAction = {},
        )
    }
}
