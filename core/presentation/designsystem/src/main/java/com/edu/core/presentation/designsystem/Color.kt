package com.edu.core.presentation.designsystem

import androidx.compose.ui.graphics.Color

// ---------------------------------------------------------------------------
// Raw palette — sourced from /design/tokens.jsx
// Consume the semantic Material3 role aliases in Theme.kt / ColorScheme.
// Reference these raw values only in CineVault-specific composables that
// need a colour outside the standard role system (e.g. RatingBadge, star).
// ---------------------------------------------------------------------------

// Backgrounds
val CvBg          = Color(0xFF0A0A0A)  // app background — near-black, OLED-friendly
val CvSurface     = Color(0xFF141414)  // cards
val CvSurfaceElev = Color(0xFF1F1F1F)  // elevated chips, buttons
val CvSurfaceHi   = Color(0xFF2A2A2A)  // highest elevation / hover state

// Borders
val CvBorder       = Color(0x14FFFFFF)  // rgba(255,255,255,0.08)
val CvBorderStrong = Color(0x24FFFFFF)  // rgba(255,255,255,0.14)

// Text
val CvText     = Color(0xFFF5F5F5)
val CvTextDim  = Color(0xFFA3A3A3)
val CvTextMute = Color(0xFF6B6B6B)

// Accent
val CvAmber     = Color(0xFFF5A623)  // primary amber accent
val CvAmberDim  = Color(0xFFC9881A)  // dimmed amber (hover / pressed)
val CvAmberSoft = Color(0x24F5A623)  // rgba(245,166,35,0.14) ≈ 14% alpha container
val CvGold      = Color(0xFFE8C46B)  // warm gold — secondary accent

// Status
val CvDanger  = Color(0xFFFF4757)
val CvSuccess = Color(0xFF3DDC97)

// Rating-specific (used by RatingBadge composable)
val CvStar     = Color(0xFFF5A623)  // same hue as CvAmber — rating stars
val CvCritical = Color(0xFFFF6B35)  // orange-red for scores < 5

// ---------------------------------------------------------------------------
// Dark colour-scheme role aliases (consumed by Theme.kt darkColorScheme)
// ---------------------------------------------------------------------------
internal val DarkPrimary            = CvAmber
internal val DarkOnPrimary          = CvBg
internal val DarkPrimaryContainer   = CvAmberSoft
internal val DarkOnPrimaryContainer = CvAmber

internal val DarkSecondary            = CvGold
internal val DarkOnSecondary          = CvBg
internal val DarkSecondaryContainer   = CvSurfaceElev
internal val DarkOnSecondaryContainer = CvGold

internal val DarkTertiary            = CvSuccess
internal val DarkOnTertiary          = CvBg
internal val DarkTertiaryContainer   = Color(0xFF0D3324)
internal val DarkOnTertiaryContainer = CvSuccess

internal val DarkBackground         = CvBg
internal val DarkOnBackground       = CvText
internal val DarkSurface            = CvSurface
internal val DarkOnSurface          = CvText
internal val DarkSurfaceVariant     = CvSurfaceElev
internal val DarkOnSurfaceVariant   = CvTextDim

internal val DarkError            = CvDanger
internal val DarkOnError          = CvBg
internal val DarkErrorContainer   = Color(0xFF3B0010)
internal val DarkOnErrorContainer = CvDanger

internal val DarkOutline        = CvBorder
internal val DarkOutlineVariant = CvBorderStrong

// ---------------------------------------------------------------------------
// Light colour-scheme role aliases (consumed by Theme.kt lightColorScheme)
// ---------------------------------------------------------------------------
internal val LightPrimary            = CvAmberDim
internal val LightOnPrimary          = Color(0xFFFFFFFF)
internal val LightPrimaryContainer   = Color(0xFFFFEDD0)
internal val LightOnPrimaryContainer = Color(0xFF3A2000)

internal val LightSecondary            = Color(0xFF7A6020)
internal val LightOnSecondary          = Color(0xFFFFFFFF)
internal val LightSecondaryContainer   = Color(0xFFFFE08A)
internal val LightOnSecondaryContainer = Color(0xFF241A00)

internal val LightTertiary            = Color(0xFF1A7D56)
internal val LightOnTertiary          = Color(0xFFFFFFFF)
internal val LightTertiaryContainer   = Color(0xFFB0F4D8)
internal val LightOnTertiaryContainer = Color(0xFF002114)

internal val LightBackground         = Color(0xFFFFFBF5)
internal val LightOnBackground       = Color(0xFF1C1B18)
internal val LightSurface            = Color(0xFFFFF8EE)
internal val LightOnSurface          = Color(0xFF1C1B18)
internal val LightSurfaceVariant     = Color(0xFFF0E6D3)
internal val LightOnSurfaceVariant   = Color(0xFF4E4638)

internal val LightError            = Color(0xFFBA1A1A)
internal val LightOnError          = Color(0xFFFFFFFF)
internal val LightErrorContainer   = Color(0xFFFFDAD6)
internal val LightOnErrorContainer = Color(0xFF410002)

internal val LightOutline        = Color(0xFF7E7668)
internal val LightOutlineVariant = Color(0xFFD0C5B4)