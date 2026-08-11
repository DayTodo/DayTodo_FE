package com.team_daytodo.daytodo.feature.save.presentation.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.team_daytodo.daytodo.domain.bookmark.model.SavedPlaceSortType
import com.team_daytodo.daytodo.feature.save.model.displayName
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

@Composable
internal fun SaveSortDialog(
    selectedSortType: SavedPlaceSortType,
    onSortTypeClick: (SavedPlaceSortType) -> Unit,
    onDismissRequest: () -> Unit,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(279.dp),
            shape = RoundedCornerShape(12.dp),
            color = Color.White,
        ) {
            Column(
                modifier = Modifier.padding(
                    horizontal = 20.dp,
                    vertical = 27.5.dp,
                ),
            ) {
                Text(
                    text = "정렬",
                    style = DayTodoTheme.typography.label2,
                    color = Color(0xFF616166),
                    maxLines = 1,
                )
                Spacer(modifier = Modifier.height(20.dp))
                SortOptions.forEachIndexed { index, sortType ->
                    SortOptionRow(
                        sortType = sortType,
                        selected = selectedSortType == sortType,
                        onClick = { onSortTypeClick(sortType) },
                    )
                    if (index < SortOptions.lastIndex) {
                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider(
                            modifier = Modifier.fillMaxWidth(),
                            thickness = 1.dp,
                            color = Color(0xFFF3F3F3),
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun SortOptionRow(
    sortType: SavedPlaceSortType,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(37.dp)
            .clickable(role = Role.RadioButton, onClick = onClick)
            .padding(start = 13.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = sortType.displayName(),
            modifier = Modifier.weight(1f),
            style = DayTodoTheme.typography.label2,
            color = Color(0xFF616166),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        SortRadio(selected = selected)
    }
}

@Composable
private fun SortRadio(
    selected: Boolean,
    modifier: Modifier = Modifier,
) {
    val selectedColor = Color(0xFF8B8AF5)
    val borderColor = if (selected) selectedColor else Color(0xFFD9D9D9)

    Canvas(modifier = modifier.size(16.dp)) {
        drawCircle(
            color = borderColor,
            style = Stroke(width = 1.5.dp.toPx()),
        )
        if (selected) {
            drawCircle(
                color = selectedColor,
                radius = 4.dp.toPx(),
            )
        }
    }
}

private val SortOptions = listOf(
    SavedPlaceSortType.RecentSaved,
    SavedPlaceSortType.OldestSaved,
    SavedPlaceSortType.Name,
    SavedPlaceSortType.Popularity,
)
