package com.team_daytodo.daytodo.feature.course.presentation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CourseCreateLoadingScreen(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            LoadingDots()
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "그룹 생성 중",
                style = DayTodoTheme.typography.headlineLarge,
                color = fieldContentColor,
            )
        }
    }
}

@Composable
private fun LoadingDots(
    modifier: Modifier = Modifier,
) {
    val transition = rememberInfiniteTransition(label = "course-create-loading")
    val activeDot by transition.animateFloat(
        initialValue = 0f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1100, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "active-dot",
    )

    Canvas(modifier = modifier.size(96.dp)) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val orbitRadius = size.minDimension / 2f - 8.dp.toPx()
        val dotRadius = 5.dp.toPx()
        val activeIndex = activeDot.toInt() % 8

        repeat(8) { index ->
            val angle = -PI / 2.0 + (2.0 * PI * index / 8.0)
            val dotCenter = Offset(
                x = center.x + orbitRadius * cos(angle).toFloat(),
                y = center.y + orbitRadius * sin(angle).toFloat(),
            )
            drawCircle(
                color = if (index == activeIndex) LoadingActiveColor else LoadingInactiveColor,
                radius = dotRadius,
                center = dotCenter,
            )
        }
    }
}