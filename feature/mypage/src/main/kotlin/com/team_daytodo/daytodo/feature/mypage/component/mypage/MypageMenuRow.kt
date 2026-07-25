package com.team_daytodo.daytodo.feature.mypage.component.mypage

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

/** 그룹 왼쪽 끝에서 행 내용까지의 여백. */
private val RowStartPadding = 24.dp

/** 화살표가 그룹 오른쪽 끝에서 떨어지는 거리. 화살표가 없는 행에는 적용하지 않는다. */
private val ArrowEndPadding = 20.dp

/** 선행 아이콘과 텍스트 사이 간격. */
private val LeadingIconSpacing = 16.dp

/**
 * 그룹 내 label2 메뉴 항목 한 줄.
 * 필요 시 텍스트 왼쪽에 선행 아이콘을, 오른쪽 끝에 > 화살표를 노출한다.
 */
@Composable
fun MypageMenuRow(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = DayTodoTheme.colors.textPrimary,
    @DrawableRes leadingIconRes: Int? = null,
    trailingArrow: Boolean = false,
    onClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(
                start = RowStartPadding,
                end = if (trailingArrow) ArrowEndPadding else 0.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (leadingIconRes != null) {
            Icon(
                painter = painterResource(id = leadingIconRes),
                contentDescription = null,
                // 에셋 자체 색(#616166)과 동일한 토큰으로 틴트해 디자인 색을 유지한다.
                tint = DayTodoTheme.colors.textPrimary,
            )
            Spacer(modifier = Modifier.width(LeadingIconSpacing))
        }
        Text(
            text = text,
            style = DayTodoTheme.typography.label2,
            color = color,
            modifier = Modifier.weight(1f),
        )
        if (trailingArrow) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = DayTodoTheme.colors.iconDefault,
            )
        }
    }
}
