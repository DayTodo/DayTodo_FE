package com.team_daytodo.daytodo.feature.calendar.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme
import java.time.DayOfWeek
import java.time.LocalDate

// TODO: DayTodoTheme.colors 에 빨강 토큰 추가 후 교체 (임시 하드코딩)
private val SundayColor = Color(0xFFFF5B00)

/**
 * 달력의 개별 날짜 셀.
 *
 * 셀 전체 크기는 가변(aspectRatio 1:1)이며, 그 안의 선택 표시 네모만
 * 24x24dp로 고정되어 화면 크기와 무관하게 동일한 물리적 크기를 유지한다.
 *
 * 4가지 상태 분기:
 * - 코스 없음 + 미선택      → 숫자만
 * - 코스 있음 + 미선택      → 숫자 + 하단 점
 * - 선택됨 + 코스 없음      → 회색 배경 네모(둥근 사각형)
 * - 선택됨 + 코스 있음      → 보라 배경 네모(둥근 사각형)
 */
@Composable
fun CalendarDayCell(
    day: CalendarDay,
    isSelected: Boolean,
    hasCourse: Boolean,
    onClick: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    val inMonth = day.position == DayPosition.MonthDate

    val backgroundColor = when {
        isSelected && hasCourse -> DayTodoTheme.colors.brandPrimary
        isSelected -> DayTodoTheme.colors.backgroundSecondary
        else -> Color.Transparent
    }

    val textColor = when {
        !inMonth -> DayTodoTheme.colors.textQuaternary
        isSelected && hasCourse -> DayTodoTheme.colors.iconOnColor
        day.date.dayOfWeek == DayOfWeek.SUNDAY -> SundayColor
        else -> DayTodoTheme.colors.textPrimary
    }

    val showDot = inMonth && hasCourse && !isSelected

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .fillMaxWidth()
            .clickable(enabled = inMonth) { onClick(day.date) },
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
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
                    text = day.date.dayOfMonth.toString(),
                    style = DayTodoTheme.typography.body2,
                    color = textColor,
                )
            }
            // 코스 점: 네모 바로 아래. 미표시 시에도 자리를 예약해
            // 셀마다 네모 세로 위치가 흔들리지 않게 함.
            Box(
                modifier = Modifier
                    .padding(top = 2.dp)
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(if (showDot) DayTodoTheme.colors.brandPrimary else Color.Transparent),
            )
        }
    }
}
