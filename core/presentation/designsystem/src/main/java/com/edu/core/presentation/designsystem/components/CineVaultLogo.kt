package com.edu.core.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.edu.core.presentation.designsystem.CvAmber
import com.edu.core.presentation.designsystem.CvBg
import com.edu.core.presentation.designsystem.CvText
import com.edu.core.presentation.designsystem.PlayfairDisplayFontFamily
import com.edu.core.presentation.designsystem.CineVaultSpacing

@Composable
fun CineVaultLogo(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(CineVaultSpacing.sm),
    ) {
        Box(
            modifier = Modifier
                .size(26.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(CvAmber),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "C",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = CvBg,
                    fontFamily = PlayfairDisplayFontFamily,
                    fontWeight = FontWeight.Bold,
                ),
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Cine",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = CvText,
                    fontFamily = PlayfairDisplayFontFamily,
                    letterSpacing = (-0.3).sp,
                ),
            )
            Text(
                text = "Vault",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = CvAmber,
                    fontFamily = PlayfairDisplayFontFamily,
                    fontStyle = FontStyle.Italic,
                    letterSpacing = (-0.3).sp,
                ),
            )
        }
    }
}