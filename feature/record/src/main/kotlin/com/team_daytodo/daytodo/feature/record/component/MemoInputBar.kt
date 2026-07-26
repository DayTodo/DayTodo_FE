package com.team_daytodo.daytodo.feature.record.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

/**
 * 하단 고정 메모 입력창.
 *
 * - "작성하기" placeholder 를 가진 알약형 입력 필드 + 우측 전송 버튼
 * - 입력 필드 영역 클릭 시 [focusRequester] 로 포커스를 이동하고 키보드를 표시
 * - 키보드의 Send 액션 또는 전송 버튼으로 [onSubmit] 호출
 *
 * imePadding 등 키보드 대응은 이 컴포저블을 배치하는 화면(Modifier)에서 처리한다.
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MemoInputBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSubmit: () -> Unit,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val canSubmit = value.isNotBlank()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(DayTodoTheme.colors.backgroundDefault)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(55.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(DayTodoTheme.colors.backgroundSecondary)
                .clickable {
                    focusRequester.requestFocus()
                    keyboardController?.show()
                }
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart,
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle = DayTodoTheme.typography.label3.copy(
                    color = DayTodoTheme.colors.textPrimary,
                ),
                cursorBrush = SolidColor(DayTodoTheme.colors.brandPrimary),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = { onSubmit() }),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                decorationBox = { innerTextField ->
                    if (value.isEmpty()) {
                        Text(
                            text = "작성하기",
                            style = DayTodoTheme.typography.label3,
                            color = DayTodoTheme.colors.textTertiary,
                        )
                    }
                    innerTextField()
                },
            )
        }

        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(
                    if (canSubmit) {
                        DayTodoTheme.colors.brandPrimary
                    } else {
                        DayTodoTheme.colors.backgroundTertiary
                    },
                )
                .clickable(enabled = canSubmit, onClick = onSubmit),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = "메모 전송",
                tint = DayTodoTheme.colors.iconOnColor,
            )
        }
    }
}
