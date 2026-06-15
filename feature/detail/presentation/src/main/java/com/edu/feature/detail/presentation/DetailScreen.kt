package com.edu.feature.detail.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import com.edu.core.presentation.designsystem.LocalSharedTransitionScope
import android.content.Intent
import android.net.Uri
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.edu.core.domain.movie.Actor
import com.edu.core.presentation.designsystem.CineVaultAnimation
import com.edu.core.presentation.designsystem.CineVaultRadius
import com.edu.core.presentation.designsystem.CineVaultSpacing
import com.edu.core.presentation.designsystem.CineVaultTheme
import com.edu.core.presentation.designsystem.CvSurface
import com.edu.core.presentation.designsystem.CvSurfaceElev
import com.edu.core.presentation.designsystem.CvText
import com.edu.core.presentation.designsystem.CvTextDim
import com.edu.core.presentation.designsystem.CvTextMute
import com.edu.core.presentation.designsystem.components.MovieCard
import com.edu.core.presentation.designsystem.components.MovieCardShimmer
import com.edu.core.presentation.designsystem.components.RatingBadge
import com.edu.core.presentation.designsystem.components.SectionHeader
import com.edu.core.presentation.designsystem.components.shimmerEffect
import com.edu.core.presentation.designsystem.icon.arrow_back_image
import com.edu.core.presentation.designsystem.icon.bookmark_border_image
import com.edu.core.presentation.designsystem.icon.bookmark_filled_image
import com.edu.core.presentation.designsystem.icon.play_arrow_image
import com.edu.core.presentation.ui.ObserveAsEvent
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

private val BackdropHeight = 320.dp
private val ContentOverlap = 90.dp

