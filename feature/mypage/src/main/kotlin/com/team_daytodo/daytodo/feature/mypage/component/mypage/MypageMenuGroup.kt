package com.team_daytodo.daytodo.feature.mypage.component.mypage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

/**
 * "관리 / 고객센터 / 기타"처럼 반복되는 그룹 패턴:
 * title1 헤더 + 12dp 간격 + 하위 항목들(항목 간 12dp).
 * 하위 항목은 호출부에서 content 슬롯으로 구성한다.
 */
@Composable
fun MypageMenuGroup(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = DayTodoTheme.typography.title1,
            color = DayTodoTheme.colors.textPrimary,
        )
        Spacer(modifier = Modifier.height(12.dp))
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            content()
        }
    }
}
