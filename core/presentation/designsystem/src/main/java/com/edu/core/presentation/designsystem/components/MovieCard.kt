package com.edu.core.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.edu.core.presentation.designsystem.CineVaultAnimation
import com.edu.core.presentation.designsystem.CineVaultRadius
import com.edu.core.presentation.designsystem.CineVaultSpacing
import com.edu.core.presentation.designsystem.CineVaultTheme
import com.edu.core.presentation.designsystem.CvSurface
import com.edu.core.presentation.designsystem.CvSurfaceElev
import com.edu.core.presentation.designsystem.CvText
import com.edu.core.presentation.designsystem.CvTextDim
import com.edu.core.presentation.designsystem.CvTextMute
import com.edu.core.presentation.designsystem.JetBrainsMonoFontFamily
import com.edu.core.presentation.designsystem.icon.broken_image

// ---------------------------------------------------------------------------
// MovieCard
//
// Standard poster card for horizontal carousels and grid screens.
// Design spec: /design/components.jsx → CVMovieCard
//
// Three poster states handled automatically:
//   Loading → shimmerEffect() sweeps the full 2:3 poster area
//   Success → Coil image with DURATION_MEDIUM crossfade
//   Error   → BrokenImage icon centred on CvSurfaceElev
//
// Overlays:
//   RatingBadge (Pill) — top-start, always visible
//   OfflineBadge       — top-end, only when isAvailableOffline=true
//
// Shared element transitions (MovieCard → DetailScreen):
//   Pass Modifier.sharedElement(...) via posterModifier so the transition
//   scope and key stay in the feature layer, not the design system.
// ---------------------------------------------------------------------------

/**
 * Movie poster card for horizontal carousels and grid screens.
 *
 * Sizes itself to fill available width; control width through [modifier].
 * Standard widths: 124 dp (carousel row), 160 dp (grid cell).
 *
 * @param title               Movie title shown below the poster.
 * @param posterUrl           Full poster image URL (TMDB or equivalent).
 * @param rating              Score 0–10; shown as X.X on the rating badge.
 * @param year                Release year in the metadata line.
 * @param genre               Primary genre label in the metadata line.
 * @param isAvailableOffline  Shows [OfflineBadge] at top-end when true.
 * @param onClick             Invoked on card tap.
 * @param modifier            Controls card width — wrap in `Modifier.width(124.dp)`.
 * @param posterModifier      Applied first on the poster Box; attach
 *                            `Modifier.sharedElement(...)` here for
 *                            shared-element transitions to DetailScreen.
 */
