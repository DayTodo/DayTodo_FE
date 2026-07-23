package com.team_daytodo.daytodo.feature.record.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.team_daytodo.daytodo.feature.record.model.MemoEntry
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

/**
 * 메모(댓글) 리스트 아이템. "이름: 내용" 형식으로 표시한다.
 * 이름은 강조(textPrimary), 내용은 보조(textSecondary) 색으로 구분한다.
 */
@Composable
fun MemoItem(
    memo: MemoEntry,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "${memo.author}: ",
            style = DayTodoTheme.typography.label3,
            color = DayTodoTheme.colors.textPrimary,
        )
        Text(
            text = memo.content,
            style = DayTodoTheme.typography.label3,
            color = DayTodoTheme.colors.textSecondary,
        )
    }
}
