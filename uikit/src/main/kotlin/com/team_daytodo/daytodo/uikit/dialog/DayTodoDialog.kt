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

/** 다이얼로그 카드 모서리 반경. 프로젝트의 기존 다이얼로그(CourseLocationDialog)와 동일하게 맞춘다. */
private val DialogCornerRadius = 12.dp

/** 다이얼로그 최대 폭. 태블릿 등 넓은 화면에서 과하게 늘어나지 않도록 제한한다. */
private val DialogMaxWidth = 320.dp

/** 좌우 화면 가장자리에서 다이얼로그가 떨어지는 거리. */
private val DialogOuterPadding = 20.dp

/** 보라색 헤더 밴드 높이. */
private val HeaderHeight = 52.dp

/** 본문 텍스트 좌우 여백. 본문 한 줄이 의도치 않게 접히지 않도록 좁게 잡는다. */
private val MessageHorizontalPadding = 16.dp

/** 헤더(또는 카드 상단) ↔ 본문 여백. */
private val MessageTopPadding = 24.dp

/** 본문 ↔ 카드 아래쪽 끝 여백. 본문 위 여백과 맞춰 카드 안에서 세로 균형을 잡는다. */
private val MessageBottomPadding = 24.dp

/** 안내 카드 ↔ 취소/확인 버튼 행 여백. 둘은 서로 분리된 요소다. */
private val CardToActionSpacing = 32.dp

/** 취소/확인 버튼 사이 간격. */
private val ActionSpacing = 24.dp

private val ActionWidth = 115.dp
private val ActionHeight = 47.dp

/**
 * 경고/확인용 공통 다이얼로그.
 *
 * "주의" 안내 카드(brandPrimary 헤더 밴드 + 가운데 정렬 본문)와 취소/확인 알약 버튼 행이
 * 서로 분리된 두 덩어리로 놓이고, 그 사이가 [CardToActionSpacing] 만큼 떠 있다.
 * 사용자가 반드시 둘 중 하나를 고르도록 바깥 터치와 뒤로가기로는 닫히지 않는다.
 *
 * @param title 헤더 밴드에 표시할 문구. 예: "주의"
 * @param message 본문. 줄바꿈이 필요하면 문자열에 직접 \n 을 넣는다.
 */
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

/**
 * 버튼 없이 안내 문구만 보여주는 공통 다이얼로그.
 *
 * 작업 완료를 알리는 용도라 별도 액션이 없고, 바깥 터치나 뒤로가기로 닫는다.
 * 닫힘 이후 처리(화면 이동 등)는 [onDismiss] 를 받는 쪽에서 한다.
 */
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

/** 두 다이얼로그가 공유하는 카드 배경. 모서리 반경과 폭 제약을 한곳에서 관리한다. */
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

/** 두 다이얼로그가 공유하는 본문 문구 영역. */
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
            // 기존 DayTodoNextStepButton 과 같은 알약 형태로 맞춘다.
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
