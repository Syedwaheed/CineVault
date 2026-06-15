package com.edu.core.presentation.designsystem

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

// ---------------------------------------------------------------------------
// CineVault font families — loaded from locally bundled TTF files in res/font/
// Source: /design/tokens.jsx → CV.font
//
// Three families as per the design system:
//   display  → Playfair Display  (cinematic serif)
//   body     → Inter Tight       (UI / body copy)
//   mono     → JetBrains Mono    (ratings, badges, metadata chips)
//
// Note: the JetBrains Mono medium file on disk has a typo in its filename
// ("jetbarains_mono_medium") — the R reference below matches the file exactly.
// ---------------------------------------------------------------------------

/** Cinematic serif: hero titles, display headings, movie poster text. */
val PlayfairDisplayFontFamily = FontFamily(
    Font(resId = R.font.playfair_display_regular,  weight = FontWeight.Normal),
    Font(resId = R.font.playfair_display_medium,   weight = FontWeight.Medium),
    Font(resId = R.font.playfair_display_semibold, weight = FontWeight.SemiBold),
    Font(resId = R.font.playfair_display_bold,     weight = FontWeight.Bold),
)

/** Body copy, UI labels, section headers, all screen text. */
val InterTightFontFamily = FontFamily(
    Font(resId = R.font.inter_tight_regular,  weight = FontWeight.Normal),
    Font(resId = R.font.inter_tight_medium,   weight = FontWeight.Medium),
    Font(resId = R.font.inter_tight_semibold, weight = FontWeight.SemiBold),
    Font(resId = R.font.inter_tight_bold,     weight = FontWeight.Bold),
)

/** Monospace: rating badges, score chips, metadata labels, runtime display. */
val JetBrainsMonoFontFamily = FontFamily(
    Font(resId = R.font.jetbrains_mono_regular,    weight = FontWeight.Normal),
    Font(resId = R.font.jetbarains_mono_medium,    weight = FontWeight.Medium),
    Font(resId = R.font.jetbrains_mono_bold,       weight = FontWeight.Bold),
)