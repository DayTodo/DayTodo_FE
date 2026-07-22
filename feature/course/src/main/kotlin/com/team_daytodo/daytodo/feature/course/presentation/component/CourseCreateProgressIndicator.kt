package com.team_daytodo.daytodo.feature.course.presentation.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.feature.course.model.CourseCreateStep
import com.team_daytodo.daytodo.feature.course.presentation.defaults.CourseCreateDefaults.progressColor

@Composable
fun CourseCreateProgressIndicator(
    step: Int,
    modifier: Modifier = Modifier,
) {
    val totalSteps = CourseCreateStep.entries.size
    val progressFraction by animateFloatAsState(
        targetValue = step.coerceIn(1, totalSteps) / totalSteps.toFloat(),
        animationSpec = tween(
            durationMillis = 320,
            easing = FastOutSlowInEasing,
        ),
        label = "course-create-progress",
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(8.dp)
            .clip(RoundedCornerShape(999.dp))
            .background(ProgressTrackColor),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progressFraction)
                .fillMaxHeight()
                .clip(RoundedCornerShape(999.dp))
                .background(progressColor),
        )
    }
}

private val ProgressTrackColor = Color(0xFFF3F4F6)
