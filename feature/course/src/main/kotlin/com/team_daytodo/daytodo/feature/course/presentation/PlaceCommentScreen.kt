package com.team_daytodo.daytodo.feature.course.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team_daytodo.daytodo.domain.course.model.CourseComment
import com.team_daytodo.daytodo.feature.course.model.PlaceCommentUiState
import com.team_daytodo.daytodo.uikit.component.DayTodoHeaderSection
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme
import com.team_daytodo.daytodo.uikit.R as UIKitR

@Composable
fun PlaceCommentScreen(
    uiState: PlaceCommentUiState,
    onBackClick: () -> Unit,
    onInputChange: (String) -> Unit,
    onSubmitClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DayTodoTheme.colors.backgroundDefault),
    ) {
        DayTodoHeaderSection(
            title = "댓글",
            onBackClick = onBackClick,
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Spacer(modifier = Modifier.height(32.dp))
                uiState.place?.let { place ->
                    Text(
                        text = place.name,
                        style = DayTodoTheme.typography.headlineLarge.copy(letterSpacing = 0.sp),
                        color = DayTodoTheme.colors.textPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${place.address}, ${place.category}",
                        style = DayTodoTheme.typography.title2.copy(letterSpacing = 0.sp),
                        color = Color(0xFFB5B5B5),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Spacer(modifier = Modifier.height(36.dp))
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(bottom = 112.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(
                        items = uiState.comments,
                        key = CourseComment::id,
                    ) { comment ->
                        CommentItem(comment = comment)
                    }
                }
            }

            CommentInputBar(
                value = uiState.input,
                enabled = uiState.canSubmit,
                onValueChange = onInputChange,
                onSubmitClick = onSubmitClick,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .imePadding()
                    .padding(bottom = 48.dp),
            )
        }
    }
}

@Composable
private fun CommentItem(
    comment: CourseComment,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 72.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFC4C7C5)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 9.dp, vertical = 12.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp),
                verticalAlignment = Alignment.Top,
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE7E7FF)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = painterResource(id = UIKitR.drawable.ic_default_profile),
                        contentDescription = "${comment.author.name} 프로필 이미지",
                        tint = DayTodoTheme.colors.brandPrimary,
                        modifier = Modifier.size(28.dp),
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = comment.author.name,
                        style = DayTodoTheme.typography.body2.copy(letterSpacing = 0.sp),
                        color = DayTodoTheme.colors.brandPrimary,
                        maxLines = 1,
                    )
                    Text(
                        text = comment.content,
                        style = DayTodoTheme.typography.body2.copy(letterSpacing = 0.sp),
                        color = DayTodoTheme.colors.textPrimary,
                    )
                }
            }
            Text(
                text = comment.createdAtMillis.relativeTimeText(),
                modifier = Modifier.align(Alignment.End),
                style = DayTodoTheme.typography.caption2.copy(letterSpacing = 0.sp),
                color = Color(0xFFC6C6C6),
            )
        }
    }
}

@Composable
private fun CommentInputBar(
    value: String,
    enabled: Boolean,
    onValueChange: (String) -> Unit,
    onSubmitClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .weight(1f)
                .height(38.dp)
                .clip(RoundedCornerShape(33.dp))
                .background(Color.White)
                .border(1.dp, Color(0xFFC1C1C1), RoundedCornerShape(33.dp)),
            singleLine = true,
            textStyle = DayTodoTheme.typography.caption1.copy(
                color = DayTodoTheme.colors.textPrimary,
                letterSpacing = 0.sp,
            ),
            cursorBrush = SolidColor(DayTodoTheme.colors.brandPrimary),
            keyboardOptions = KeyboardOptions(
                imeAction = androidx.compose.ui.text.input.ImeAction.Send,
            ),
            keyboardActions = KeyboardActions(
                onSend = {
                    if (enabled) onSubmitClick()
                },
            ),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    if (value.isBlank()) {
                        Text(
                            text = "댓글을 입력해 주세요",
                            style = DayTodoTheme.typography.caption1.copy(letterSpacing = 0.sp),
                            color = DayTodoTheme.colors.textSecondary,
                        )
                    }
                    innerTextField()
                }
            },
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = "등록",
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable(
                    enabled = enabled,
                    role = Role.Button,
                    onClick = onSubmitClick,
                )
                .padding(5.dp),
            style = DayTodoTheme.typography.label2.copy(letterSpacing = 0.sp),
            color = if (enabled) DayTodoTheme.colors.brandPrimary else DayTodoTheme.colors.textSecondary,
        )
    }
}

private fun Long.relativeTimeText(): String {
    val diffMinutes = ((System.currentTimeMillis() - this) / 60_000L).coerceAtLeast(0L)
    return when {
        diffMinutes < 1L -> "방금"
        diffMinutes < 60L -> "${diffMinutes}분 전"
        diffMinutes < 60L * 24L -> "${diffMinutes / 60L}시간 전"
        else -> "${diffMinutes / (60L * 24L)}일 전"
    }
}