@Composable
fun DetailScreen(
    movieId: Int,
    imageBaseUrl: String,
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (Int) -> Unit,
    onRequireAuth: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = koinViewModel(key = "detail_$movieId", parameters = { parametersOf(movieId) }),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val pendingSnackbar = remember { mutableStateOf<String?>(null) }
    var trailerVideoId by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(pendingSnackbar.value) {
        pendingSnackbar.value?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            pendingSnackbar.value = null
        }
    }

    ObserveAsEvent(viewModel.uiEffect) { effect ->
        when (effect) {
            DetailEffect.NavigateBack -> onNavigateBack()
            is DetailEffect.NavigateToDetail -> onNavigateToDetail(effect.movieId)
            DetailEffect.RequireAuth -> onRequireAuth()
            is DetailEffect.ShowError -> pendingSnackbar.value = effect.message.asString(context)
            is DetailEffect.PlayTrailer -> trailerVideoId = effect.youtubeKey
        }
    }

    Box(modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        when (val state = uiState) {
            is DetailUiState.Loading -> DetailLoadingContent(
                onBack = { viewModel.onAction(DetailAction.Back) },
            )
            is DetailUiState.Success -> DetailSuccessContent(
                state = state,
                imageBaseUrl = imageBaseUrl,
                onBack = { viewModel.onAction(DetailAction.Back) },
                onWatchTrailer = { viewModel.onAction(DetailAction.WatchTrailer) },
                onWatchlist = { viewModel.onAction(DetailAction.ToggleWatchlist) },
                onSimilarMovieClick = { id -> viewModel.onAction(DetailAction.SimilarMovieClicked(id)) },
            )
            is DetailUiState.Error -> DetailErrorContent(
                message = state.message.asString(context),
                onBack = { viewModel.onAction(DetailAction.Back) },
            )
        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter),
        )

        // Trailer overlay — rendered inside the Activity window, not a Dialog window,
        // so YouTubePlayerView gets the correct window attachment
        trailerVideoId?.let { videoId ->
            YouTubePlayerOverlay(
                videoId = videoId,
                onDismiss = { trailerVideoId = null },
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

// ─── Success layout ────────────────────────────────────────────────────────────

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun DetailSuccessContent(
    state: DetailUiState.Success,
    imageBaseUrl: String,
    onBack: () -> Unit,
    onWatchTrailer: () -> Unit,
    onWatchlist: () -> Unit,
    onSimilarMovieClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val movie = state.movie
    val backdropUrl = movie.backdropPath?.let { "$imageBaseUrl/w780$it" }
    val posterUrl = movie.posterPath?.let { "$imageBaseUrl/w500$it" }
    val year = movie.releaseDate.take(4)
    val runtimeText = if (movie.runtime > 0) "${movie.runtime}m" else ""

    val sharedTransitionScope = LocalSharedTransitionScope.current
    val animatedContentScope = LocalNavAnimatedContentScope.current as? AnimatedVisibilityScope

    Box(modifier = modifier.fillMaxSize()) {
        // ── Scrollable body ──────────────────────────────────────────────────
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            // Space so the backdrop is visible behind the content
            item { Spacer(modifier = Modifier.height(BackdropHeight - ContentOverlap)) }

            // ── Poster + meta row ────────────────────────────────────────────
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = CineVaultSpacing.lg),
                        verticalAlignment = Alignment.Bottom,
                    ) {
                        val posterSharedModifier = if (sharedTransitionScope != null && animatedContentScope != null) {
                            with(sharedTransitionScope) {
                                Modifier.sharedElement(
                                    sharedContentState = rememberSharedContentState(key = "poster_${movie.id}"),
                                    animatedVisibilityScope = animatedContentScope,
                                    boundsTransform = { _, _ -> CineVaultAnimation.sharedElementSpec() },
                                )
                            }
                        } else Modifier
                        PosterImage(
                            url = posterUrl,
                            modifier = posterSharedModifier
                                .width(110.dp)
                                .height(165.dp)
                                .clip(RoundedCornerShape(CineVaultRadius.md)),
                        )
                        Spacer(modifier = Modifier.width(CineVaultSpacing.md))
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(bottom = CineVaultSpacing.xs),
                        ) {
                            Text(
                                text = movie.title,
                                style = MaterialTheme.typography.headlineSmall,
                                color = CvText,
                                fontWeight = FontWeight.Bold,
                                lineHeight = 28.sp,
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis,
                            )
                            Spacer(modifier = Modifier.height(CineVaultSpacing.xs))
                            val subtitle = listOfNotNull(
                                year.takeIf { it.isNotEmpty() },
                                runtimeText.takeIf { it.isNotEmpty() },
                            ).joinToString(" · ")
                            if (subtitle.isNotEmpty()) {
                                Text(
                                    text = subtitle,
                                    style = MaterialTheme.typography.labelLarge,
                                    color = CvTextDim,
                                )
                                Spacer(modifier = Modifier.height(CineVaultSpacing.sm))
                            }
                            RatingBadge(rating = movie.voteAverage.toFloat())
                        }
                    }

                    // ── Genre chips ──────────────────────────────────────────
                    if (movie.genreIds.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(CineVaultSpacing.md))
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = CineVaultSpacing.lg),
                            horizontalArrangement = Arrangement.spacedBy(CineVaultSpacing.sm),
                        ) {
                            items(movie.genreIds.take(5)) { genreId ->
                                GenreLabel(genreId)
                            }
                        }
                    }

                    // ── CTA row ──────────────────────────────────────────────
                    Spacer(modifier = Modifier.height(CineVaultSpacing.md))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = CineVaultSpacing.lg),
                        horizontalArrangement = Arrangement.spacedBy(CineVaultSpacing.sm),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Button(
                            onClick = onWatchTrailer,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                            ),
                        ) {
                            Icon(
                                imageVector = play_arrow_image,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                            )
                            Spacer(modifier = Modifier.width(CineVaultSpacing.xs))
                            Text(text = "Watch Trailer")
                        }
                        OutlinedIconButton(onClick = onWatchlist) {
                            Icon(
                                imageVector = if (state.isInWatchlist) bookmark_filled_image
                                              else bookmark_border_image,
                                contentDescription = if (state.isInWatchlist) "Remove from watchlist"
                                                     else "Add to watchlist",
                                tint = if (state.isInWatchlist) MaterialTheme.colorScheme.primary
                                       else MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    }

                    // ── Synopsis ─────────────────────────────────────────────
                    if (movie.overview.isNotBlank()) {
                        Spacer(modifier = Modifier.height(CineVaultSpacing.xxl))
                        Text(
                            text = "Synopsis",
                            style = MaterialTheme.typography.titleMedium,
                            color = CvText,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = CineVaultSpacing.lg),
                        )
                        Spacer(modifier = Modifier.height(CineVaultSpacing.sm))
                        Text(
                            text = movie.overview,
                            style = MaterialTheme.typography.bodyMedium,
                            color = CvTextDim,
                            modifier = Modifier.padding(horizontal = CineVaultSpacing.lg),
                        )
                    }

                    // ── Cast section ─────────────────────────────────────────
                    Spacer(modifier = Modifier.height(CineVaultSpacing.xxl))
                    SectionHeader(title = "Cast", onSeeAll = null)
                    Spacer(modifier = Modifier.height(CineVaultSpacing.md))
                    if (state.isCastLoading) {
                        CastShimmerRow()
                    } else {
                        CastRow(cast = state.cast, imageBaseUrl = imageBaseUrl)
                    }

                    // ── Similar movies ────────────────────────────────────────
                    if (state.similarMovies.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(CineVaultSpacing.xxl))
                        SectionHeader(title = "More like this", onSeeAll = null)
                        Spacer(modifier = Modifier.height(CineVaultSpacing.md))
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = CineVaultSpacing.lg),
                            horizontalArrangement = Arrangement.spacedBy(CineVaultSpacing.md),
                        ) {
                            items(state.similarMovies, key = { it.id }) { similar ->
                                MovieCard(
                                    title = similar.title,
                                    posterUrl = similar.posterPath?.let { "$imageBaseUrl/w500$it" } ?: "",
                                    rating = similar.voteAverage.toFloat(),
                                    year = similar.releaseDate.take(4).toIntOrNull() ?: 0,
                                    genre = similar.genreIds.firstOrNull()?.let { genreIdToName(it) } ?: "",
                                    onClick = { onSimilarMovieClick(similar.id) },
                                    modifier = Modifier.width(108.dp),
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(CineVaultSpacing.xxxl))
                }
            }
        }

        // ── Fixed backdrop (behind scroll) ───────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(BackdropHeight),
        ) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(backdropUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                alignment = Alignment.TopCenter,
                loading = {
                    Box(modifier = Modifier.fillMaxSize().shimmerEffect())
                },
                error = {
                    Box(modifier = Modifier.fillMaxSize().background(CvSurfaceElev))
                },
                modifier = Modifier.fillMaxSize(),
            )
            // Gradient: dark at top (status bar), transparent in middle, full bg at bottom
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colorStops = arrayOf(
                                0.0f to Color(0x66000000),
                                0.35f to Color(0x00000000),
                                0.95f to MaterialTheme.colorScheme.background,
                            ),
                        ),
                    ),
            )
        }

        // ── Floating top bar (always on top) ─────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = CineVaultSpacing.xs),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = arrow_back_image,
                    contentDescription = "Back",
                    tint = Color.White,
                )
            }
        }
    }
}

