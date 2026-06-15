package com.edu.core.presentation.designsystem.icon

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

val broken_image: ImageVector by lazy {
    ImageVector.Builder(
        name = "broken_image",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            fill = SolidColor(Color.Black),
            fillAlpha = 1f,
            stroke = null,
            strokeAlpha = 1f,
            strokeLineWidth = 1f,
            strokeLineCap = StrokeCap.Butt,
            strokeLineJoin = StrokeJoin.Bevel,
            strokeLineMiter = 1f,
            pathFillType = PathFillType.NonZero,
        ) {
            moveTo(5f, 21f)
            quadTo(4.18f, 21f, 3.59f, 20.41f)
            reflectiveQuadTo(3f, 19f)
            verticalLineTo(5f)
            quadTo(3f, 4.17f, 3.59f, 3.59f)
            reflectiveQuadTo(5f, 3f)
            horizontalLineTo(19f)
            quadToRelative(0.83f, 0f, 1.41f, 0.59f)
            reflectiveQuadTo(21f, 5f)
            verticalLineTo(19f)
            quadToRelative(0f, 0.82f, -0.59f, 1.41f)
            reflectiveQuadTo(19f, 21f)
            horizontalLineTo(5f)
            close()
            moveTo(6f, 12.58f)
            lineToRelative(4f, -4f)
            lineToRelative(4f, 4f)
            lineToRelative(4f, -4f)
            lineToRelative(1f, 1f)
            verticalLineTo(5f)
            horizontalLineTo(5f)
            verticalLineToRelative(6.57f)
            lineToRelative(1f, 1f)
            close()
            moveTo(5f, 19f)
            horizontalLineTo(19f)
            verticalLineTo(12.4f)
            lineToRelative(-1f, -1f)
            lineToRelative(-4f, 4f)
            lineToRelative(-4f, -4f)
            lineToRelative(-4f, 4f)
            lineToRelative(-1f, -1f)
            verticalLineTo(19f)
            close()
            moveToRelative(0f, 0f)
            verticalLineTo(12.4f)
            verticalLineToRelative(2f)
            verticalLineTo(11.58f)
            verticalLineToRelative(-2f)
            verticalLineTo(5f)
            verticalLineToRelative(6.57f)
            verticalLineTo(14.4f)
            verticalLineTo(19f)
            close()
        }
    }.build()
}

@Preview(showBackground = true)
@Composable
private fun BrokenImagePreview() {
    Icon(
        imageVector = broken_image,
        contentDescription = null,
    )
}