@Composable
fun MovieCard(
    title: String,
    posterUrl: String,
    rating: Float,
    year: Int,
    genre: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isAvailableOffline: Boolean = false,
    posterModifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        // ── Poster ────────────────────────────────────────────────────────────
        Box(
            modifier = posterModifier
                .fillMaxWidth()
                .aspectRatio(2f / 3f)
                .clip(RoundedCornerShape(CineVaultRadius.md))
                .background(CvSurface)
                .clickable(onClick = onClick),
        ) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(posterUrl)
                    .crossfade(CineVaultAnimation.DURATION_MEDIUM)
                    .build(),
                contentDescription = title,
                contentScale       = ContentScale.Crop,
                modifier           = Modifier.fillMaxSize(),
                loading = {
                    Box(modifier = Modifier.fillMaxSize().shimmerEffect())
                },
                error = {
                    Box(
                        modifier         = Modifier.fillMaxSize().background(CvSurfaceElev),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector        = broken_image, // From core-presentation-designsystem-icons
                            contentDescription = null,
                            tint               = CvTextMute,
                            modifier           = Modifier.size(32.dp),
                        )
                    }
                },
            )

            // Rating badge — top-start
            RatingBadge(
                rating   = rating,
                variant  = RatingVariant.Pill,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(CineVaultSpacing.sm),
            )

            // Offline badge — top-end; only when cached for offline use
            if (isAvailableOffline) {
                OfflineBadge(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(CineVaultSpacing.sm),
                )
            }
        }

        // ── Metadata ──────────────────────────────────────────────────────────
        Spacer(modifier = Modifier.height(CineVaultSpacing.sm))
        Text(
            text     = title,
            style    = MaterialTheme.typography.titleMedium.copy(color = CvText),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.height(CineVaultSpacing.xs))
        Text(
            text  = "$year · $genre",
            style = MaterialTheme.typography.bodySmall.copy(
                fontFamily = JetBrainsMonoFontFamily,
                color      = CvTextDim,
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

// ---------------------------------------------------------------------------
// MovieCardShimmer — explicit skeleton for UiState.Loading
//
// Use when the list is loading and no concrete items exist yet. Mirrors
// MovieCard's exact shape so the skeleton → content transition is seamless.
// ---------------------------------------------------------------------------

/**
 * Skeleton placeholder matching [MovieCard] dimensions.
 *
 * Use in a UiState.Loading row alongside the same width modifier as
 * the real cards so the layout does not shift on load.
 */
@Composable
fun MovieCardShimmer(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        // Poster placeholder — matches 2:3 ratio + md corner radius
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f / 3f)
                .shimmerEffect(),
        )
        // Title line placeholder — width ~85 % of card, height matches titleMedium
        Spacer(modifier = Modifier.height(CineVaultSpacing.sm))
        Box(
            modifier = Modifier
                .fillMaxWidth(fraction = 0.85f)
                .height(14.dp)
                .shimmerEffect(CineVaultRadius.sm),
        )
        // Meta line placeholder — width ~60 %, height matches bodySmall
        Spacer(modifier = Modifier.height(CineVaultSpacing.xs))
        Box(
            modifier = Modifier
                .fillMaxWidth(fraction = 0.60f)
                .height(11.dp)
                .shimmerEffect(CineVaultRadius.sm),
        )
    }
}

// ---------------------------------------------------------------------------
// Previews
// ---------------------------------------------------------------------------

@Preview(showBackground = true, backgroundColor = 0xFF0A0A0A, name = "MovieCard — Default")
@Composable
private fun MovieCardPreview() {
    CineVaultTheme {
        Row(
            modifier = Modifier.padding(CineVaultSpacing.lg),
            horizontalArrangement = Arrangement.spacedBy(CineVaultSpacing.md),
        ) {
            MovieCard(
                title = "Dune: Part Two",
                posterUrl = "https://image.tmdb.org/t/p/w342/cdqLnri3NEGcmfnqwk2TSIYtddg.jpg",
                rating = 8.5f,
                year = 2024,
                genre = "Sci-Fi",
                onClick = {},
                modifier = Modifier.width(124.dp),
            )
            MovieCard(
                title = "Oppenheimer",
                posterUrl = "https://image.tmdb.org/t/p/w342/8Gxv8giaFIU9RCnfsHgdifEafN4.jpg",
                rating = 8.9f,
                year = 2023,
                genre = "Drama",
                isAvailableOffline = true,
                onClick = {},
                modifier = Modifier.width(124.dp),
            )
            MovieCard(
                title = "A Very Long Movie Title That Should Ellipsize",
                posterUrl = "",
                rating = 6.2f,
                year = 2024,
                genre = "Thriller",
                onClick = {},
                modifier = Modifier.width(124.dp),
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A0A, name = "MovieCard — Shimmer")
@Composable
private fun MovieCardShimmerPreview() {
    CineVaultTheme {
        Row(
            modifier = Modifier.padding(CineVaultSpacing.lg),
            horizontalArrangement = Arrangement.spacedBy(CineVaultSpacing.md),
        ) {
            MovieCardShimmer(modifier = Modifier.width(124.dp))
            MovieCardShimmer(modifier = Modifier.width(124.dp))
            MovieCardShimmer(modifier = Modifier.width(124.dp))
        }
    }
}