package com.team_daytodo.daytodo.feature.course.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team_daytodo.daytodo.domain.course.model.CourseDate
import com.team_daytodo.daytodo.feature.course.R
import com.team_daytodo.daytodo.feature.course.model.CourseEditUiState
import com.team_daytodo.daytodo.feature.course.model.displayEditText
import com.team_daytodo.daytodo.feature.course.presentation.component.CourseBudgetRangeFields
import com.team_daytodo.daytodo.feature.course.presentation.component.CourseDatePickerDialog
import com.team_daytodo.daytodo.feature.course.presentation.component.CourseLocationDialog
import com.team_daytodo.daytodo.feature.course.presentation.component.todayCourseDate
import com.team_daytodo.daytodo.uikit.component.DayTodoHeaderSection
import com.team_daytodo.daytodo.uikit.component.DayTodoNextStepButton
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme
import com.team_daytodo.daytodo.uikit.R as UIKitR

@Composable
fun CourseEditScreen(
    uiState: CourseEditUiState,
    onNameChange: (String) -> Unit,
    onRegionSelected: (String) -> Unit,
    onDateSelected: (CourseDate) -> Unit,
    onMinBudgetChange: (String) -> Unit,
    onMaxBudgetChange: (String) -> Unit,
    onRetryRegionOptionsClick: () -> Unit,
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
    onSubmitClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showLocationDialog by rememberSaveable { mutableStateOf(false) }
    var showDateDialog by rememberSaveable { mutableStateOf(false) }
    val today = androidx.compose.runtime.remember { todayCourseDate() }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DayTodoTheme.colors.backgroundDefault),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            DayTodoHeaderSection(
                title = "코스설정 수정",
                onBackClick = onBackClick,
                rightContent = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_share),
                        contentDescription = "코스 공유",
                        tint = DayTodoTheme.colors.textPrimary,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable(role = Role.Button, onClick = onShareClick),
                    )
                },
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
                    .padding(top = 24.dp, bottom = 190.dp),
            ) {
                CourseEditInputSection(label = "코스 이름") {
                    CourseEditField(
                        value = uiState.name,
                        placeholder = "코스 이름",
                        onValueChange = onNameChange,
                    )
                }
                Spacer(modifier = Modifier.height(40.dp))
                CourseEditInputSection(label = "지역") {
                    CourseEditField(
                        value = uiState.selectedRegion,
                        placeholder = "지역을 선택해 주세요",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = UIKitR.drawable.ic_next,
                        onClick = {
                            if (uiState.regionOptions.isNotEmpty()) {
                                showLocationDialog = true
                            } else {
                                onRetryRegionOptionsClick()
                            }
                        },
                    )
                    RegionLoadMessage(uiState = uiState)
                }
                Spacer(modifier = Modifier.height(40.dp))
                CourseEditInputSection(label = "날짜") {
                    CourseEditField(
                        value = uiState.selectedDate?.displayEditText().orEmpty(),
                        placeholder = "날짜를 선택해 주세요",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = R.drawable.ic_course_edit_calendar,
                        onClick = { showDateDialog = true },
                    )
                }
                Spacer(modifier = Modifier.height(40.dp))
                CourseEditInputSection(label = "장소 가격대 범위") {
                    CourseBudgetRangeFields(
                        minBudgetDigits = uiState.minBudgetDigits,
                        onMinBudgetChange = onMinBudgetChange,
                        maxBudgetDigits = uiState.maxBudgetDigits,
                        onMaxBudgetChange = onMaxBudgetChange,
                        errorMessage = uiState.budgetErrorMessage,
                    )
                }
            }
        }

        DayTodoNextStepButton(
            text = if (uiState.isSaving) "수정 중..." else "수정하기",
            enabled = uiState.canSubmit,
            onClick = onSubmitClick,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 20.dp)
                .padding(bottom = 99.dp),
        )
    }

    if (showLocationDialog) {
        CourseLocationDialog(
            regions = uiState.regionOptions,
            selectedRegion = uiState.selectedRegion,
            onRegionSelected = {
                onRegionSelected(it)
                showLocationDialog = false
            },
            onDismissRequest = { showLocationDialog = false },
        )
    }

    if (showDateDialog) {
        CourseDatePickerDialog(
            initialDate = uiState.selectedDate ?: today,
            minimumDate = today,
            onDateSelected = {
                onDateSelected(it)
                showDateDialog = false
            },
            onPastDateClick = {},
            onDismissRequest = { showDateDialog = false },
        )
    }
}

@Composable
private fun CourseEditInputSection(
    label: String,
    content: @Composable () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = DayTodoTheme.typography.title2.copy(letterSpacing = 0.sp),
            color = DayTodoTheme.colors.textPrimary,
        )
        Spacer(modifier = Modifier.height(8.dp))
        content()
    }
}

@Composable
private fun CourseEditField(
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    trailingIcon: Int? = null,
    onClick: (() -> Unit)? = null,
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFC1C1C1), RoundedCornerShape(12.dp))
            .then(
                if (onClick != null) {
                    Modifier.clickable(role = Role.Button, onClick = onClick)
                } else {
                    Modifier
                },
            ),
        enabled = !readOnly,
        singleLine = true,
        textStyle = DayTodoTheme.typography.label2.copy(
            color = DayTodoTheme.colors.textPrimary,
            letterSpacing = 0.sp,
        ),
        keyboardOptions = KeyboardOptions.Default,
        cursorBrush = SolidColor(DayTodoTheme.colors.brandPrimary),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterStart,
            ) {
                if (readOnly || value.isBlank()) {
                    Text(
                        text = value.ifBlank { placeholder },
                        modifier = Modifier.padding(end = if (trailingIcon != null) 32.dp else 0.dp),
                        style = DayTodoTheme.typography.label2.copy(letterSpacing = 0.sp),
                        color = if (value.isBlank()) {
                            DayTodoTheme.colors.textSecondary
                        } else {
                            DayTodoTheme.colors.textPrimary
                        },
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                if (!readOnly) {
                    innerTextField()
                }
                if (trailingIcon != null) {
                    Icon(
                        painter = painterResource(id = trailingIcon),
                        contentDescription = null,
                        tint = DayTodoTheme.colors.textPrimary,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .size(24.dp),
                    )
                }
            }
        },
    )
}

@Composable
private fun RegionLoadMessage(uiState: CourseEditUiState) {
    val message = when {
        uiState.isRegionLoading -> "지역 정보를 불러오는 중이에요."
        uiState.isRegionLoadFailed -> "지역 정보를 불러오지 못했어요. 다시 눌러주세요."
        uiState.regionOptions.isEmpty() -> "선택 가능한 지역이 없어요."
        else -> null
    } ?: return

    Spacer(modifier = Modifier.height(10.dp))
    Text(
        text = message,
        style = DayTodoTheme.typography.caption2.copy(letterSpacing = 0.sp),
        color = DayTodoTheme.colors.textSecondary,
    )
}
