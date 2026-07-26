package com.team_daytodo.daytodo.feature.mypage.component.profileedit

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

private val PhotoSize = 92.dp
private val AddBadgeSize = 23.dp
private val AddIconSize = 15.dp

private val AddBadgeOffset = 2.dp

@Composable
fun ProfilePhotoPicker(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.clickable(
            role = Role.Button,
            onClick = onClick,
        ),
    ) {
        Box(
            modifier = Modifier
                .size(PhotoSize)
                .clip(CircleShape)
                .background(DayTodoTheme.colors.backgroundSecondary),
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = AddBadgeOffset, y = AddBadgeOffset)
                .size(AddBadgeSize)
                .clip(CircleShape)
                .background(DayTodoTheme.colors.textPrimary),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "프로필 사진 변경",
                tint = DayTodoTheme.colors.iconOnColor,
                modifier = Modifier.size(AddIconSize),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfilePhotoPickerPreview() {
    DayTodoTheme {
        ProfilePhotoPicker(onClick = {})
    }
}
