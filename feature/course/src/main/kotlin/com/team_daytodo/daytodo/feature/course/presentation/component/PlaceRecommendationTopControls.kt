package com.team_daytodo.daytodo.feature.course.presentation.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team_daytodo.daytodo.feature.course.R
import com.team_daytodo.daytodo.feature.course.model.PlaceCourseMode
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme
import kotlin.math.abs

@Composable
internal fun PlaceRecommendationSearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSearch: () -> Unit,
    onClearClick: () -> Unit,
    onFocusChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = androidx.compose.ui.platform.LocalFocusManager.current
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(47.dp),
        shape = RoundedCornerShape(40.dp),
        color = Color.White,
        shadowElevation = 1.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = null,
                tint = DayTodoTheme.colors.textPrimary,
                modifier = Modifier
                    .size(24.dp)
                    .padding(3.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .weight(1f)
                    .onFocusChanged { onFocusChange(it.isFocused) },
                singleLine = true,
                textStyle = DayTodoTheme.typography.caption1.copy(
                    color = DayTodoTheme.colors.textPrimary,
                    letterSpacing = 0.sp,
                ),
                cursorBrush = SolidColor(DayTodoTheme.colors.brandPrimary),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search,
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        focusManager.clearFocus()
                        onSearch()
                    },
                ),
                decorationBox = { innerTextField ->
                    Box(contentAlignment = Alignment.CenterStart) {
                        if (value.isBlank()) {
                            Text(
                                text = "검색",
                                style = DayTodoTheme.typography.caption1.copy(letterSpacing = 0.sp),
                                color = DayTodoTheme.colors.textSecondary,
                            )
                        }
                        innerTextField()
                    }
                },
            )
            if (value.isNotBlank()) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clickable(role = Role.Button, onClick = onClearClick),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_cancel),
                        contentDescription = "검색어 지우기",
                        tint = DayTodoTheme.colors.textPrimary,
                        modifier = Modifier.size(13.dp),
                    )
                }
            }
        }
    }
}

@Composable
internal fun PlaceCourseModeSwitch(
    mode: PlaceCourseMode,
    onModeClick: (PlaceCourseMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    val selectedIndex = if (mode == PlaceCourseMode.Recommendation) 0 else 1
    val animatedIndex by animateFloatAsState(
        targetValue = selectedIndex.toFloat(),
        animationSpec = spring(
            dampingRatio = 0.58f,
            stiffness = 520f,
        ),
        label = "gooeyTogglePosition",
    )

    Surface(
        modifier = modifier.size(width = 48.dp, height = 84.dp),
        shape = RoundedCornerShape(999.dp),
        color = Color.White,
        shadowElevation = 1.dp,
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val radius = 18.dp.toPx()
                val centerX = size.width / 2f
                val topY = 24.dp.toPx()
                val bottomY = 60.dp.toPx()
                val selectedY = topY + (bottomY - topY) * animatedIndex.coerceIn(0f, 1f)
                val previousY = if (selectedIndex == 1) topY else bottomY
                val fractionalDistance = abs(animatedIndex - selectedIndex)
                val bridgeAlpha = (fractionalDistance * (1f - fractionalDistance) * 4f)
                    .coerceIn(0f, 0.72f)

                if (bridgeAlpha > 0.01f) {
                    val bridgeTop = minOf(previousY, selectedY)
                    val bridgeBottom = maxOf(previousY, selectedY)
                    drawRoundRect(
                        color = Color(0xFF8B8AF5).copy(alpha = bridgeAlpha),
                        topLeft = Offset(centerX - radius, bridgeTop - radius),
                        size = Size(radius * 2f, bridgeBottom - bridgeTop + radius * 2f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(radius, radius),
                    )
                }
                drawCircle(
                    color = Color(0xFF8B8AF5),
                    radius = radius,
                    center = Offset(centerX, selectedY),
                )
            }
            ToggleTextItem(
                text = "추천",
                selected = mode == PlaceCourseMode.Recommendation,
                onClick = { onModeClick(PlaceCourseMode.Recommendation) },
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 6.dp),
            )
            ToggleTextItem(
                text = "코스",
                selected = mode == PlaceCourseMode.Course,
                onClick = { onModeClick(PlaceCourseMode.Course) },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 6.dp),
            )
        }
    }
}

@Composable
private fun ToggleTextItem(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(36.dp)
            .clickable(role = Role.Button, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = DayTodoTheme.typography.caption2.copy(letterSpacing = 0.sp),
            color = if (selected) Color.White else DayTodoTheme.colors.brandPrimary,
            maxLines = 1,
            textAlign = TextAlign.Center,
        )
    }
}
