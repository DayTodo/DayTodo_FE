package com.team_daytodo.daytodo.feature.mypage.state

import com.team_daytodo.daytodo.domain.mypage.model.InterestRegionOption

data class InterestRegionUiState(
    val groups: List<InterestRegionGroup> = emptyList(),
    val selectedGroupName: String = "",
    val selectedRegionIds: Set<Long> = emptySet(),
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
) {
    val selectedGroup: InterestRegionGroup?
        get() = groups.firstOrNull { it.parentName == selectedGroupName }

    // BE는 관심지역 최소 1개 선택을 요구한다(@NotEmpty) — 0개면 저장 자체를 막는다.
    val canSave: Boolean
        get() = selectedRegionIds.isNotEmpty() && !isSaving
}

data class InterestRegionGroup(
    val parentName: String,
    val displayName: String = parentName,
    val options: List<InterestRegionOption>,
)

sealed interface InterestRegionEvent {
    data object Saved : InterestRegionEvent
}
