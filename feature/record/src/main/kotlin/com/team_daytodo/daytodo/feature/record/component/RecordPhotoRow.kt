package com.team_daytodo.daytodo.feature.record.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.team_daytodo.daytodo.domain.record.model.RecordPhoto
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

@Composable
fun RecordPhotoRow(
    photos: List<RecordPhoto>,
    onPhotoClick: (Int) -> Unit,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (photos.isEmpty()) {
        EmptyPhotoContent(modifier = modifier)
    } else {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            photos.take(3).forEachIndexed { index, photo ->
                PhotoThumbnail(
                    photo = photo,
                    onClick = { onPhotoClick(index) },
                )
            }
            Text(
                text = "사진 더보기",
                style = DayTodoTheme.typography.caption2,
                color = DayTodoTheme.colors.textSecondary,
                modifier = Modifier.clickable(onClick = onMoreClick),
            )
        }
    }
}
@Composable
private fun PhotoThumbnail(
    photo: RecordPhoto,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // imageUrl은 업로드 API가 없어 기기 로컬 갤러리 Uri가 그대로 저장된 값일 수 있어(TodayViewModel
    // 참고), 세션이 바뀌면 그 Uri의 임시 읽기 권한이 만료되어 Coil이 영영 로딩하지 못하는 경우가
    // 있다. 이때 AsyncImage는 실패해도 아무 것도 그리지 않아 회색 배경만 계속 보이므로, 실패
    // 상태를 따로 표시해 "로딩 중"과 "표시 불가"를 구분한다.
    var loadFailed by remember(photo.imageUrl) { mutableStateOf(false) }

    Box(
        modifier = modifier
            .size(84.dp)
            .background(color = DayTodoTheme.colors.backgroundSecondary)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        if (loadFailed) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "사진을 표시할 수 없어요",
                tint = DayTodoTheme.colors.iconDefault,
            )
        } else {
            AsyncImage(
                model = photo.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                onError = { loadFailed = true },
                modifier = Modifier.size(84.dp),
            )
        }
    }
}

@Composable
private fun EmptyPhotoContent(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(84.dp)
            .background(color = DayTodoTheme.colors.divider),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            tint = Color.White,
        )
    }
}
