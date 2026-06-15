package com.edu.core.presentation.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.edu.core.presentation.designsystem.CineVaultSpacing
import com.edu.core.presentation.designsystem.CineVaultTheme
import com.edu.core.presentation.designsystem.CvAmber
import com.edu.core.presentation.designsystem.CvText
import com.edu.core.presentation.designsystem.PlayfairDisplayFontFamily

// ---------------------------------------------------------------------------
// SectionHeader
//
// Row with a cinematic section title and an optional "See all" action button.
// Maps to /design/components.jsx CVSectionHeader.
//
// Title:
//   - Style: headlineMedium (18 sp, SemiBold)
//   - Font:  PlayfairDisplayFontFamily (overrides Inter Tight from the slot)
//   - Color: CvText
//
// "See all" button:
//   - Style: labelLarge (12 sp, Medium, +0.3 sp tracking)
//   - Color: CvAmber (primary)
//   - Pass onSeeAll = null to hide the button entirely.
//
// The header handles its own horizontal padding (CineVaultSpacing.lg = 16 dp)
// so callers do not need to add padding.
// ---------------------------------------------------------------------------

/**
 * Section title row with an optional "See all" action.
 *
 * @param title    Section label (e.g. "Trending This Week").
 * @param onSeeAll Lambda invoked when "See all" is tapped. Pass null to hide.
 */
@Composable
fun SectionHeader(
    title: String,
    onSeeAll: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = CineVaultSpacing.lg),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically,
    ) {
        Text(
            text  = title,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontFamily = PlayfairDisplayFontFamily,
                color      = CvText,
            ),
        )
        if (onSeeAll != null) {
            TextButton(onClick = onSeeAll) {
                Text(
                    text  = "See all",
                    style = MaterialTheme.typography.labelLarge.copy(color = CvAmber),
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Previews
// ---------------------------------------------------------------------------

@Preview(showBackground = true, backgroundColor = 0xFF0A0A0A)
@Composable
private fun SectionHeaderWithActionPreview() {
    CineVaultTheme {
        Column(modifier = Modifier.padding(vertical = CineVaultSpacing.sm)) {
            SectionHeader(title = "Trending This Week", onSeeAll = {})
            SectionHeader(title = "New Releases", onSeeAll = {})
            SectionHeader(title = "Continue Watching", onSeeAll = {})
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A0A)
@Composable
private fun SectionHeaderNoActionPreview() {
    CineVaultTheme {
        SectionHeader(title = "My Watchlist")
    }
}
