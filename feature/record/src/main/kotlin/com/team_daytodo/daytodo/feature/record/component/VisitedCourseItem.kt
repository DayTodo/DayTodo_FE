package com.team_daytodo.daytodo.feature.record.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.feature.record.R
import com.team_daytodo.daytodo.feature.record.model.VisitedCourse
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

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
    var isSaved by remember { mutableStateOf(false) }

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
        IconButton(
            onClick = {
                isSaved = !isSaved
                onSaveClick(course)
            },
        ) {
            Icon(
                painter = painterResource(
                    id = if (isSaved) R.drawable.ic_fullbookmark else R.drawable.ic_emptybookmark,
                ),
                contentDescription = "장소 저장",
                // 북마크 SVG가 색을 자체 보유(채움: brandPrimary, 빈 상태: textPrimary 외곽선)하므로 tint로 덮어쓰지 않는다.
                tint = Color.Unspecified,
            )
        }
    }
}
