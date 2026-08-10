package com.team_daytodo.daytodo.uikit.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

@Composable
fun DayTodoEmptyStateCard(
    message: String,
    iconPainter: Painter,
    modifier: Modifier = Modifier,
    height: Dp = 99.dp,
    iconWidth: Dp = 36.77.dp,
    iconHeight: Dp = 59.74.dp,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(height),
        shape = RoundedCornerShape(12.dp),
        color = EmptyCardColor,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                modifier = Modifier
                    .width(iconWidth)
                    .height(iconHeight),
                painter = iconPainter,
                contentDescription = null,
                tint = EmptySymbolColor,
            )
            Text(
                text = message,
                style = DayTodoTheme.typography.title2,
                color = DayTodoTheme.colors.textSecondary,
                textAlign = TextAlign.Center,
            )
        }
    }
}

private val EmptyCardColor = Color(0xFFF3F3F3)
private val EmptySymbolColor = Color(0xFFDFDFDF)
