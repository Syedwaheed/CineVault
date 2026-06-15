package com.edu.core.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.edu.core.presentation.designsystem.CineVaultRadius
import com.edu.core.presentation.designsystem.CineVaultSpacing
import com.edu.core.presentation.designsystem.CineVaultTheme
import com.edu.core.presentation.designsystem.CvAmber
import com.edu.core.presentation.designsystem.CvBg
import com.edu.core.presentation.designsystem.R

// ---------------------------------------------------------------------------
// OfflineBadge
//
// Amber bookmark icon overlaid on a MovieCard poster when the movie is cached
// for offline viewing (isAvailableOffline = true).
//
// Visual spec: small translucent dark pill at the top-end of the poster.
// Position is controlled by the caller via Modifier.align(TopEnd) + padding.
// (MovieCard positions it at Alignment.TopEnd with CineVaultSpacing.sm padding.)
// ---------------------------------------------------------------------------

/**
 * Amber bookmark overlay badge indicating a movie is available offline.
 *
 * Always place inside a [Box] that clips the poster:
 * ```
 * OfflineBadge(
 *     modifier = Modifier
 *         .align(Alignment.TopEnd)
 *         .padding(CineVaultSpacing.sm),
 * )
 * ```
 */
@Composable
fun OfflineBadge(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(28.dp)
            .clip(RoundedCornerShape(CineVaultRadius.sm))
            .background(CvBg.copy(alpha = 0.72f)),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter            = painterResource(R.drawable.ic_bookmarks_filled),
            contentDescription = "Available offline",
            tint               = CvAmber,
            modifier           = Modifier.size(16.dp),
        )
    }
}

// ---------------------------------------------------------------------------
// Preview
// ---------------------------------------------------------------------------

@Preview(showBackground = true, backgroundColor = 0xFF141414)
@Composable
private fun OfflineBadgePreview() {
    CineVaultTheme {
        Box(modifier = Modifier.padding(CineVaultSpacing.lg)) {
            OfflineBadge()
        }
    }
}
