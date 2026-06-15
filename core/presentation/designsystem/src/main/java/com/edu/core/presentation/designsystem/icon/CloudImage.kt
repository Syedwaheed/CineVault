package com.edu.core.presentation.designsystem.icon

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


 val cloud_image: ImageVector by lazy {
   ImageVector.Builder(
     name = "cloud",
     defaultWidth = 24.dp,
     defaultHeight = 24.dp,
     viewportWidth = 24f,
     viewportHeight = 24f,
   )
     .apply {
       path(
         fill = SolidColor(Color.Black),
         fillAlpha = 1f,
         stroke = null,
         strokeAlpha = 1f,
         strokeLineWidth = 1f,
         strokeLineCap = StrokeCap.Butt,
         strokeLineJoin = StrokeJoin.Bevel,
         strokeLineMiter = 1f,
         pathFillType = PathFillType.Companion.NonZero,
       ) {
         moveTo(6.5f, 20f)
         quadTo(4.23f, 20f, 2.61f, 18.43f)
         reflectiveQuadTo(1f, 14.58f)
         quadTo(1f, 12.63f, 2.18f, 11.1f)
         reflectiveQuadTo(5.25f, 9.15f)
         quadTo(5.88f, 6.85f, 7.75f, 5.43f)
         reflectiveQuadTo(12f, 4f)
         quadToRelative(2.93f, 0f, 4.96f, 2.04f)
         reflectiveQuadTo(19f, 11f)
         quadToRelative(1.73f, 0.2f, 2.86f, 1.49f)
         reflectiveQuadTo(23f, 15.5f)
         quadToRelative(0f, 1.88f, -1.31f, 3.19f)
         reflectiveQuadTo(18.5f, 20f)
         horizontalLineTo(6.5f)
         close()
         moveToRelative(0f, -2f)
         horizontalLineToRelative(12f)
         quadToRelative(1.05f, 0f, 1.78f, -0.73f)
         reflectiveQuadTo(21f, 15.5f)
         reflectiveQuadTo(20.28f, 13.73f)
         reflectiveQuadTo(18.5f, 13f)
         horizontalLineTo(17f)
         verticalLineTo(11f)
         quadTo(17f, 8.92f, 15.54f, 7.46f)
         reflectiveQuadTo(12f, 6f)
         quadTo(9.93f, 6f, 8.46f, 7.46f)
         reflectiveQuadTo(7f, 11f)
         horizontalLineTo(6.5f)
         quadTo(5.05f, 11f, 4.03f, 12.02f)
         reflectiveQuadTo(3f, 14.5f)
         reflectiveQuadToRelative(1.03f, 2.48f)
         reflectiveQuadTo(6.5f, 18f)
         close()
         moveTo(12f, 12f)
         close()
       }
     }
     .build()
 }


@Preview(showBackground = true)
@Composable
private fun CloudImagePreview() {
    Icon(
        imageVector        = cloud_image,
        contentDescription = null,
    )
}
