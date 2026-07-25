package com.team_daytodo.daytodo.feature.mypage.component.profileedit

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

/** 이름 / 닉네임 / 이메일 / 연동 계정 상자가 공유하는 모서리 반경. */
internal val ProfileFieldShape = RoundedCornerShape(12.dp)

/** 기본 상자 높이(이름/닉네임). 이메일 상자만 호출부에서 54dp 로 덮어쓴다. */
private val DefaultFieldHeight = 41.dp

/**
 * "이름 / 닉네임 / 이메일" 처럼 label + 값 표시 상자 패턴 (label만 바꿔 재사용).
 * 상자는 입력 필드(TextField)가 아니라 DB 값 표시용 Box.
 * 내부 좌우 16dp는 표시 여백으로 가정함.
 *
 * filled = true 이면 이메일 스타일(연회색 배경 + 테두리 없음),
 * false 이면 기본 스타일(투명 배경 + brandPrimary 테두리)로 그린다.
 */
@Composable
fun ProfileInfoField(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    filled: Boolean = false,
    height: Dp = DefaultFieldHeight,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = DayTodoTheme.typography.title2,
            color = DayTodoTheme.colors.textPrimary,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .clip(ProfileFieldShape)
                .then(
                    if (filled) {
                        // gray100 에 해당하는 uikit 토큰이 backgroundSecondary(#F3F4F6).
                        Modifier.background(DayTodoTheme.colors.backgroundSecondary)
                    } else {
                        Modifier.border(
                            width = 1.dp,
                            color = DayTodoTheme.colors.brandPrimary,
                            shape = ProfileFieldShape,
                        )
                    },
                )
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart,
        ) {
            Text(
                text = value,
                style = DayTodoTheme.typography.caption1,
                // textPrimary(#616166) = 진한 회색.
                color = DayTodoTheme.colors.textPrimary,
            )
        }
    }
}
