package com.team_daytodo.daytodo.feature.mypage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team_daytodo.daytodo.domain.mypage.usecase.GetInterestRegionOptionsUseCase
import com.team_daytodo.daytodo.domain.mypage.usecase.GetInterestRegionsUseCase
import com.team_daytodo.daytodo.domain.mypage.usecase.UpdateInterestRegionsUseCase
import com.team_daytodo.daytodo.feature.mypage.state.InterestRegionEvent
import com.team_daytodo.daytodo.feature.mypage.state.InterestRegionGroup
import com.team_daytodo.daytodo.feature.mypage.state.InterestRegionUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class InterestRegionViewModel @Inject constructor(
    private val getInterestRegionOptionsUseCase: GetInterestRegionOptionsUseCase,
    private val getInterestRegionsUseCase: GetInterestRegionsUseCase,
    private val updateInterestRegionsUseCase: UpdateInterestRegionsUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(InterestRegionUiState(isLoading = true))
    val uiState: StateFlow<InterestRegionUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<InterestRegionEvent>()
    val event: SharedFlow<InterestRegionEvent> = _event.asSharedFlow()

    init {
        loadRegions()
    }

    fun onGroupSelected(parentName: String) {
        _uiState.update { it.copy(selectedGroupName = parentName) }
    }

    fun onRegionToggled(regionId: Long) {
        _uiState.update { current ->
            val updated = if (regionId in current.selectedRegionIds) {
                current.selectedRegionIds - regionId
            } else {
                current.selectedRegionIds + regionId
            }
            current.copy(selectedRegionIds = updated)
        }
    }

    fun onSaveClick() {
        val currentState = _uiState.value
        if (!currentState.canSave) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            updateInterestRegionsUseCase(currentState.selectedRegionIds.toList())
                .onSuccess {
                    _uiState.update { it.copy(isSaving = false) }
                    _event.emit(InterestRegionEvent.Saved)
                }
                .onFailure { cause ->
                    _uiState.update { it.copy(isSaving = false, errorMessage = cause.message) }
                }
        }
    }

    private fun loadRegions() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            getInterestRegionOptionsUseCase()
                .onSuccess { options ->
                    val groups = options
                        // 하위지역이 없는 항목(예: 하위 구가 없는 시/도)은 parentRegionName이
                        // null이라 자기 자신을 유일한 자식으로 갖는 그룹이 된다 — 좌/우 2단
                        // 레이아웃을 특수 분기 없이 통일해서 처리하기 위함.
                        .groupBy { (it.parentRegionName ?: it.regionName).toRegionParentDisplayName() }
                        .map { (parentName, groupOptions) ->
                            InterestRegionGroup(
                                parentName = parentName,
                                displayName = parentName,
                                options = groupOptions,
                            )
                        }

                    getInterestRegionsUseCase()
                        .onSuccess { serverRegions ->
                            // 서버가 돌려준 regionId가 현재 카탈로그에 없는 경우(예: 지역 개편으로
                            // 카탈로그에서 빠진 지역을 과거에 저장해둔 경우)를 방어한다. 매칭되는
                            // 것만 초기 선택으로 반영하고 나머지는 조용히 무시한다(크래시 없이).
                            val catalogIds = options.map { it.regionId }.toSet()
                            val matchedIds = serverRegions.map { it.regionId }.filter { it in catalogIds }.toSet()
                            val selectedGroupName = groups
                                .firstOrNull { group -> group.options.any { it.regionId in matchedIds } }
                                ?.parentName
                                ?: groups.firstOrNull()?.parentName.orEmpty()
                            _uiState.update {
                                it.copy(
                                    groups = groups,
                                    selectedGroupName = selectedGroupName,
                                    selectedRegionIds = matchedIds,
                                    isLoading = false,
                                )
                            }
                        }
                        .onFailure { cause ->
                            _uiState.update {
                                it.copy(
                                    groups = groups,
                                    selectedGroupName = groups.firstOrNull()?.parentName.orEmpty(),
                                    isLoading = false,
                                    errorMessage = cause.message,
                                )
                            }
                        }
                }
                .onFailure { cause ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = cause.message) }
                }
        }
    }
}

private fun String.toRegionParentDisplayName(): String = when (this) {
    "서울특별시" -> "서울"
    "부산광역시" -> "부산"
    "대구광역시" -> "대구"
    "인천광역시" -> "인천"
    "광주광역시" -> "광주"
    "대전광역시" -> "대전"
    "울산광역시" -> "울산"
    "세종특별자치시" -> "세종"
    "제주특별자치도" -> "제주"
    "강원특별자치도", "강원도" -> "강원"
    "경기도" -> "경기"
    "충청북도", "충청남도" -> "충청"
    "전북특별자치도", "전라북도", "전라남도" -> "전라"
    "경상북도", "경상남도" -> "경상"
    else -> this
}
