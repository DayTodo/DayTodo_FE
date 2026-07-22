package com.team_daytodo.daytodo.feature.today.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

data class CourseMember(
    val id: String,
    val name: String,
)

@Composable
fun CourseMemberRow(
    members: List<CourseMember>,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(members, key = { it.id }) { member ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                // 프로필 사진 연동 전까지 사용하는 단색 원 placeholder
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = DayTodoTheme.colors.backgroundTertiary,
                            shape = CircleShape,
                        ),
                )
                Text(
                    text = member.name,
                    style = DayTodoTheme.typography.caption2,
                    color = DayTodoTheme.colors.textPrimary,
                )
            }
        }
    }
}
