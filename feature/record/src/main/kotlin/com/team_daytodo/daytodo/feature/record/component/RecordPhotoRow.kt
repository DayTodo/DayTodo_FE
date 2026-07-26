package com.team_daytodo.daytodo.feature.record.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.feature.record.model.RecordPhoto
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
    val imageRes = photo.imageRes
    Box(
        modifier = modifier
            .size(84.dp)
            .background(color = DayTodoTheme.colors.backgroundSecondary)
            .clickable(onClick = onClick),
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
