package com.team_daytodo.daytodo.feature.mypage.component.profileedit

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Box
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

/**
 * 회색 pill 배경의 작은 텍스트 버튼("변경하기", "연동 해제").
 * 스펙상 반경 999px = 높이의 절반이므로 RoundedCornerShape(percent = 50)로 표현한다.
 * 내부 패딩은 스펙에 없어 기본값(12x6dp)을 가정함.
 */
@Composable
fun ProfileGreyButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(percent = 50))
            .background(DayTodoTheme.colors.backgroundSecondary)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = DayTodoTheme.typography.label3,
            // 스펙 #616161 → 팔레트 최근접 토큰 textPrimary(#616166).
            color = DayTodoTheme.colors.textPrimary,
        )
    }
}
