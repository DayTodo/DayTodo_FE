package com.team_daytodo.daytodo.feature.course

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team_daytodo.daytodo.uikit.component.DayTodoHeaderSection
import com.team_daytodo.daytodo.uikit.component.DayTodoNextStepButton
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

private val ScreenHorizontalPadding = 20.dp

@Composable
fun InviteCodeJoinScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    onEnterClick: (String) -> Unit = {},
) {
    var inviteCode by rememberSaveable { mutableStateOf("") }
    val hasInviteCode = inviteCode.isNotBlank()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DayTodoTheme.colors.backgroundDefault)
            .statusBarsPadding(),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            DayTodoHeaderSection(
                title = "초대코드 입력하기",
                onBackClick = onBackClick,
                horizontalPadding = ScreenHorizontalPadding,
            )
            Spacer(modifier = Modifier.height(53.dp))
            InviteCodeTextField(
                value = inviteCode,
                onValueChange = { inviteCode = it },
                modifier = Modifier.padding(horizontal = ScreenHorizontalPadding),
            )
        }

        DayTodoNextStepButton(
            text = "입장하기",
            enabled = hasInviteCode,
            onClick = { onEnterClick(inviteCode) },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(horizontal = ScreenHorizontalPadding)
                .padding(bottom = 99.dp),
        )
    }
}

@Composable
private fun InviteCodeTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val inputTextStyle = DayTodoTheme.typography.label2.copy(
        lineHeight = 16.sp,
        letterSpacing = 0.sp,
        color = HeaderContentColor,
    )
    val placeholderTextStyle = inputTextStyle.copy(color = InviteFieldBorderColor)
    val selectionColors = remember {
        TextSelectionColors(
            handleColor = HeaderContentColor,
            backgroundColor = HeaderContentColor.copy(alpha = 0.25f),
        )
    }

    CompositionLocalProvider(LocalTextSelectionColors provides selectionColors) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(Color.White)
                .border(
                    width = 1.dp,
                    color = InviteFieldBorderColor,
                    shape = RoundedCornerShape(0.dp),
                ),
            singleLine = true,
            textStyle = inputTextStyle,
            cursorBrush = SolidColor(HeaderContentColor),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = "ex) daytodoapp.fofoeeopaff.dfmkem",
                            style = placeholderTextStyle,
                        )
                    }
                    innerTextField()
                }
            },
        )
    }
}

private val HeaderContentColor = Color(0xFF616166)
private val InviteFieldBorderColor = Color(0xFFC1C1C1)

@Preview
@Composable
fun PreviewInviteCodeJoinScreen() {
    InviteCodeJoinScreen(
        onBackClick = {},
        onEnterClick = {},
    )
}