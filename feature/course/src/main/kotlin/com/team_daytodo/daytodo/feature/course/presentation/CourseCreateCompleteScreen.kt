package com.team_daytodo.daytodo.feature.course.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team_daytodo.daytodo.core.model.Relationship
import com.team_daytodo.daytodo.feature.course.R
import com.team_daytodo.daytodo.feature.course.presentation.defaults.CourseCreateDefaults.buttonBottomPadding
import com.team_daytodo.daytodo.feature.course.presentation.defaults.CourseCreateDefaults.fieldBorderColor
import com.team_daytodo.daytodo.feature.course.presentation.defaults.CourseCreateDefaults.fieldContentColor
import com.team_daytodo.daytodo.feature.course.presentation.defaults.CourseCreateDefaults.progressColor
import com.team_daytodo.daytodo.feature.course.presentation.defaults.CourseCreateDefaults.screenHorizontalPadding
import com.team_daytodo.daytodo.feature.course.presentation.defaults.relationshipColors
import com.team_daytodo.daytodo.uikit.component.DayTodoNextStepButton
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme
import kotlinx.coroutines.delay

@Composable
fun CourseCreateCompleteScreen(
    inviteLink: String,
    relationship: Relationship,
    onDoneClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val clipboardManager = LocalClipboardManager.current
    var copiedToastKey by remember { mutableIntStateOf(0) }
    var showCopiedToast by remember { mutableStateOf(false) }

    LaunchedEffect(copiedToastKey) {
        if (copiedToastKey == 0) return@LaunchedEffect

        showCopiedToast = true
        delay(CopiedToastDurationMillis)
        showCopiedToast = false
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = screenHorizontalPadding, vertical = 297.94.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_relationship),
                contentDescription = null,
                modifier = Modifier.size(width = 152.dp, height = 77.dp),
                tint = relationshipColors(relationship).icon,
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "그룹이 생성됐어요",
                style = DayTodoTheme.typography.headlineLarge,
                color = fieldContentColor,
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = screenHorizontalPadding)
                .padding(bottom = buttonBottomPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CopiedLinkToast(visible = showCopiedToast)
            Spacer(modifier = Modifier.height(16.dp))
            InviteLinkRow(
                inviteLink = inviteLink,
                onClick = {
                    clipboardManager.setText(AnnotatedString(inviteLink))
                    copiedToastKey += 1
                },
            )
            Spacer(modifier = Modifier.height(84.dp))
            DayTodoNextStepButton(
                text = "돌아가기",
                enabled = true,
                onClick = onDoneClick,
            )
        }
    }
}

@Composable
private fun CopiedLinkToast(
    visible: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.height(36.dp),
        contentAlignment = Alignment.Center,
    ) {
        if (visible) {
            Text(
                text = "링크가 복사되었어요",
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(CopiedToastBackgroundColor)
                    .padding(horizontal = 17.dp, vertical = 8.dp),
                style = DayTodoTheme.typography.caption2.copy(
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.sp,
                ),
                color = progressColor,
            )
        }
    }
}

@Composable
private fun InviteLinkRow(
    inviteLink: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, fieldBorderColor, RoundedCornerShape(12.dp))
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 11.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = inviteLink,
            modifier = Modifier.weight(1f),
            style = DayTodoTheme.typography.caption2.copy(
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.sp,
            ),
            color = fieldBorderColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.width(12.dp))
        Icon(
            painter = painterResource(id = R.drawable.ic_link),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = fieldBorderColor,
        )
    }
}

private const val CopiedToastDurationMillis = 1800L
private val CopiedToastBackgroundColor = Color(0xFFECECFF)

@Preview
@Composable
fun PreviewCourseCreateCompleteScreen() {
    CourseCreateCompleteScreen(
        inviteLink = "https://daytodo.page.link/test",
        relationship = Relationship.FRIEND,
        onDoneClick = {},
    )
}
