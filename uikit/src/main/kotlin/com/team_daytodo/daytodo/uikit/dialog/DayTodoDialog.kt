package com.team_daytodo.daytodo.uikit.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

private val DialogCornerRadius = 12.dp

private val DialogMaxWidth = 320.dp

private val DialogOuterPadding = 20.dp

private val HeaderHeight = 52.dp

private val MessageHorizontalPadding = 16.dp

private val MessageTopPadding = 24.dp

private val MessageBottomPadding = 24.dp

private val CardToActionSpacing = 32.dp

private val ActionSpacing = 24.dp

private val ActionWidth = 115.dp
private val ActionHeight = 47.dp

@Composable
fun DayTodoAlertDialog(
    title: String,
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    confirmText: String = "확인",
    dismissText: String = "취소",
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
        ),
    ) {
        Column(
            modifier = modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            DayTodoDialogSurface {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(HeaderHeight)
                        .background(DayTodoTheme.colors.brandPrimary),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = title,
                        style = DayTodoTheme.typography.title2,
                        color = DayTodoTheme.colors.textQuaternary,
                    )
                }

                DayTodoDialogMessage(
                    message = message,
                    modifier = Modifier.padding(
                        top = MessageTopPadding,
                        bottom = MessageBottomPadding,
                    ),
                )
            }

            Spacer(modifier = Modifier.height(CardToActionSpacing))

            Row(horizontalArrangement = Arrangement.spacedBy(ActionSpacing)) {
                DialogActionButton(
                    text = dismissText,
                    backgroundColor = DayTodoTheme.colors.brandPrimary,
                    onClick = onDismiss,
                )
                DialogActionButton(
                    text = confirmText,
                    backgroundColor = DayTodoTheme.colors.iconDisabled,
                    onClick = onConfirm,
                )
            }
        }
    }
}

@Composable
fun DayTodoMessageDialog(
    message: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
        ),
    ) {
        DayTodoDialogSurface(modifier = modifier) {
            DayTodoDialogMessage(
                message = message,
                modifier = Modifier.padding(
                    top = MessageTopPadding,
                    bottom = MessageBottomPadding,
                ),
            )
        }
    }
}

@Composable
private fun DayTodoDialogSurface(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = DialogOuterPadding)
            .widthIn(max = DialogMaxWidth),
        shape = RoundedCornerShape(DialogCornerRadius),
        color = DayTodoTheme.colors.backgroundDefault,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            content()
        }
    }
}

@Composable
private fun DayTodoDialogMessage(
    message: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = message,
        style = DayTodoTheme.typography.body2,
        color = DayTodoTheme.colors.textPrimary,
        textAlign = TextAlign.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = MessageHorizontalPadding),
    )
}

@Composable
private fun DialogActionButton(
    text: String,
    backgroundColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(width = ActionWidth, height = ActionHeight)
            .clip(RoundedCornerShape(999.dp))
            .background(backgroundColor)
            .clickable(role = Role.Button, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = DayTodoTheme.typography.label2,
            color = DayTodoTheme.colors.textQuaternary,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DayTodoAlertDialogPreview() {
    DayTodoTheme {
        DayTodoAlertDialog(
            title = "주의",
            message = "탈퇴 시 모든 계정 정보와 이용 내역이\n영구적으로 삭제되며\n복구할 수 없습니다.",
            onConfirm = {},
            onDismiss = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DayTodoMessageDialogPreview() {
    DayTodoTheme {
        DayTodoMessageDialog(
            message = "안전하게 탈퇴되었습니다.\n다음에 다시 만나요!",
            onDismiss = {},
        )
    }
}
