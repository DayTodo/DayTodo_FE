package com.team_daytodo.daytodo.feature.course.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team_daytodo.daytodo.core.model.Relationship
import com.team_daytodo.daytodo.domain.course.model.CourseDate
import com.team_daytodo.daytodo.domain.course.model.CourseRegionGroup
import com.team_daytodo.daytodo.feature.course.R
import com.team_daytodo.daytodo.feature.course.model.CourseCreatePhase
import com.team_daytodo.daytodo.feature.course.model.CourseCreateStep
import com.team_daytodo.daytodo.feature.course.model.CourseCreateUiState
import com.team_daytodo.daytodo.feature.course.presentation.defaults.CourseCreateDefaults.screenHorizontalPadding
import com.team_daytodo.daytodo.feature.course.presentation.component.BudgetTextField
import com.team_daytodo.daytodo.feature.course.presentation.component.CourseCreateProgressIndicator
import com.team_daytodo.daytodo.feature.course.presentation.component.CourseDatePickerDialog
import com.team_daytodo.daytodo.feature.course.presentation.component.CourseLocationDialog
import com.team_daytodo.daytodo.feature.course.presentation.component.CourseSelectionField
import com.team_daytodo.daytodo.feature.course.presentation.component.CourseTextField
import com.team_daytodo.daytodo.feature.course.presentation.component.InputStepLayout
import com.team_daytodo.daytodo.feature.course.presentation.component.RelationshipCard
import com.team_daytodo.daytodo.feature.course.presentation.component.todayCourseDate
import com.team_daytodo.daytodo.feature.course.presentation.defaults.CourseCreateDefaults.buttonBottomPadding
import com.team_daytodo.daytodo.feature.course.presentation.defaults.CourseCreateDefaults.fieldContentColor
import com.team_daytodo.daytodo.feature.course.presentation.defaults.CourseCreateDefaults.progressColor
import com.team_daytodo.daytodo.feature.course.presentation.defaults.CourseCreateDefaults.secondaryTextColor
import com.team_daytodo.daytodo.feature.course.presentation.defaults.CourseCreateDefaults.sundayColor
import com.team_daytodo.daytodo.uikit.component.DayTodoHeaderSection
import com.team_daytodo.daytodo.uikit.component.DayTodoNextStepButton
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

@Composable
fun CourseCreateScreen(
    uiState: CourseCreateUiState,
    onRoomNameChange: (String) -> Unit,
    onRegionSelected: (String) -> Unit,
    onDateSelected: (CourseDate) -> Unit,
    onMinBudgetChange: (String) -> Unit,
    onMaxBudgetChange: (String) -> Unit,
    onRelationshipSelected: (Relationship) -> Unit,
    onRetryRegionOptionsClick: () -> Unit,
    onPastDateClick: () -> Unit,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    onSubmitClick: () -> Unit,
    onDoneClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState.phase) {
        CourseCreatePhase.Input -> CourseCreateInputFlow(
            uiState = uiState,
            onRoomNameChange = onRoomNameChange,
            onRegionSelected = onRegionSelected,
            onDateSelected = onDateSelected,
            onMinBudgetChange = onMinBudgetChange,
            onMaxBudgetChange = onMaxBudgetChange,
            onRelationshipSelected = onRelationshipSelected,
            onRetryRegionOptionsClick = onRetryRegionOptionsClick,
            onPastDateClick = onPastDateClick,
            onBackClick = onBackClick,
            onNextClick = onNextClick,
            onSubmitClick = onSubmitClick,
            modifier = modifier,
        )

        CourseCreatePhase.Loading -> CourseCreateLoadingScreen(modifier = modifier)
        CourseCreatePhase.Complete -> CourseCreateCompleteScreen(
            inviteLink = uiState.inviteLink,
            relationship = uiState.completedRelationship ?: Relationship.FRIEND,
            onDoneClick = onDoneClick,
            modifier = modifier,
        )
    }
}

