package com.edu.core.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.edu.core.presentation.designsystem.CineVaultRadius
import com.edu.core.presentation.designsystem.CineVaultSpacing
import com.edu.core.presentation.designsystem.CineVaultTheme
import com.edu.core.presentation.designsystem.CvAmber
import com.edu.core.presentation.designsystem.CvBg
import com.edu.core.presentation.designsystem.CvStar
import com.edu.core.presentation.designsystem.CvText
import com.edu.core.presentation.designsystem.JetBrainsMonoFontFamily
import com.edu.core.presentation.designsystem.icon.search_image
import com.edu.core.presentation.designsystem.icon.star_image


// ---------------------------------------------------------------------------
// RatingBadge
//
// Displays a numeric movie score. Two variants from /design/components.jsx:
//
//   Pill — filled amber pill, dark text on amber, used as poster overlay.
//          spec: background = CvAmber, text = CvBg, font = JetBrains Mono
//
//   Star — inline star + score, no background, used in list rows / reviews.
//          spec: star tint = CvStar (amber), score = CvText, JetBrains Mono
//
// Score is always formatted to one decimal place (e.g. "8.7").
// Font rule: score text always uses JetBrainsMonoFontFamily (design system).
// ---------------------------------------------------------------------------

/** Selects the visual presentation of [RatingBadge]. */
enum class RatingVariant { Pill, Star }

/**
 * Displays a rating score as a pill overlay or an inline star + text.
 *
 * @param rating   Raw score value (e.g. 8.7f). Formatted to one decimal.
 * @param variant  [RatingVariant.Pill] for poster overlays;
 *                 [RatingVariant.Star] for inline usage.
 */
@Composable
fun RatingBadge(
    rating: Float,
    variant: RatingVariant = RatingVariant.Pill,
    modifier: Modifier = Modifier,
) {
    val formatted = "%.1f".format(rating)
    when (variant) {
        RatingVariant.Pill -> PillBadge(score = formatted, modifier = modifier)
        RatingVariant.Star -> StarBadge(score = formatted, modifier = modifier)
    }
}

// ── Pill variant ──────────────────────────────────────────────────────────

@Composable
private fun PillBadge(score: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(CineVaultRadius.pill))
            .background(CvAmber)
            .padding(horizontal = CineVaultSpacing.sm, vertical = 3.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.dp),
    ) {
        Icon(
            imageVector        = star_image,
            contentDescription = null,
            tint               = CvBg,
            modifier           = Modifier.size(10.dp),
        )
        Text(
            text  = score,
            style = MaterialTheme.typography.labelMedium.copy(
                fontFamily = JetBrainsMonoFontFamily,
                color      = CvBg,
            ),
        )
    }
}

// ── Star variant ──────────────────────────────────────────────────────────

@Composable
private fun StarBadge(score: String, modifier: Modifier = Modifier) {
    Row(
        modifier              = modifier,
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Icon(
            imageVector        = star_image,
            contentDescription = null,
            tint               = CvStar,
            modifier           = Modifier.size(14.dp),
        )
        Text(
            text  = score,
            style = MaterialTheme.typography.labelLarge.copy(
                fontFamily = JetBrainsMonoFontFamily,
                color      = CvText,
            ),
        )
    }
}

// ---------------------------------------------------------------------------
// Previews
// ---------------------------------------------------------------------------

@Preview(showBackground = true, backgroundColor = 0xFF141414)
@Composable
private fun RatingBadgePillPreview() {
    CineVaultTheme {
        Column(
            modifier = Modifier.padding(CineVaultSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(CineVaultSpacing.sm),
        ) {
            RatingBadge(rating = 9.1f, variant = RatingVariant.Pill)
            RatingBadge(rating = 7.4f, variant = RatingVariant.Pill)
            RatingBadge(rating = 4.9f, variant = RatingVariant.Pill)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF141414)
@Composable
private fun RatingBadgeStarPreview() {
    CineVaultTheme {
        Column(
            modifier = Modifier.padding(CineVaultSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(CineVaultSpacing.sm),
        ) {
            RatingBadge(rating = 9.1f, variant = RatingVariant.Star)
            RatingBadge(rating = 7.4f, variant = RatingVariant.Star)
        }
    }
}
