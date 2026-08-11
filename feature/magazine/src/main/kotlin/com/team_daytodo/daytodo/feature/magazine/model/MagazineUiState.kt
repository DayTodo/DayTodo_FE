package com.team_daytodo.daytodo.feature.magazine.model

import com.team_daytodo.daytodo.domain.magazine.model.MagazineDetail

data class MagazineUiState(
    val isLoading: Boolean = false,
    val detail: MagazineDetail? = null,
    val isSaved: Boolean = false,
    val bookmarkId: Long? = null,
    val errorMessage: String? = null,
)

sealed interface MagazineEvent {
    data class ShowMessage(val message: String) : MagazineEvent
}
