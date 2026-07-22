package com.team_daytodo.daytodo.feature.today.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

data class MemoryPhoto(
    val id: String,
    val selectedOrder: Int? = null,
)

@Composable
fun MemorySaveScreen(
    photos: List<MemoryPhoto>,
    onAddPhotoClick: () -> Unit = {},
    onSaveClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val selectedCount = photos.count { it.selectedOrder != null }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DayTodoTheme.colors.backgroundDefault)
            .padding(horizontal = 20.dp),
    ) {
        Column(modifier = Modifier.padding(vertical = 16.dp)) {
            Text(
                text = "추억 저장하기",
                style = DayTodoTheme.typography.title1,
                color = DayTodoTheme.colors.textPrimary,
            )
            Text(
                text = "${selectedCount}장 선택됨",
                style = DayTodoTheme.typography.caption1,
                color = DayTodoTheme.colors.textSecondary,
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(photos, key = { it.id }) { photo ->
                MemoryPhotoCell(photo = photo)
            }
            item {
                AddPhotoCell(onClick = onAddPhotoClick)
            }
        }

        Button(
            onClick = onSaveClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
        ) {
            Text(text = "저장하기")
        }
    }
}

@Composable
private fun MemoryPhotoCell(
    photo: MemoryPhoto,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .background(
                color = DayTodoTheme.colors.backgroundSecondary,
                shape = RoundedCornerShape(8.dp),
            ),
    ) {
        if (photo.selectedOrder != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
                    .size(20.dp)
                    .background(
                        color = DayTodoTheme.colors.brandPrimary,
                        shape = CircleShape,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = photo.selectedOrder.toString(),
                    style = DayTodoTheme.typography.caption2,
                    color = Color.White,
                )
            }
        }
    }
}

@Composable
private fun AddPhotoCell(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .background(
                color = DayTodoTheme.colors.backgroundTertiary,
                shape = RoundedCornerShape(8.dp),
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "+",
            style = DayTodoTheme.typography.title1,
            color = DayTodoTheme.colors.iconDefault,
        )
    }
}

@Preview(showBackground = true, heightDp = 800)
@Composable
private fun MemorySaveScreenPreview() {
    DayTodoTheme {
        MemorySaveScreen(
            photos = listOf(
                MemoryPhoto(id = "1", selectedOrder = 1),
                MemoryPhoto(id = "2", selectedOrder = 2),
                MemoryPhoto(id = "3"),
                MemoryPhoto(id = "4"),
                MemoryPhoto(id = "5"),
            ),
        )
    }
}
