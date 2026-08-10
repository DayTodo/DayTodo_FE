package com.team_daytodo.daytodo.feature.record.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

/**
 * 하루에 코스가 여러 개일 때 고르는 "코스 선택" 행. 저장(북마크)과는 무관하며,
 * 연한 회색(backgroundSecondary) 박스 안에 코스 제목만 표시하고 누르면 코스를 선택한다.
 */
@Composable
fun VisitedCourseItem(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(color = DayTodoTheme.colors.backgroundSecondary)
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            style = DayTodoTheme.typography.label3,
            color = DayTodoTheme.colors.textPrimary,
        )
    }
}