// ─── Loading skeleton ──────────────────────────────────────────────────────────

@Composable
private fun DetailLoadingContent(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(BackdropHeight - ContentOverlap)
                        .shimmerEffect(),
                )
            }
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(CineVaultSpacing.lg),
                ) {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Box(
                            modifier = Modifier
                                .width(110.dp)
                                .height(165.dp)
                                .shimmerEffect(CineVaultRadius.md),
                        )
                        Spacer(modifier = Modifier.width(CineVaultSpacing.md))
                        Column(modifier = Modifier.weight(1f)) {
                            Box(modifier = Modifier.fillMaxWidth(0.8f).height(24.dp).shimmerEffect(CineVaultRadius.sm))
                            Spacer(modifier = Modifier.height(CineVaultSpacing.sm))
                            Box(modifier = Modifier.fillMaxWidth(0.5f).height(14.dp).shimmerEffect(CineVaultRadius.sm))
                            Spacer(modifier = Modifier.height(CineVaultSpacing.sm))
                            Box(modifier = Modifier.width(56.dp).height(22.dp).shimmerEffect(CineVaultRadius.pill))
                        }
                    }
                    Spacer(modifier = Modifier.height(CineVaultSpacing.xl))
                    repeat(3) {
                        Box(modifier = Modifier.fillMaxWidth().height(14.dp).shimmerEffect(CineVaultRadius.sm))
                        Spacer(modifier = Modifier.height(CineVaultSpacing.sm))
                    }
                    Spacer(modifier = Modifier.height(CineVaultSpacing.xxl))
                    CastShimmerRow()
                    Spacer(modifier = Modifier.height(CineVaultSpacing.xxl))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(CineVaultSpacing.md),
                    ) {
                        items(5) { MovieCardShimmer(modifier = Modifier.width(108.dp)) }
                    }
                }
            }
        }
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .statusBarsPadding()
                .padding(start = CineVaultSpacing.xs),
        ) {
            Icon(
                imageVector = arrow_back_image,
                contentDescription = "Back",
                tint = CvText,
            )
        }
    }
}

