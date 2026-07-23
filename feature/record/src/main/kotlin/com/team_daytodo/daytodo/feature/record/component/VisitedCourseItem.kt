package com.team_daytodo.daytodo.feature.record.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

data class VisitedCourse(
    val id: String,
    val title: String,
)

/**
 * "다녀간 코스" 리스트 아이템.
 * 연한 회색(backgroundSecondary) 박스 안에 코스 제목 + 우측 장소 저장(북마크) 버튼.
 */
@Composable
fun VisitedCourseItem(
    course: VisitedCourse,
    onSaveClick: (VisitedCourse) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(color = DayTodoTheme.colors.backgroundSecondary)
            .padding(start = 16.dp, end = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = course.title,
            style = DayTodoTheme.typography.label3,
            color = DayTodoTheme.colors.textPrimary,
            modifier = Modifier.weight(1f),
        )
        // TODO: 장소 저장(북마크) 아이콘 SVG 필요. 프로젝트/Material core 에 북마크 아이콘이
        //  없어 임시로 Material Star 사용 — 실제 아이콘 확보 시 교체.
        IconButton(onClick = { onSaveClick(course) }) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "장소 저장",
                tint = DayTodoTheme.colors.iconDefault,
            )
        }
    }
}
