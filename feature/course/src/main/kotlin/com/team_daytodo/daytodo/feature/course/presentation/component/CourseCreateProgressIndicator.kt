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
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.feature.course.presentation.ProgressColor
import com.team_daytodo.daytodo.feature.course.presentation.ProgressTrackColor
import com.team_daytodo.daytodo.feature.course.presentation.TotalInputSteps

@Composable
fun CourseCreateProgressIndicator(
    step: Int,
    modifier: Modifier = Modifier,
) {
    val progressFraction by animateFloatAsState(
        targetValue = step.coerceIn(1, TotalInputSteps) / TotalInputSteps.toFloat(),
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
                .background(ProgressColor),
        )
    }
}
