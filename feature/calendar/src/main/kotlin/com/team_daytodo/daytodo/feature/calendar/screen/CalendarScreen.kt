package com.team_daytodo.daytodo.feature.calendar.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.daysOfWeek
import com.team_daytodo.daytodo.feature.calendar.R
import com.team_daytodo.daytodo.feature.calendar.component.CalendarDayCell
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

private val selectedDateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")

// TODO: DayTodoTheme.colors 에 빨강 토큰 추가 후 교체 (임시 하드코딩)
private val SundayColor = Color(0xFFFF5B00)

// 코스 있음 박스 배경: 연보라. uikit 토큰 미노출로 임시 하드코딩
private val CourseContainerColor = Color(0xFFECECFF)

// 더미 데이터: 특정 날짜에만 코스가 존재
private val defaultCoursesByDate = mapOf(
    LocalDate.of(2026, 7, 29) to "홍대거리 코스",
    LocalDate.of(2026, 7, 16) to "성수 데이트 코스",
)

@Composable
fun CalendarScreen(
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    initialSelectedDate: LocalDate = LocalDate.now(),
    coursesByDate: Map<LocalDate, String> = defaultCoursesByDate,
) {
    var selectedDate by remember { mutableStateOf(initialSelectedDate) }

    val currentMonth = remember(initialSelectedDate) { YearMonth.from(initialSelectedDate) }
    val firstDayOfWeek = DayOfWeek.SUNDAY
    val state = rememberCalendarState(
        startMonth = currentMonth.minusMonths(12),
        endMonth = currentMonth.plusMonths(12),
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = firstDayOfWeek,
    )
    val coroutineScope = rememberCoroutineScope()
    val visibleMonth = state.firstVisibleMonth.yearMonth

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = DayTodoTheme.colors.backgroundDefault,
        topBar = {
            Column {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.align(Alignment.CenterStart),
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back_ios),
                            contentDescription = "뒤로가기",
                        )
                    }
                    Text(
                        text = "캘린더",
                        style = DayTodoTheme.typography.title1,
                        color = DayTodoTheme.colors.textPrimary,
                        modifier = Modifier.align(Alignment.Center),
                    )
                }
                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color(0xFFE0E0E0),
                )
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 18.dp),
        ) {
            MonthNavigationRow(
                month = visibleMonth,
                onPreviousClick = {
                    coroutineScope.launch {
                        state.animateScrollToMonth(visibleMonth.minusMonths(1))
                    }
                },
                onNextClick = {
                    coroutineScope.launch {
                        state.animateScrollToMonth(visibleMonth.plusMonths(1))
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
            )

            WeekDayHeader(firstDayOfWeek = firstDayOfWeek)

            HorizontalCalendar(
                state = state,
                dayContent = { day ->
                    CalendarDayCell(
                        day = day,
                        isSelected = day.date == selectedDate,
                        hasCourse = coursesByDate.containsKey(day.date),
                        onClick = { selectedDate = it },
                    )
                },
            )

            SelectedCourseSection(
                selectedDate = selectedDate,
                courseName = coursesByDate[selectedDate],
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
            )
        }
    }
}

@Composable
private fun MonthNavigationRow(
    month: YearMonth,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = onPreviousClick) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowLeft,
                contentDescription = "이전 달",
                tint = DayTodoTheme.colors.iconDefault,
            )
        }
        Text(
            text = "${month.monthValue}월",
            style = DayTodoTheme.typography.title2,
            color = DayTodoTheme.colors.textPrimary,
        )
        IconButton(onClick = onNextClick) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "다음 달",
                tint = DayTodoTheme.colors.iconDefault,
            )
        }
    }
}

@Composable
private fun WeekDayHeader(
    firstDayOfWeek: DayOfWeek,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier.fillMaxWidth()) {
        daysOfWeek(firstDayOfWeek).forEach { dayOfWeek ->
            Text(
                text = dayOfWeek.getDisplayName(TextStyle.NARROW, Locale.ENGLISH),
                style = DayTodoTheme.typography.caption2,
                // 일요일 헤더는 날짜 셀 일요일과 동일한 빨강
                color = if (dayOfWeek == DayOfWeek.SUNDAY) {
                    SundayColor
                } else {
                    DayTodoTheme.colors.textSecondary
                },
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun SelectedCourseSection(
    selectedDate: LocalDate,
    courseName: String?,
    modifier: Modifier = Modifier,
) {
    val hasCourse = courseName != null
    Column(modifier = modifier) {
        Text(
            text = selectedDate.format(selectedDateFormatter),
            style = DayTodoTheme.typography.label2,
            color = DayTodoTheme.colors.textPrimary,
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 13.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    color = if (hasCourse) {
                        // 코스 있음: 연보라 컨테이너 배경 (purple100, 하드코딩)
                        CourseContainerColor
                    } else {
                        // 코스 없음: 옅은 회색 배경
                        DayTodoTheme.colors.backgroundSecondary
                    },
                )
                .padding(16.dp),
        ) {
            Text(
                text = courseName ?: "오늘의 코스가 없어요",
                style = DayTodoTheme.typography.label2,
                color = if (hasCourse) {
                    DayTodoTheme.colors.textPrimary
                } else {
                    DayTodoTheme.colors.textSecondary
                },
            )
        }
    }
}

/**
 * 선택된 날짜(7/18)에 코스가 "없는" 상태.
 * - 18일: 회색 네모(선택 + 코스 없음)
 * - 29일: 보라 점(코스 있음 + 미선택)
 * - 하단 코스 섹션: 회색 배경 "오늘의 코스가 없어요"
 */
@Preview(showBackground = true, heightDp = 800)
@Composable
private fun CalendarScreenPreview1() {
    DayTodoTheme {
        CalendarScreen(
            initialSelectedDate = LocalDate.of(2026, 7, 18),
        )
    }
}

/**
 * 선택된 날짜(7/18)에 코스가 "있는" 상태.
 * - 18일: 보라 네모(선택 + 코스 있음) — 프리뷰 전용으로 7/18 코스 추가
 * - 16일: 점(코스 있음 + 미선택)
 * - 하단 코스 섹션: 보라 배경 + 흰색 코스 이름
 */
@Preview(showBackground = true, heightDp = 800)
@Composable
private fun CalendarScreenPreview2() {
    DayTodoTheme {
        CalendarScreen(
            initialSelectedDate = LocalDate.of(2026, 7, 18),
            coursesByDate = defaultCoursesByDate + (LocalDate.of(2026, 7, 18) to "프리뷰 코스"),
        )
    }
}
