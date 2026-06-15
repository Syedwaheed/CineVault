package com.edu.core.presentation.designsystem

import androidx.compose.ui.unit.dp

// ---------------------------------------------------------------------------
// CineVaultSpacing — token-based spacing scale
// Source: /design/tokens.jsx → CV.space
//
// Usage:  Modifier.padding(CineVaultSpacing.lg)
//         Arrangement.spacedBy(CineVaultSpacing.sm)
// ---------------------------------------------------------------------------
object CineVaultSpacing {
    /** 4 dp  — space1 — tight insets, icon padding */
    val xs  = 4.dp
    /** 8 dp  — space2 — compact gaps, small chips */
    val sm  = 8.dp
    /** 12 dp — space3 — list item inner padding */
    val md  = 12.dp
    /** 16 dp — space4 — standard screen edge margin */
    val lg  = 16.dp
    /** 20 dp — space5 — card inner padding */
    val xl  = 20.dp
    /** 24 dp — space6 — section vertical gap */
    val xxl = 24.dp
    /** 32 dp — space8 — large vertical rhythm */
    val xxxl = 32.dp
    /** 40 dp — space10 — hero section padding */
    val huge = 40.dp
}

// ---------------------------------------------------------------------------
// CineVaultRadius — token-based corner radius scale
// Source: /design/tokens.jsx → CV.radius
//
// Usage:  RoundedCornerShape(CineVaultRadius.md)
// ---------------------------------------------------------------------------
object CineVaultRadius {
    /** 4 dp  — micro chips, small badges */
    val xs   = 4.dp
    /** 8 dp  — standard chips, text fields */
    val sm   = 8.dp
    /** 12 dp — cards, bottom sheet top corners */
    val md   = 12.dp
    /** 16 dp — elevated cards */
    val lg   = 16.dp
    /** 20 dp — hero banners, large surfaces */
    val xl   = 20.dp
    /** 999 dp — pill-shaped buttons, genre tags */
    val pill = 999.dp
}