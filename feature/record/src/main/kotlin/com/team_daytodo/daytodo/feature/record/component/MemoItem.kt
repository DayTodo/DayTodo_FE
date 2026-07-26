package com.team_daytodo.daytodo.feature.record.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.feature.record.model.MemoEntry
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

/**
 * 메모(댓글) 리스트 아이템. "작성하기" 입력 박스와 동일한 스타일(55dp 높이,
 * backgroundSecondary 배경, 좌측 16dp 여백)의 회색 네모 안에 "이름: 내용" 형식으로 표시한다.
 * 이름/내용 모두 textPrimary로 통일한다.
 */
@Composable
fun MemoItem(
    memo: MemoEntry,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(55.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(DayTodoTheme.colors.backgroundSecondary)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        Row {
            Text(
                text = "${memo.author}: ",
                style = DayTodoTheme.typography.label3,
                color = DayTodoTheme.colors.textPrimary,
            )
            Text(
                text = memo.content,
                style = DayTodoTheme.typography.label3,
                color = DayTodoTheme.colors.textPrimary,
            )
        }
    }
}
