package com.team_daytodo.daytodo.feature.course.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team_daytodo.daytodo.domain.course.model.CourseDate
import com.team_daytodo.daytodo.domain.course.model.CourseSummary
import com.team_daytodo.daytodo.feature.course.model.CourseListUiState
import com.team_daytodo.daytodo.feature.course.model.displayKoreanDateWithWeekday
import com.team_daytodo.daytodo.feature.course.presentation.component.CourseDatePickerDialog
import com.team_daytodo.daytodo.feature.course.presentation.component.todayCourseDate
import com.team_daytodo.daytodo.feature.course.presentation.defaults.relationshipColors
import com.team_daytodo.daytodo.uikit.component.DayTodoEmptyStateCard
import com.team_daytodo.daytodo.uikit.component.DayTodoHeaderSection
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme
import com.team_daytodo.daytodo.uikit.R as UIKitR

@Composable
fun CourseListScreen(
    uiState: CourseListUiState,
    onBackClick: () -> Unit,
    onDateSelected: (CourseDate?) -> Unit,
    onCourseClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showDateDialog by rememberSaveable { mutableStateOf(false) }
    val today = androidx.compose.runtime.remember { todayCourseDate() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DayTodoTheme.colors.backgroundDefault),
    ) {
        DayTodoHeaderSection(
            title = "예정 코스 목록",
            onBackClick = onBackClick,
        )
        Spacer(modifier = Modifier.height(24.dp))
        DateFilterRow(
            selectedDate = uiState.selectedDate,
            onFilterClick = { showDateDialog = true },
            onClearClick = { onDateSelected(null) },
            modifier = Modifier.padding(horizontal = 20.dp),
        )
        Spacer(modifier = Modifier.height(37.dp))

        when {
            uiState.isLoading -> LoadingMessage(text = "예정 코스를 불러오는 중이에요.")
            uiState.visibleCourses.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                ) {
                    DayTodoEmptyStateCard(
                        message = if (uiState.selectedDate == null) {
                            "참여 중인 예정 코스가 없어요."
                        } else {
                            "선택한 날짜의 코스가 없어요."
                        },
                        iconPainter = painterResource(id = UIKitR.drawable.ic_logo),
                    )
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(
                        start = 20.dp,
                        end = 20.dp,
                        bottom = 180.dp,
                    ),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(
                        items = uiState.visibleCourses,
                        key = CourseSummary::id,
                    ) { course ->
                        UpcomingCourseCard(
                            course = course,
                            onClick = { onCourseClick(course.id) },
                        )
                    }
                }
            }
        }
    }

    if (showDateDialog) {
        CourseDatePickerDialog(
            initialDate = uiState.selectedDate ?: today,
            minimumDate = today,
            onDateSelected = {
                onDateSelected(it)
                showDateDialog = false
            },
            onPastDateClick = {},
            onDismissRequest = { showDateDialog = false },
        )
    }
}

@Composable
private fun DateFilterRow(
    selectedDate: CourseDate?,
    onFilterClick: () -> Unit,
    onClearClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val borderColor = DayTodoTheme.colors.textPrimary

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(24.dp)
            .drawBehind {
                val strokeWidth = 0.5.dp.toPx()
                val y = size.height - strokeWidth / 2

                drawLine(
                    color = borderColor,
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = strokeWidth
                )
            }
            .padding(start = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = selectedDate?.displayKoreanDateWithWeekday() ?: "날짜 필터",
            modifier = Modifier.weight(1f),
            style = DayTodoTheme.typography.caption1.copy(letterSpacing = 0.sp),
            color = DayTodoTheme.colors.textPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        if (selectedDate != null) {
            Text(
                text = "초기화",
                modifier = Modifier
                    .clickable(role = Role.Button, onClick = onClearClick)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                style = DayTodoTheme.typography.caption2.copy(letterSpacing = 0.sp),
                color = DayTodoTheme.colors.brandPrimary,
            )
        }
        Box(
            modifier = Modifier
                .size(24.dp)
                .clickable(role = Role.Button, onClick = onFilterClick)
                .padding(4.dp),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(id = UIKitR.drawable.ic_filter),
                contentDescription = "날짜 필터",
                tint = DayTodoTheme.colors.textPrimary,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

@Composable
private fun UpcomingCourseCard(
    course: CourseSummary,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = relationshipColors(course.relationship)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(116.dp)
            .clickable(role = Role.Button, onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = colors.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
        ) {
            Text(
                text = course.name,
                style = DayTodoTheme.typography.headlineSmall.copy(letterSpacing = 0.sp),
                color = colors.text,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = course.date.displayKoreanDateWithWeekday(),
                style = DayTodoTheme.typography.title3.copy(letterSpacing = 0.sp),
                color = DayTodoTheme.colors.textSecondary,
                maxLines = 1,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = "멤버: ${course.members.size}명",
                    style = DayTodoTheme.typography.caption1.copy(letterSpacing = 0.sp),
                    color = DayTodoTheme.colors.textPrimary,
                )
                Text(
                    text = "담아 놓은 장소: ${course.placeCount}개",
                    style = DayTodoTheme.typography.caption1.copy(letterSpacing = 0.sp),
                    color = DayTodoTheme.colors.textPrimary,
                )
            }
        }
    }
}

@Composable
private fun LoadingMessage(
    text: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 60.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = DayTodoTheme.typography.caption1.copy(letterSpacing = 0.sp),
            color = DayTodoTheme.colors.textSecondary,
        )
    }
}