// ─── Error state ───────────────────────────────────────────────────────────────

@Composable
private fun DetailErrorContent(
    message: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize().padding(CineVaultSpacing.lg),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(text = message, style = MaterialTheme.typography.bodyLarge, color = CvTextDim)
        Spacer(modifier = Modifier.height(CineVaultSpacing.lg))
        Button(onClick = onBack) { Text("Go back") }
    }
}

// ─── Cast row ──────────────────────────────────────────────────────────────────

@Composable
private fun CastRow(
    cast: List<Actor>,
    imageBaseUrl: String,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = CineVaultSpacing.lg),
        horizontalArrangement = Arrangement.spacedBy(CineVaultSpacing.md),
    ) {
        items(cast, key = { it.id }) { actor ->
            CastItem(actor = actor, imageBaseUrl = imageBaseUrl)
        }
    }
}

@Composable
private fun CastItem(
    actor: Actor,
    imageBaseUrl: String,
    modifier: Modifier = Modifier,
) {
    val profileUrl = actor.profilePath?.let { "$imageBaseUrl/w500$it" }
    Column(
        modifier = modifier.width(72.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(profileUrl)
                .crossfade(true)
                .build(),
            contentDescription = actor.name,
            contentScale = ContentScale.Crop,
            loading = {
                Box(modifier = Modifier.size(64.dp).shimmerEffect(CineVaultRadius.pill))
            },
            error = {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(CvSurfaceElev),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = actor.name.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString(""),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                    )
                }
            },
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape),
        )
        Spacer(modifier = Modifier.height(CineVaultSpacing.sm))
        Text(
            text = actor.name,
            style = MaterialTheme.typography.labelSmall,
            color = CvText,
            fontWeight = FontWeight.SemiBold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            lineHeight = 14.sp,
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = actor.character,
            style = MaterialTheme.typography.labelSmall,
            color = CvTextMute,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            lineHeight = 13.sp,
            fontSize = 10.sp,
        )
    }
}

@Composable
private fun CastShimmerRow(modifier: Modifier = Modifier) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = CineVaultSpacing.lg),
        horizontalArrangement = Arrangement.spacedBy(CineVaultSpacing.md),
    ) {
        items(6) {
            Column(
                modifier = Modifier.width(72.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(modifier = Modifier.size(64.dp).shimmerEffect(CineVaultRadius.pill))
                Spacer(modifier = Modifier.height(CineVaultSpacing.sm))
                Box(modifier = Modifier.fillMaxWidth(0.9f).height(11.dp).shimmerEffect(CineVaultRadius.sm))
                Spacer(modifier = Modifier.height(4.dp))
                Box(modifier = Modifier.fillMaxWidth(0.7f).height(10.dp).shimmerEffect(CineVaultRadius.sm))
            }
        }
    }
}

