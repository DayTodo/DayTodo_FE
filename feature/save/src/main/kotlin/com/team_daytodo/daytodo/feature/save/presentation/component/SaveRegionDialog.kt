package com.team_daytodo.daytodo.feature.save.presentation.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.team_daytodo.daytodo.domain.region.model.Region
import com.team_daytodo.daytodo.domain.region.model.RegionLevel
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

@Composable
internal fun SaveRegionDialog(
    regions: List<Region>,
    selectedRegionId: Long?,
    onRegionClick: (Long?) -> Unit,
    onDismissRequest: () -> Unit,
) {
    val sidoRegions = regions.filter { it.regionLevel == RegionLevel.SIDO }
    val sigunguByParent = regions
        .filter { it.regionLevel == RegionLevel.SIGUNGU }
        .groupBy { it.parentRegionId }

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 480.dp),
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
                    text = "지역",
                    style = DayTodoTheme.typography.label2,
                    color = Color(0xFF616166),
                    maxLines = 1,
                )
                Spacer(modifier = Modifier.height(20.dp))
                LazyColumn {
                    item {
                        RegionOptionRow(
                            name = "전체 지역",
                            selected = selectedRegionId == null,
                            indent = false,
                            onClick = { onRegionClick(null) },
                        )
                        RegionDivider()
                    }
                    sidoRegions.forEach { sido ->
                        item {
                            RegionOptionRow(
                                name = sido.regionName,
                                selected = selectedRegionId == sido.regionId,
                                indent = false,
                                onClick = { onRegionClick(sido.regionId) },
                            )
                        }
                        items(sigunguByParent[sido.regionId].orEmpty()) { sigungu ->
                            RegionOptionRow(
                                name = sigungu.regionName,
                                selected = selectedRegionId == sigungu.regionId,
                                indent = true,
                                onClick = { onRegionClick(sigungu.regionId) },
                            )
                        }
                        item { RegionDivider() }
                    }
                }
            }
        }
    }
}

@Composable
private fun RegionDivider() {
    Spacer(modifier = Modifier.height(8.dp))
    HorizontalDivider(
        modifier = Modifier.fillMaxWidth(),
        thickness = 1.dp,
        color = Color(0xFFF3F3F3),
    )
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun RegionOptionRow(
    name: String,
    selected: Boolean,
    indent: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(37.dp)
            .clickable(role = Role.RadioButton, onClick = onClick)
            .padding(start = if (indent) 33.dp else 13.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = name,
            modifier = Modifier.weight(1f),
            style = DayTodoTheme.typography.label2,
            color = Color(0xFF616166),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        RegionRadio(selected = selected)
    }
}

@Composable
private fun RegionRadio(
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