@Composable
private fun CourseCreateInputFlow(
    uiState: CourseCreateUiState,
    onRoomNameChange: (String) -> Unit,
    onRegionSelected: (String) -> Unit,
    onDateSelected: (CourseDate) -> Unit,
    onMinBudgetChange: (String) -> Unit,
    onMaxBudgetChange: (String) -> Unit,
    onRelationshipSelected: (Relationship) -> Unit,
    onRetryRegionOptionsClick: () -> Unit,
    onPastDateClick: () -> Unit,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    onSubmitClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DayTodoTheme.colors.backgroundDefault)
            .statusBarsPadding(),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            DayTodoHeaderSection(
                title = "코스 방 만들기",
                onBackClick = onBackClick,
                horizontalPadding = screenHorizontalPadding,
            )
            CourseCreateProgressIndicator(
                step = uiState.currentStep.progressStep,
                modifier = Modifier
                    .padding(horizontal = screenHorizontalPadding)
                    .padding(top = 48.dp),
            )

            when (uiState.currentStep) {
                CourseCreateStep.RoomName -> RoomNameStep(
                    roomName = uiState.roomName,
                    onRoomNameChange = onRoomNameChange,
                )

                CourseCreateStep.Region -> RegionStep(
                    regionOptions = uiState.regionOptions,
                    isRegionLoading = uiState.isRegionLoading,
                    isRegionLoadFailed = uiState.isRegionLoadFailed,
                    selectedRegion = uiState.selectedRegion,
                    onRegionSelected = onRegionSelected,
                    onRetryRegionOptionsClick = onRetryRegionOptionsClick,
                )

                CourseCreateStep.Date -> DateStep(
                    selectedDate = uiState.selectedDate,
                    onDateSelected = onDateSelected,
                    onPastDateClick = onPastDateClick,
                )

                CourseCreateStep.Budget -> BudgetStep(
                    minBudgetDigits = uiState.minBudgetDigits,
                    onMinBudgetChange = onMinBudgetChange,
                    maxBudgetDigits = uiState.maxBudgetDigits,
                    onMaxBudgetChange = onMaxBudgetChange,
                    errorMessage = uiState.budgetErrorMessage,
                )

                CourseCreateStep.Relationship -> RelationshipStep(
                    selectedRelationship = uiState.selectedRelationship,
                    onRelationshipSelected = onRelationshipSelected,
                )
            }
        }

        DayTodoNextStepButton(
            text = uiState.primaryButtonText,
            enabled = uiState.isPrimaryButtonEnabled,
            onClick = {
                if (uiState.currentStep == CourseCreateStep.Relationship) {
                    onSubmitClick()
                } else {
                    onNextClick()
                }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(horizontal = screenHorizontalPadding)
                .padding(bottom = buttonBottomPadding),
        )
    }
}

@Composable
private fun RoomNameStep(
    roomName: String,
    onRoomNameChange: (String) -> Unit,
) {
    InputStepLayout(title = "코스 방의\n이름을 지어 주세요") {
        CourseTextField(
            value = roomName,
            onValueChange = onRoomNameChange,
            placeholder = "ex) 벚꽃 나들이 코스",
        )
    }
}

@Composable
private fun RegionStep(
    regionOptions: List<CourseRegionGroup>,
    isRegionLoading: Boolean,
    isRegionLoadFailed: Boolean,
    selectedRegion: String,
    onRegionSelected: (String) -> Unit,
    onRetryRegionOptionsClick: () -> Unit,
) {
    var showLocationDialog by rememberSaveable { mutableStateOf(false) }

    InputStepLayout(title = "코스 지역을\n선택해주세요") {
        CourseSelectionField(
            text = selectedRegion,
            placeholder = "홍대/합정/마포/서대문/은평",
            enabled = regionOptions.isNotEmpty() && !isRegionLoading,
            onClick = { showLocationDialog = true },
        )
        RegionLoadStateMessage(
            isLoading = isRegionLoading,
            isFailed = isRegionLoadFailed,
            hasRegions = regionOptions.isNotEmpty(),
            onRetryClick = onRetryRegionOptionsClick,
        )
    }

    if (showLocationDialog) {
        CourseLocationDialog(
            regions = regionOptions,
            selectedRegion = selectedRegion,
            onRegionSelected = {
                onRegionSelected(it)
                showLocationDialog = false
            },
            onDismissRequest = { showLocationDialog = false },
        )
    }
}

