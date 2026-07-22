package com.team_daytodo.daytodo.feature.course.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.team_daytodo.daytodo.domain.course.model.CourseDate
import com.team_daytodo.daytodo.feature.course.R
import com.team_daytodo.daytodo.feature.course.presentation.defaults.CourseCreateDefaults.fieldContentColor
import com.team_daytodo.daytodo.feature.course.presentation.defaults.CourseCreateDefaults.parentRegionSelectedBackground
import com.team_daytodo.daytodo.feature.course.presentation.defaults.CourseCreateDefaults.placeholderColor
import com.team_daytodo.daytodo.feature.course.presentation.defaults.CourseCreateDefaults.sundayColor
import com.team_daytodo.daytodo.uikit.R as UIKitR
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme
import java.util.Calendar

@Composable
internal fun CourseDatePickerDialog(
    initialDate: CourseDate,
    minimumDate: CourseDate,
    onDateSelected: (CourseDate) -> Unit,
    onPastDateClick: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    var visibleYear by remember { mutableIntStateOf(initialDate.year) }
    var visibleMonth by remember { mutableIntStateOf(initialDate.month) }
    var dialogSelectedDate by remember { mutableStateOf(initialDate) }

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 372.dp),
            shape = RoundedCornerShape(12.dp),
            color = Color.White,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 27.dp, bottom = 17.dp),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(13.dp)
                        .padding(end = 25.dp),
                    contentAlignment = Alignment.CenterEnd,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_cancel),
                        contentDescription = "취소",
                        tint = fieldContentColor,
                        modifier = Modifier
                            .size(13.dp)
                            .clickable(role = Role.Button, onClick = onDismissRequest),
                    )
                }
                Spacer(modifier = Modifier.height(14.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(18.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "${visibleYear}년",
                        style = DayTodoTheme.typography.body3,
                        color = fieldContentColor,
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(28.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(id = UIKitR.drawable.ic_back),
                        contentDescription = "이전 달",
                        tint = fieldContentColor,
                        modifier = Modifier
                            .size(width = 20.dp, height = 23.dp)
                            .clickable(role = Role.Button) {
                                val previous = previousMonth(visibleYear, visibleMonth)
                                visibleYear = previous.first
                                visibleMonth = previous.second
                            },
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "${visibleMonth}월",
                        style = DayTodoTheme.typography.title1,
                        color = fieldContentColor,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Icon(
                        painter = painterResource(id = UIKitR.drawable.ic_next),
                        contentDescription = "다음 달",
                        tint = fieldContentColor,
                        modifier = Modifier
                            .size(width = 20.dp, height = 23.dp)
                            .clickable(role = Role.Button) {
                                val next = nextMonth(visibleYear, visibleMonth)
                                visibleYear = next.first
                                visibleMonth = next.second
                            },
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                CalendarGrid(
                    visibleYear = visibleYear,
                    visibleMonth = visibleMonth,
                    selectedDate = dialogSelectedDate,
                    minimumDate = minimumDate,
                    onDateClick = { dialogSelectedDate = it },
                    onPastDateClick = onPastDateClick,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .widthIn(max = 327.dp)
                        .padding(horizontal = 20.dp),
                )
                Spacer(modifier = Modifier.height(23.dp))
                Text(
                    text = "확인",
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(end = 22.dp)
                        .clickable(role = Role.Button) { onDateSelected(dialogSelectedDate) },
                    style = DayTodoTheme.typography.title1.copy(
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.sp,
                    ),
                    color = fieldContentColor,
                )
            }
        }
    }
}

@Composable
private fun CalendarGrid(
    visibleYear: Int,
    visibleMonth: Int,
    selectedDate: CourseDate,
    minimumDate: CourseDate,
    onDateClick: (CourseDate) -> Unit,
    onPastDateClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val weekDays = listOf("일", "월", "화", "수", "목", "금", "토")
    val dates = remember(visibleYear, visibleMonth) {
        calendarCells(visibleYear, visibleMonth)
    }

    Column(modifier = modifier) {
        Row(modifier = Modifier.fillMaxWidth()) {
            weekDays.forEachIndexed { index, day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    style = DayTodoTheme.typography.body3,
                    color = if (index == 0) sundayColor else fieldContentColor,
                    textAlign = TextAlign.Center,
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        dates.chunked(7).forEach { week ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(28.dp),
            ) {
                week.forEachIndexed { index, day ->
                    val date = day?.let { CourseDate(visibleYear, visibleMonth, it) }
                    val isSelected = date == selectedDate
                    val isPastDate = date?.isBefore(minimumDate) == true

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .clip(CircleShape)
                            .background(if (isSelected) parentRegionSelectedBackground else Color.Transparent)
                            .clickable(
                                enabled = date != null,
                                role = Role.Button,
                            ) {
                                when {
                                    date == null -> Unit
                                    isPastDate -> onPastDateClick()
                                    else -> onDateClick(date)
                                }
                            },
                        contentAlignment = Alignment.Center,
                    ) {
                        if (day != null) {
                            Text(
                                text = day.toString(),
                                style = DayTodoTheme.typography.body3,
                                color = when {
                                    isPastDate -> placeholderColor
                                    index == 0 -> sundayColor
                                    else -> fieldContentColor
                                },
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(5.dp))
        }
    }
}

internal fun todayCourseDate(): CourseDate {
    val calendar = Calendar.getInstance()
    return CourseDate(
        year = calendar.get(Calendar.YEAR),
        month = calendar.get(Calendar.MONTH) + 1,
        day = calendar.get(Calendar.DAY_OF_MONTH),
    )
}

private fun calendarCells(year: Int, month: Int): List<Int?> {
    val calendar = Calendar.getInstance().apply {
        set(Calendar.YEAR, year)
        set(Calendar.MONTH, month - 1)
        set(Calendar.DAY_OF_MONTH, 1)
    }
    val firstDayOffset = calendar.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY
    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    return buildList {
        repeat(firstDayOffset) { add(null) }
        for (day in 1..daysInMonth) {
            add(day)
        }
        while (size % 7 != 0 || size < 42) {
            add(null)
        }
    }
}

private fun previousMonth(year: Int, month: Int): Pair<Int, Int> =
    if (month == 1) {
        year - 1 to 12
    } else {
        year to month - 1
    }

private fun nextMonth(year: Int, month: Int): Pair<Int, Int> =
    if (month == 12) {
        year + 1 to 1
    } else {
        year to month + 1
    }

private fun CourseDate.isBefore(other: CourseDate): Boolean =
    year < other.year ||
        (year == other.year && month < other.month) ||
        (year == other.year && month == other.month && day < other.day)
