package com.team_daytodo.daytodo.feature.home.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.feature.home.model.HomeMagazineUiModel
import com.team_daytodo.daytodo.feature.home.presentation.component.MagazineCard
import com.team_daytodo.daytodo.uikit.component.DayTodoSectionTitle

@Composable
internal fun MagazineSection(
    magazines: List<HomeMagazineUiModel>,
    onMagazineClick: (HomeMagazineUiModel) -> Unit,
) {
    Column {
        DayTodoSectionTitle(
            modifier = Modifier.padding(horizontal = 20.dp),
            title = "오늘의 Pick! 매거진",
        )
        Spacer(modifier = Modifier.height(8.21.dp))
        magazines.forEach { magazine ->
            MagazineCard(
                magazine = magazine,
                onClick = { onMagazineClick(magazine) },
            )
        }
    }
}