@Composable
private fun RegionLoadStateMessage(
    isLoading: Boolean,
    isFailed: Boolean,
    hasRegions: Boolean,
    onRetryClick: () -> Unit,
) {
    val message = when {
        isLoading -> "지역 정보를 불러오는 중이에요."
        isFailed -> "지역 정보를 불러오지 못했어요."
        !hasRegions -> "선택 가능한 지역 정보가 없어요."
        else -> null
    } ?: return

    Spacer(modifier = Modifier.height(10.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = message,
            style = DayTodoTheme.typography.caption2.copy(letterSpacing = 0.sp),
            color = secondaryTextColor,
        )
        if (!isLoading) {
            Text(
                text = "다시 시도",
                modifier = Modifier.clickable(role = Role.Button, onClick = onRetryClick),
                style = DayTodoTheme.typography.caption2.copy(
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.sp,
                ),
                color = progressColor,
            )
        }
    }
}

@Composable
private fun DateStep(
    selectedDate: CourseDate?,
    onDateSelected: (CourseDate) -> Unit,
    onPastDateClick: () -> Unit,
) {
    var showDateDialog by rememberSaveable { mutableStateOf(false) }
    val today = remember { todayCourseDate() }

    InputStepLayout(title = "코스를 즐길 날짜를 선택해주세요") {
        CourseSelectionField(
            text = selectedDate?.displayText().orEmpty(),
            placeholder = "ex) ${today.displayText()}",
            onClick = { showDateDialog = true },
        )
    }

    if (showDateDialog) {
        CourseDatePickerDialog(
            initialDate = selectedDate ?: today,
            minimumDate = today,
            onDateSelected = {
                onDateSelected(it)
                showDateDialog = false
            },
            onPastDateClick = onPastDateClick,
            onDismissRequest = { showDateDialog = false },
        )
    }
}

@Composable
private fun BudgetStep(
    minBudgetDigits: String,
    onMinBudgetChange: (String) -> Unit,
    maxBudgetDigits: String,
    onMaxBudgetChange: (String) -> Unit,
    errorMessage: String?,
) {
    InputStepLayout(title = "코스 예산을\n설정해 주세요") {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BudgetTextField(
                value = minBudgetDigits,
                onValueChange = onMinBudgetChange,
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.width(24.dp))
            Icon(
                painter = painterResource(id = R.drawable.ic_tilde),
                contentDescription = null,
                tint = fieldContentColor,
                modifier = Modifier.size(16.dp),
            )
            Spacer(modifier = Modifier.width(24.dp))
            BudgetTextField(
                value = maxBudgetDigits,
                onValueChange = onMaxBudgetChange,
                modifier = Modifier.weight(1f),
            )
        }
        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = errorMessage,
                style = DayTodoTheme.typography.caption2.copy(letterSpacing = 0.sp),
                color = sundayColor,
            )
        }
    }
}

@Composable
private fun RelationshipStep(
    selectedRelationship: Relationship?,
    onRelationshipSelected: (Relationship) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = screenHorizontalPadding)
            .padding(top = 42.dp),
    ) {
        Text(
            text = "코스를 함께할\n관계를 선택해 주세요.",
            style = DayTodoTheme.typography.headlineLarge,
            color = fieldContentColor,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "*인원수에 따라 이동 동선, 예약 가능한 장소, 추천 활동이 달라져요.",
            style = DayTodoTheme.typography.caption2.copy(
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.sp,
            ),
            color = fieldContentColor,
        )
        Spacer(modifier = Modifier.height(28.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(9.dp),
        ) {
            Relationship.entries.forEach { relationship ->
                RelationshipCard(
                    relationship = relationship,
                    selected = selectedRelationship == relationship,
                    onClick = { onRelationshipSelected(relationship) },
                    modifier = Modifier
                        .weight(1f)
                        .height(RelationshipCardHeight),
                )
            }
        }
    }
}

private val RelationshipCardHeight = 178.dp

@Preview
@Composable
private fun CourseCreateScreenPreview() {
    CourseCreateScreen(
        uiState = CourseCreateUiState(),
        onRoomNameChange = {},
        onRegionSelected = {},
        onDateSelected = {},
        onMinBudgetChange = {},
        onMaxBudgetChange = {},
        onRelationshipSelected = {},
        onRetryRegionOptionsClick = {},
        onPastDateClick = {},
        onBackClick = {},
        onNextClick = {},
        onSubmitClick = {},
        onDoneClick = {},
    )
}
