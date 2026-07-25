package com.team_daytodo.daytodo.feature.mypage.component.profileedit

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

private val ButtonHeight = 58.dp

/** 스펙상 반경 999px. uikit DayTodoNextStepButton 과 동일한 표기를 따른다. */
private val ButtonShape = RoundedCornerShape(999.dp)

/**
 * 프로필 관리 하단 "저장하기" 버튼.
 * uikit DayTodoNextStepButton 과 같은 pill + label1 + 흰 텍스트 컨벤션을 따르되,
 * 높이가 58dp 고정 스펙이라(uikit 쪽은 vertical padding 기반) 별도 컴포저블로 둔다.
 * 색은 하드코딩 대신 brandPrimary / textQuaternary 토큰을 쓴다.
 */
@Composable
fun ProfileSaveButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(ButtonHeight)
            .clip(ButtonShape)
            .background(DayTodoTheme.colors.brandPrimary)
            .clickable(
                role = Role.Button,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = DayTodoTheme.typography.label1,
            color = DayTodoTheme.colors.textQuaternary,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileSaveButtonPreview() {
    DayTodoTheme {
        ProfileSaveButton(text = "저장하기", onClick = {})
    }
}
