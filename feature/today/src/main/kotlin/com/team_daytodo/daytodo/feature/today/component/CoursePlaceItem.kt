package com.team_daytodo.daytodo.feature.today.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.feature.today.R
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

data class CoursePlace(
    val id: String,
    val name: String,
    val category: String,
)

@Composable
fun CoursePlaceItem(
    order: Int,
    place: CoursePlace,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(7.dp),
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_drag_indicator),
            contentDescription = "항목 순서 변경",
        )

        Row(
            modifier = Modifier
                .weight(1f)
                .height(66.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(color = DayTodoTheme.colors.backgroundSecondary)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(color = DayTodoTheme.colors.iconOnColor, shape = CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = order.toString(),
                    style = DayTodoTheme.typography.label3,
                    color = DayTodoTheme.colors.brandPrimary,
                )
            }

            Column(
                // 숫자 박스 ↔ 이름 16dp, 이름 ↔ 삭제 아이콘 8dp
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp, end = 8.dp),
                verticalArrangement = Arrangement.spacedBy(1.dp),
            ) {
                Text(
                    text = place.name,
                    style = DayTodoTheme.typography.title2.copy(
                        platformStyle = PlatformTextStyle(includeFontPadding = false),
                        lineHeightStyle = LineHeightStyle(
                            alignment = LineHeightStyle.Alignment.Center,
                            trim = LineHeightStyle.Trim.Both
                        )
                    ),
                    color = DayTodoTheme.colors.textPrimary,
                )
                Text(
                    text = place.category,
                    style = DayTodoTheme.typography.label3.copy(
                        platformStyle = PlatformTextStyle(includeFontPadding = false),
                        lineHeightStyle = LineHeightStyle(
                            alignment = LineHeightStyle.Alignment.Center,
                            trim = LineHeightStyle.Trim.Both
                        )
                    ),
                    color = DayTodoTheme.colors.textSecondary,
                )
            }

            Icon(
                painter = painterResource(id = R.drawable.ic_delete),
                contentDescription = "삭제",
            )
        }
    }
}
