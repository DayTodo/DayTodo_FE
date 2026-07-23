package com.team_daytodo.daytodo.feature.record.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

data class RecordPhoto(
    val id: String,
    // 서버/DB 이미지로 교체될 자리. 지금은 dummypicture 리소스, 나중엔 URL 기반 로더로 대체 가능.
    val imageRes: Int? = null,
)

/**
 * 선택된 날짜의 추억 사진 미리보기.
 *
 * - 사진이 있으면 최대 3장 + 같은 행에 "사진 더보기" 텍스트
 * - 사진이 없으면 회색 네모(+ 흰색 플러스) 빈 상태 영역
 */
@Composable
fun RecordPhotoRow(
    photos: List<RecordPhoto>,
    onPhotoClick: (RecordPhoto) -> Unit,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (photos.isEmpty()) {
        EmptyPhotoState(modifier = modifier)
    } else {
        Row(
            modifier = modifier,
            // 사진 더보기 텍스트를 행의 바닥에 붙인다. 사진 썸네일(84dp)이 행 높이를 결정하므로
            // 텍스트가 행 높이 밖으로 나가지 않는다.
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            photos.take(3).forEach { photo ->
                PhotoThumbnail(
                    photo = photo,
                    onClick = onPhotoClick,
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

/**
 * 사진 한 장을 표시하는 재사용 가능한 썸네일.
 * [RecordPhoto.imageRes] 가 있으면 실제 이미지를, 없으면 회색 빈 상태 박스를 보여준다.
 * 나중에 서버/DB 이미지로 교체될 때도 이 컴포넌트의 파라미터만 바뀌면 된다.
 */
@Composable
private fun PhotoThumbnail(
    photo: RecordPhoto,
    onClick: (RecordPhoto) -> Unit,
    modifier: Modifier = Modifier,
) {
    val imageRes = photo.imageRes
    Box(
        modifier = modifier
            .size(84.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(color = DayTodoTheme.colors.backgroundSecondary)
            .clickable { onClick(photo) },
        contentAlignment = Alignment.Center,
    ) {
        if (imageRes != null) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
private fun EmptyPhotoState(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(84.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(color = DayTodoTheme.colors.backgroundSecondary),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            tint = Color.White,
        )
    }
}
