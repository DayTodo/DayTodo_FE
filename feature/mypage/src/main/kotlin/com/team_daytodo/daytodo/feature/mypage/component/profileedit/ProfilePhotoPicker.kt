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

/** 뱃지가 원형 홀더와 살짝 겹치도록 바깥으로 밀어내는 양. */
private val AddBadgeOffset = 2.dp

/**
 * 원형 프로필 사진 placeholder + 우측 하단 플러스 뱃지.
 * 사진과 뱃지를 합친 영역 전체가 하나의 버튼으로 동작한다(뱃지가 원 밖으로 튀어나온
 * 부분까지 눌린다). 실제 사진 선택 동작은 호출부에서 onClick 으로 붙인다.
 * 실제 이미지 로딩은 API 연동 시 placeholder Box 를 교체해 붙인다.
 */
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
                // textPrimary(#616166) = 진한 회색.
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
