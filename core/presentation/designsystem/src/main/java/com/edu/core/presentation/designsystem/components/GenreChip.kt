package com.edu.core.presentation.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.edu.core.presentation.designsystem.CineVaultRadius
import com.edu.core.presentation.designsystem.CineVaultSpacing
import com.edu.core.presentation.designsystem.CineVaultTheme
import com.edu.core.presentation.designsystem.CvAmber
import com.edu.core.presentation.designsystem.CvBg
import com.edu.core.presentation.designsystem.CvBorderStrong
import com.edu.core.presentation.designsystem.CvTextDim
import com.edu.core.presentation.designsystem.CvTextMute

// ---------------------------------------------------------------------------
// GenreChip
//
// Pill-shaped genre tag. Maps to /design/components.jsx CVGenreChip.
// Uses Material3 AssistChip styled with CineVault tokens.
//
// Selected   → CvAmber background, matching border, CvBg (dark) label text.
// Unselected → transparent background, CvBorderStrong border, CvTextDim text.
//
// Pill shape: RoundedCornerShape(CineVaultRadius.pill).
// Text: labelLarge style (12 sp, Inter Tight, Medium).
// ---------------------------------------------------------------------------

/**
 * Genre tag chip. Toggles between selected and unselected visual states.
 *
 * @param label    Genre string (e.g. "Drama", "Sci-Fi").
 * @param selected True when this genre is the active filter.
 * @param onClick  Called when the chip is tapped.
 */
@Composable
fun GenreChip(
    label: String,
    selected: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AssistChip(
        onClick  = onClick,
        label    = {
            Text(
                text  = label,
                style = MaterialTheme.typography.labelLarge.copy(
                    color = if (selected) CvBg else CvTextDim,
                ),
            )
        },
        shape  = RoundedCornerShape(CineVaultRadius.pill),
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) CvAmber else CvBorderStrong,
        ),
        colors = AssistChipDefaults.assistChipColors(
            containerColor         = if (selected) CvAmber else Color.Transparent,
            labelColor             = if (selected) CvBg else CvTextDim,
            disabledContainerColor = Color.Transparent,
            disabledLabelColor     = CvTextMute,
        ),
        elevation = AssistChipDefaults.assistChipElevation(elevation = 0.dp),
        modifier  = modifier,
    )
}

// ---------------------------------------------------------------------------
// Previews
// ---------------------------------------------------------------------------

@Preview(showBackground = true, backgroundColor = 0xFF0A0A0A)
@Composable
private fun GenreChipPreview() {
    CineVaultTheme {
        Row(
            modifier = Modifier.padding(CineVaultSpacing.lg),
            horizontalArrangement = Arrangement.spacedBy(CineVaultSpacing.sm),
        ) {
            GenreChip(label = "Drama", selected = true, onClick = {})
            GenreChip(label = "Sci-Fi", selected = false, onClick = {})
            GenreChip(label = "Thriller", selected = false, onClick = {})
            GenreChip(label = "Adventure", selected = false, onClick = {})
        }
    }
}
