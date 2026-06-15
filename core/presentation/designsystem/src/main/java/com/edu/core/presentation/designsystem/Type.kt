package com.edu.core.presentation.designsystem

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// ---------------------------------------------------------------------------
// CineVault type scale — faithfully mapped from /design/tokens.jsx
//
// Slots ← tokens.jsx names
//   displayLarge   ← displayL  (36 sp, Playfair Display, SemiBold, -0.5 ls)
//   displayMedium  ← displayM  (28 sp, Playfair Display, SemiBold, -0.3 ls)
//   displaySmall   ← (interp.) (22 sp, Playfair Display, SemiBold, -0.2 ls)
//   headlineLarge  ← headlineL (22 sp, Inter Tight,      SemiBold, -0.2 ls)
//   headlineMedium ← headlineM (18 sp, Inter Tight,      SemiBold, -0.1 ls)
//   headlineSmall  ← (interp.) (16 sp, Inter Tight,      SemiBold,  0.0 ls)
//   titleLarge     ← titleL    (16 sp, Inter Tight,      SemiBold,  0.0 ls)
//   titleMedium    ← titleM    (14 sp, Inter Tight,      SemiBold,  0.0 ls)
//   titleSmall     ← (interp.) (12 sp, Inter Tight,      SemiBold,  0.0 ls)
//   bodyLarge      ← bodyL     (15 sp, Inter Tight,      Normal,    0.0 ls)
//   bodyMedium     ← bodyM     (13 sp, Inter Tight,      Normal,    0.0 ls)
//   bodySmall      ← (interp.) (11 sp, Inter Tight,      Normal,    0.0 ls)
//   labelLarge     ← labelL    (12 sp, Inter Tight,      Medium,   +0.3 ls)
//   labelMedium    ← labelM    (10 sp, JetBrains Mono,   SemiBold, +1.2 ls)
//   labelSmall     ← (interp.) ( 9 sp, JetBrains Mono,   Medium,   +1.5 ls)
//
// Line heights are calculated from the multipliers in tokens.jsx (rounded to
// the nearest even sp for pixel-grid alignment).
// ---------------------------------------------------------------------------

val CineVaultTypography = Typography(
    // ---- Display (hero / splash) — Playfair Display serif ----------------
    displayLarge = TextStyle(
        fontFamily   = PlayfairDisplayFontFamily,
        fontWeight   = FontWeight.SemiBold,
        fontSize     = 36.sp,
        lineHeight   = 40.sp,   // 36 * 1.10 = 39.6
        letterSpacing = (-0.5).sp,
    ),
    displayMedium = TextStyle(
        fontFamily   = PlayfairDisplayFontFamily,
        fontWeight   = FontWeight.SemiBold,
        fontSize     = 28.sp,
        lineHeight   = 32.sp,   // 28 * 1.15 = 32.2
        letterSpacing = (-0.3).sp,
    ),
    displaySmall = TextStyle(
        fontFamily   = PlayfairDisplayFontFamily,
        fontWeight   = FontWeight.SemiBold,
        fontSize     = 22.sp,
        lineHeight   = 26.sp,   // 22 * 1.20 = 26.4
        letterSpacing = (-0.2).sp,
    ),

    // ---- Headline (section headers, movie titles) — Inter Tight ----------
    headlineLarge = TextStyle(
        fontFamily   = InterTightFontFamily,
        fontWeight   = FontWeight.SemiBold,
        fontSize     = 22.sp,
        lineHeight   = 28.sp,   // 22 * 1.25 = 27.5
        letterSpacing = (-0.2).sp,
    ),
    headlineMedium = TextStyle(
        fontFamily   = InterTightFontFamily,
        fontWeight   = FontWeight.SemiBold,
        fontSize     = 18.sp,
        lineHeight   = 24.sp,   // 18 * 1.30 = 23.4
        letterSpacing = (-0.1).sp,
    ),
    headlineSmall = TextStyle(
        fontFamily   = InterTightFontFamily,
        fontWeight   = FontWeight.SemiBold,
        fontSize     = 16.sp,
        lineHeight   = 22.sp,   // 16 * 1.35 = 21.6
        letterSpacing = 0.sp,
    ),

    // ---- Title (card titles, list items) — Inter Tight -------------------
    titleLarge = TextStyle(
        fontFamily   = InterTightFontFamily,
        fontWeight   = FontWeight.SemiBold,
        fontSize     = 16.sp,
        lineHeight   = 22.sp,   // 16 * 1.40 = 22.4
        letterSpacing = 0.sp,
    ),
    titleMedium = TextStyle(
        fontFamily   = InterTightFontFamily,
        fontWeight   = FontWeight.SemiBold,
        fontSize     = 14.sp,
        lineHeight   = 20.sp,   // 14 * 1.40 = 19.6
        letterSpacing = 0.sp,
    ),
    titleSmall = TextStyle(
        fontFamily   = InterTightFontFamily,
        fontWeight   = FontWeight.SemiBold,
        fontSize     = 12.sp,
        lineHeight   = 17.sp,   // 12 * 1.40 = 16.8
        letterSpacing = 0.sp,
    ),

    // ---- Body (descriptions, reviews) — Inter Tight ----------------------
    bodyLarge = TextStyle(
        fontFamily   = InterTightFontFamily,
        fontWeight   = FontWeight.Normal,
        fontSize     = 15.sp,
        lineHeight   = 24.sp,   // 15 * 1.50 = 22.5
        letterSpacing = 0.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily   = InterTightFontFamily,
        fontWeight   = FontWeight.Normal,
        fontSize     = 13.sp,
        lineHeight   = 20.sp,   // 13 * 1.50 = 19.5
        letterSpacing = 0.sp,
    ),
    bodySmall = TextStyle(
        fontFamily   = InterTightFontFamily,
        fontWeight   = FontWeight.Normal,
        fontSize     = 11.sp,
        lineHeight   = 17.sp,   // 11 * 1.50 = 16.5
        letterSpacing = 0.sp,
    ),

    // ---- Label (buttons, chips, metadata, ratings) -----------------------
    labelLarge = TextStyle(
        fontFamily   = InterTightFontFamily,
        fontWeight   = FontWeight.Medium,
        fontSize     = 12.sp,
        lineHeight   = 16.sp,   // 12 * 1.30 = 15.6
        letterSpacing = 0.3.sp,
    ),
    // labelM in tokens — monospace for badge / ticker text
    labelMedium = TextStyle(
        fontFamily   = JetBrainsMonoFontFamily,
        fontWeight   = FontWeight.SemiBold,
        fontSize     = 10.sp,
        lineHeight   = 13.sp,   // 10 * 1.30 = 13.0
        letterSpacing = 1.2.sp,
    ),
    labelSmall = TextStyle(
        fontFamily   = JetBrainsMonoFontFamily,
        fontWeight   = FontWeight.Medium,
        fontSize     = 9.sp,
        lineHeight   = 12.sp,   // 9 * 1.30 = 11.7
        letterSpacing = 1.5.sp,
    ),
)