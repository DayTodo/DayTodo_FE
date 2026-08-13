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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

private val PhotoSize = 92.dp
private val AddBadgeSize = 23.dp
private val AddIconSize = 15.dp

private val AddBadgeOffset = 2.dp

@Composable
fun ProfilePhotoPicker(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    imageUri: String? = null,
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
        ) {
            // imageUri는 화면 진입 시 서버 프로필 사진 URL(https://...)이거나, 사진을 새로
            // 고른 직후에는 갤러리 content:// Uri다. 예전엔 ImageDecoder/BitmapFactory로
            // content:// Uri만 디코딩해서, 저장된 서버 사진은 이 화면에 아예 뜨지 않았다.
            // Coil의 AsyncImage는 두 형태 모두 그대로 로딩하므로 이 화면 안에서 하나로 통일한다.
            if (!imageUri.isNullOrBlank()) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = "선택한 프로필 사진",
                    modifier = Modifier.size(PhotoSize).clip(CircleShape),
                    contentScale = ContentScale.Crop,
                )
            }
        }
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
