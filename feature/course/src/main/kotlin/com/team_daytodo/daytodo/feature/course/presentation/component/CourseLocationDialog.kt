package com.team_daytodo.daytodo.feature.course.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.team_daytodo.daytodo.domain.course.model.CourseRegionGroup
import com.team_daytodo.daytodo.feature.course.R
import com.team_daytodo.daytodo.feature.course.presentation.defaults.CourseCreateDefaults.fieldContentColor
import com.team_daytodo.daytodo.feature.course.presentation.defaults.CourseCreateDefaults.parentRegionSelectedBackground
import com.team_daytodo.daytodo.feature.course.presentation.defaults.CourseCreateDefaults.progressColor
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

@Composable
fun CourseLocationDialog(
    regions: List<CourseRegionGroup>,
    selectedRegion: String,
    onRegionSelected: (String) -> Unit,
    onDismissRequest: () -> Unit,
) {
    if (regions.isEmpty()) {
        return
    }

    val initialGroup = regions.firstOrNull { group ->
        selectedRegion == group.name || selectedRegion in group.children
    } ?: regions.first()
    var selectedGroupName by remember { mutableStateOf(initialGroup.name) }
    val selectedGroup = regions.first { it.name == selectedGroupName }

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 372.dp)
                .heightIn(max = 560.dp),
            shape = RoundedCornerShape(12.dp),
            color = Color.White,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 18.dp),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 25.dp, bottom = 11.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_location),
                            contentDescription = null,
                            tint = FieldContentColor,
                            modifier = Modifier.size(24.dp),
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "지역 필터",
                            style = DayTodoTheme.typography.title1,
                            color = FieldContentColor,
                        )
                    }
                }
                HorizontalDivider(thickness = 1.dp, color = FieldContentColor)
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 440.dp)
                        .padding(horizontal = 20.dp),
                ) {
                    LazyColumn(
                        modifier = Modifier.width(70.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp),
                    ) {
                        items(regions, key = { it.name }) { region ->
                            RegionParentItem(
                                text = region.name,
                                selected = region.name == selectedGroupName,
                                onClick = {
                                    if (region.children.isEmpty()) {
                                        onRegionSelected(region.name)
                                    } else {
                                        selectedGroupName = region.name
                                    }
                                },
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        items(selectedGroup.children, key = { it }) { childRegion ->
                            RegionChildItem(
                                text = childRegion,
                                selected = childRegion == selectedRegion,
                                onClick = { onRegionSelected(childRegion) },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RegionParentItem(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .width(58.dp)
            .height(33.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (selected) ParentRegionSelectedBackground else Color.Transparent)
            .clickable(role = Role.Button, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = DayTodoTheme.typography.label3.copy(letterSpacing = 0.sp),
            color = if (selected) ProgressColor else FieldContentColor,
            maxLines = 1,
        )
    }
}

@Composable
private fun RegionChildItem(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = 15.dp, vertical = 13.dp),
    ) {
        Text(
            text = text,
            style = DayTodoTheme.typography.label3.copy(letterSpacing = 0.sp),
            color = if (selected) ProgressColor else FieldContentColor,
        )
    }
}