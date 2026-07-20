package com.team_daytodo.daytodo.feature.course.presentation

import android.widget.Toast
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team_daytodo.daytodo.core.model.Relationship
import com.team_daytodo.daytodo.feature.course.R
import com.team_daytodo.daytodo.uikit.component.DayTodoNextStepButton
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

@Composable
fun CourseCreateCompleteScreen(
    inviteLink: String,
    relationship: Relationship,
    onDoneClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(horizontal = ScreenHorizontalPadding),
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
                color = FieldContentColor,
            )
            Spacer(modifier = Modifier.height(44.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, FieldBorderColor, RoundedCornerShape(12.dp))
                    .clickable(role = Role.Button) {
                        clipboardManager.setText(AnnotatedString(inviteLink))
                        Toast
                            .makeText(context, "초대 링크가 복사됐어요", Toast.LENGTH_SHORT)
                            .show()
                    }
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
                    color = FieldBorderColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.width(12.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ic_link),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = FieldBorderColor,
                )
            }
        }

        DayTodoNextStepButton(
            text = "돌아가기",
            enabled = true,
            onClick = onDoneClick,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(horizontal = ScreenHorizontalPadding)
                .padding(bottom = 99.dp),
        )
    }
}