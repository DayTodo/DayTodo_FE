package com.team_daytodo.daytodo.feature.save.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.domain.bookmark.model.Bookmark

@Composable
internal fun SavedPlaceGrid(
    places: List<Bookmark>,
    onPlaceClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    selectedPlaceIds: Set<String> = emptySet(),
    selectionMode: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(bottom = 24.dp),
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier,
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(
            items = places,
            key = Bookmark::bookmarkId,
        ) { place ->
            val placeId = place.magazineId.toString()
            SavedPlaceCard(
                place = place,
                selected = placeId in selectedPlaceIds,
                selectionMode = selectionMode,
                onClick = { onPlaceClick(placeId) },
            )
        }
    }
}
