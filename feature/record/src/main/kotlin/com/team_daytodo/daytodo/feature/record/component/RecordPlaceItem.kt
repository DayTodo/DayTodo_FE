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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.feature.record.R
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

/**
 * 선택된 코스 안의 개별 장소 행. 연한 회색(backgroundSecondary) 박스 안에 장소명 + 우측 장소 저장(북마크) 버튼.
 */
@Composable
fun RecordPlaceItem(
    placeName: String,
    isSaved: Boolean,
    onSaveClick: () -> Unit,
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
            text = placeName,
            style = DayTodoTheme.typography.label3,
            color = DayTodoTheme.colors.textPrimary,
            modifier = Modifier.weight(1f),
        )
        IconButton(onClick = onSaveClick) {
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
