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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.team_daytodo.daytodo.feature.today.model.CourseMember
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

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
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = DayTodoTheme.colors.backgroundTertiary,
                            shape = CircleShape,
                        ),
                ) {
                    // 멤버가 프로필 사진을 등록하지 않았으면 profileImageUrl이 null이라
                    // 위 단색 원이 placeholder로 그대로 보인다.
                    if (!member.profileImageUrl.isNullOrBlank()) {
                        AsyncImage(
                            model = member.profileImageUrl,
                            contentDescription = "${member.name} 프로필 사진",
                            modifier = Modifier.size(48.dp).clip(CircleShape),
                            contentScale = ContentScale.Crop,
                        )
                    }
                }
                Text(
                    text = member.name,
                    style = DayTodoTheme.typography.caption2,
                    color = DayTodoTheme.colors.textPrimary,
                )
            }
        }
    }
}
