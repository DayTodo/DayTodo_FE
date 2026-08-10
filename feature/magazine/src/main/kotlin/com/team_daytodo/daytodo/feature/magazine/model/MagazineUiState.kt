package com.team_daytodo.daytodo.feature.magazine.model

import com.team_daytodo.daytodo.domain.magazine.model.MagazinePlace

data class MagazineUiState(
    val isLoading: Boolean = false,
    val place: MagazinePlace? = null,
    val errorMessage: String? = null,
)

sealed interface MagazineEvent {
    data class ShowMessage(val message: String) : MagazineEvent
}
