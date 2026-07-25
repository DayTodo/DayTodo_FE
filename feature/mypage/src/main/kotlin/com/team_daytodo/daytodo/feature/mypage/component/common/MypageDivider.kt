package com.team_daytodo.daytodo.feature.mypage.component.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

/**
 * 투데이 상단바 아래 경계선과 동일 스펙(두께 1dp, 좌우 여백 없이 전체 폭).
 * 투데이는 색을 Color(0xFFE0E0E0)로 하드코딩했으나, 하드코딩 금지 원칙에 따라
 * uikit divider 토큰(#D9D9D9)을 사용하기로 협의됨.
 * 상단바와 본문 구분선에 공통으로 재사용한다.
 */
@Composable
fun MypageDivider(modifier: Modifier = Modifier) {
    HorizontalDivider(
        modifier = modifier.fillMaxWidth(),
        thickness = 1.dp,
        color = DayTodoTheme.colors.divider,
    )
}