// ─── Small helpers ─────────────────────────────────────────────────────────────

@Composable
private fun PosterImage(
    url: String?,
    modifier: Modifier = Modifier,
) {
    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .crossfade(true)
            .build(),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        loading = {
            Box(modifier = Modifier.fillMaxSize().shimmerEffect())
        },
        error = {
            Box(modifier = Modifier.fillMaxSize().background(CvSurfaceElev))
        },
        modifier = modifier,
    )
}

@Composable
private fun GenreLabel(genreId: Int, modifier: Modifier = Modifier) {
    // Genre IDs mapped to human-readable labels for the most common TMDB genres
    val label = genreIdToName(genreId) ?: return
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(CineVaultRadius.pill))
            .background(CvSurface)
            .padding(horizontal = CineVaultSpacing.md, vertical = CineVaultSpacing.xs),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = CvTextDim,
        )
    }
}

private fun genreIdToName(id: Int): String? = when (id) {
    28 -> "Action"
    12 -> "Adventure"
    16 -> "Animation"
    35 -> "Comedy"
    80 -> "Crime"
    99 -> "Documentary"
    18 -> "Drama"
    10751 -> "Family"
    14 -> "Fantasy"
    36 -> "History"
    27 -> "Horror"
    10402 -> "Music"
    9648 -> "Mystery"
    10749 -> "Romance"
    878 -> "Sci-Fi"
    10770 -> "TV Movie"
    53 -> "Thriller"
    10752 -> "War"
    37 -> "Western"
    else -> null
}

// ─── In-app YouTube player ─────────────────────────────────────────────────────

@Composable
private fun YouTubePlayerOverlay(
    videoId: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    var playerView by remember { mutableStateOf<YouTubePlayerView?>(null) }
    var hasError by remember { mutableStateOf(false) }

    DisposableEffect(videoId) {
        onDispose {
            playerView?.release()
            playerView = null
        }
    }

    Box(
        modifier = modifier.background(Color.Black.copy(alpha = 0.95f)),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (hasError) {
                Spacer(modifier = Modifier.height(CineVaultSpacing.xxxl))
                Text(
                    text = "This trailer can't be played in-app.",
                    color = CvTextDim,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Spacer(modifier = Modifier.height(CineVaultSpacing.md))
                Button(
                    onClick = {
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://www.youtube.com/watch?v=$videoId"),
                        )
                        context.startActivity(intent)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF0000),
                        contentColor = Color.White,
                    ),
                ) {
                    Text("Watch on YouTube")
                }
                Spacer(modifier = Modifier.height(CineVaultSpacing.xxxl))
            } else {
                AndroidView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f),
                    factory = { ctx ->
                        YouTubePlayerView(ctx).also { view ->
                            playerView = view
                            lifecycleOwner.lifecycle.addObserver(view)
                            view.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                                override fun onReady(youTubePlayer: YouTubePlayer) {
                                    youTubePlayer.loadVideo(videoId, 0f)
                                }

                                override fun onError(
                                    youTubePlayer: YouTubePlayer,
                                    error: PlayerConstants.PlayerError,
                                ) {
                                    hasError = true
                                }
                            })
                        }
                    },
                )
            }
            Spacer(modifier = Modifier.height(CineVaultSpacing.lg))
            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = CineVaultSpacing.lg),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = CvText,
                ),
            ) {
                Text("Close")
            }
        }
    }
}

// ─── Preview ───────────────────────────────────────────────────────────────────

@Preview(showBackground = true, backgroundColor = 0xFF0A0A0A)
@Composable
private fun DetailScreenLoadingPreview() {
    CineVaultTheme {
        DetailLoadingContent(onBack = {})
    }
}