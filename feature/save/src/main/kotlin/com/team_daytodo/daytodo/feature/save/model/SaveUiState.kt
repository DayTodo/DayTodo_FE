package com.team_daytodo.daytodo.feature.save.model

import com.team_daytodo.daytodo.domain.magazine.model.MagazinePlace
import com.team_daytodo.daytodo.domain.magazine.model.SavedPlaceSortType

data class SaveUiState(
    val isLoading: Boolean = false,
    val places: List<MagazinePlace> = emptyList(),
    val sortType: SavedPlaceSortType = SavedPlaceSortType.RecentSaved,
    val isSortDialogVisible: Boolean = false,
    val errorMessage: String? = null,
)

sealed interface SaveEvent {
    data class ShowMessage(val message: String) : SaveEvent
}

data class SavedPlacePickerUiState(
    val isLoading: Boolean = false,
    val isImporting: Boolean = false,
    val places: List<MagazinePlace> = emptyList(),
    val selectedPlaceIds: Set<String> = emptySet(),
    val sortType: SavedPlaceSortType = SavedPlaceSortType.RecentSaved,
    val isSortDialogVisible: Boolean = false,
    val errorMessage: String? = null,
) {
    val canImport: Boolean
        get() = selectedPlaceIds.isNotEmpty() && !isImporting
}

sealed interface SavedPlacePickerEvent {
    data class ShowMessage(val message: String) : SavedPlacePickerEvent
    data object Imported : SavedPlacePickerEvent
}

fun SavedPlaceSortType.displayName(): String =
    when (this) {
        SavedPlaceSortType.RecentSaved -> "최근 저장순"
        SavedPlaceSortType.OldestSaved -> "오래된 저장순"
        SavedPlaceSortType.Name -> "가나다순"
        SavedPlaceSortType.Popularity -> "인기순"
    }
