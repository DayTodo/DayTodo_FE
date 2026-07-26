package com.team_daytodo.daytodo.feature.record.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

// TODO: DayTodoTheme.colors 에 빨강 토큰 추가 후 교체 (임시 하드코딩, 캘린더 화면과 동일 값)
private val SundayColor = Color(0xFFFF5B00)

// 일요일 시작 요일 순서
private val weekDays = listOf(
    DayOfWeek.SUNDAY,
    DayOfWeek.MONDAY,
    DayOfWeek.TUESDAY,
    DayOfWeek.WEDNESDAY,
    DayOfWeek.THURSDAY,
    DayOfWeek.FRIDAY,
    DayOfWeek.SATURDAY,
)

/**
 * 기록 화면용 월 캘린더 섹션.
 *
 * :feature:calendar 의 CalendarScreen/CalendarDayCell 과 동일한 렌더링 패턴을
 * 모듈 간 참조 없이 java.time 으로 다시 구현한 것.
 *
 * - 선택 표시: 24x24dp 고정 네모(캘린더 화면과 동일)
 * - 일요일 숫자/헤더는 빨강
 * - 코스가 있는 날짜만 클릭 가능, 코스 없는 날짜는 흐리게(alpha) 표시
 */
@Composable
fun RecordCalendarSection(
    yearMonth: YearMonth,
    selectedDate: LocalDate,
    courseDates: Set<LocalDate>,
    onDateClick: (LocalDate) -> Unit,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        MonthNavigationRow(
            month = yearMonth,
            onPreviousClick = onPreviousMonth,
            onNextClick = onNextMonth,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
        )

        WeekDayHeader()

        MonthGrid(
            yearMonth = yearMonth,
            selectedDate = selectedDate,
            courseDates = courseDates,
            onDateClick = onDateClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
        )
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
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier.fillMaxWidth()) {
        weekDays.forEach { dayOfWeek ->
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
private fun MonthGrid(
    yearMonth: YearMonth,
    selectedDate: LocalDate,
    courseDates: Set<LocalDate>,
    onDateClick: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    val daysInMonth = yearMonth.lengthOfMonth()
    // 일요일 시작 기준, 1일 앞에 비워둘 칸 수 (SUNDAY=7 -> 0 ... SATURDAY=6 -> 6)
    val leadingEmpty = yearMonth.atDay(1).dayOfWeek.value % 7
    val totalCells = leadingEmpty + daysInMonth
    val weeks = (totalCells + 6) / 7

    Column(modifier = modifier) {
        for (week in 0 until weeks) {
            Row(modifier = Modifier.fillMaxWidth()) {
                for (dayOfWeekIndex in 0 until 7) {
                    val dayNumber = week * 7 + dayOfWeekIndex - leadingEmpty + 1
                    if (dayNumber in 1..daysInMonth) {
                        val date = yearMonth.atDay(dayNumber)
                        RecordDayCell(
                            date = date,
                            isSelected = date == selectedDate,
                            hasCourse = date in courseDates,
                            onClick = onDateClick,
                            modifier = Modifier.weight(1f),
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f),
                        )
                    }
                }
            }
        }
    }
}

/**
 * 개별 날짜 셀. 캘린더 화면의 CalendarDayCell 과 동일한 24x24 네모 + 일요일 빨강 패턴.
 * 단, 기록 화면에서는 코스가 있는 날짜만 선택 가능하며, 코스 없는 날짜는 흐리게 표시한다.
 */
@Composable
private fun RecordDayCell(
    date: LocalDate,
    isSelected: Boolean,
    hasCourse: Boolean,
    onClick: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = if (isSelected) {
        DayTodoTheme.colors.brandPrimary
    } else {
        Color.Transparent
    }

    val textColor = when {
        isSelected -> DayTodoTheme.colors.iconOnColor
        date.dayOfWeek == DayOfWeek.SUNDAY -> SundayColor
        else -> DayTodoTheme.colors.textPrimary
    }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clickable(enabled = hasCourse) { onClick(date) },
        contentAlignment = Alignment.Center,
    ) {
        // 선택 표시 네모: 24x24dp 고정, 숫자를 그 안에 중앙 정렬
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(backgroundColor),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = date.dayOfMonth.toString(),
                style = DayTodoTheme.typography.body2,
                color = textColor,
            )
        }
    }
}
