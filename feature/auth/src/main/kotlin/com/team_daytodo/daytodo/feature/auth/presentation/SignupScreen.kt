package com.team_daytodo.daytodo.feature.auth.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.feature.auth.model.SignupPolicyDialogUiState
import com.team_daytodo.daytodo.feature.auth.model.SignupUiState
import com.team_daytodo.daytodo.feature.mypage.component.terms.PolicyBodyText
import com.team_daytodo.daytodo.feature.auth.presentation.component.AuthIntroSection
import com.team_daytodo.daytodo.feature.auth.presentation.component.AuthPasswordTextField
import com.team_daytodo.daytodo.uikit.component.DayTodoAuthTextField
import com.team_daytodo.daytodo.uikit.component.DayTodoAuthTextFieldColors
import com.team_daytodo.daytodo.uikit.component.DayTodoHeaderSection
import com.team_daytodo.daytodo.uikit.component.DayTodoNextStepButton
import com.team_daytodo.daytodo.uikit.component.DayTodoNextStepButtonState
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

@Composable
fun SignupScreen(
    uiState: SignupUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordConfirmChange: (String) -> Unit,
    onTermsAgreementChange: (Boolean) -> Unit,
    onPolicyViewClick: (Int) -> Unit,
    onPolicyDialogDismiss: () -> Unit,
    onPasswordVisibilityClick: () -> Unit,
    onPasswordConfirmVisibilityClick: () -> Unit,
    onSignupClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showPolicyAgreementSheet by rememberSaveable { mutableStateOf(false) }
    val inputColors = DayTodoAuthTextFieldColors.filledWhenUnfocused()
    val signupButtonState = when {
        uiState.isLoading -> DayTodoNextStepButtonState.Loading
        uiState.canSubmit -> DayTodoNextStepButtonState.Enabled
        else -> DayTodoNextStepButtonState.Incomplete
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DayTodoTheme.colors.backgroundDefault)
            .statusBarsPadding(),
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 132.dp),
        ) {
            item {
                DayTodoHeaderSection(
                    title = "회원가입",
                    onBackClick = onBackClick,
                    horizontalPadding = ScreenHorizontalPadding,
                )
            }
            item {
                AuthIntroSection(
                    title = "함께해요, 데이투두",
                    description = "몇 가지 정보만 입력하면 바로 시작돼요",
                    modifier = Modifier
                        .padding(horizontal = ScreenHorizontalPadding)
                        .padding(top = 44.dp),
                )
            }
            item { Spacer(modifier = Modifier.height(46.dp)) }
            item {
                DayTodoAuthTextField(
                    label = "아이디(이메일)",
                    value = uiState.email,
                    onValueChange = onEmailChange,
                    modifier = Modifier.padding(horizontal = ScreenHorizontalPadding),
                    placeholder = "example@daytodo.com",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next,
                    ),
                    colors = inputColors,
                )
            }
            item { Spacer(modifier = Modifier.height(12.dp)) }
            item {
                AuthPasswordTextField(
                    label = "비밀번호",
                    value = uiState.password,
                    onValueChange = onPasswordChange,
                    passwordVisible = uiState.isPasswordVisible,
                    onPasswordVisibilityClick = onPasswordVisibilityClick,
                    modifier = Modifier.padding(horizontal = ScreenHorizontalPadding),
                    placeholder = "영문, 숫자 포함 8자 이상",
                    imeAction = ImeAction.Next,
                    colors = inputColors,
                )
            }
            item { Spacer(modifier = Modifier.height(12.dp)) }
            item {
                AuthPasswordTextField(
                    label = "비밀번호 확인",
                    value = uiState.passwordConfirm,
                    onValueChange = onPasswordConfirmChange,
                    passwordVisible = uiState.isPasswordConfirmVisible,
                    onPasswordVisibilityClick = onPasswordConfirmVisibilityClick,
                    modifier = Modifier.padding(horizontal = ScreenHorizontalPadding),
                    placeholder = "영문, 숫자 포함 8자 이상",
                    colors = inputColors,
                )
            }
            item { Spacer(modifier = Modifier.height(20.dp)) }
            item {
                SignupTermsAgreementRow(
                    checked = uiState.agreedToTerms,
                    onToggleClick = {
                        if (uiState.agreedToTerms) {
                            onTermsAgreementChange(false)
                        } else {
                            showPolicyAgreementSheet = true
                        }
                    },
                    onPolicyTextClick = { showPolicyAgreementSheet = true },
                    modifier = Modifier.padding(horizontal = ScreenHorizontalPadding),
                )
            }
        }

        DayTodoNextStepButton(
            text = "가입하기",
            state = signupButtonState,
            onClick = onSignupClick,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(horizontal = ScreenHorizontalPadding)
                .padding(bottom = BottomButtonPadding),
        )
    }

    if (showPolicyAgreementSheet) {
        SignupPolicyAgreementBottomSheet(
            onDismissRequest = { showPolicyAgreementSheet = false },
            onPolicyViewClick = onPolicyViewClick,
            onAgreeClick = {
                onTermsAgreementChange(true)
                showPolicyAgreementSheet = false
            },
        )
    }

    uiState.policyDialog?.let { dialogState ->
        SignupPolicyDocumentDialog(
            uiState = dialogState,
            onDismissRequest = onPolicyDialogDismiss,
        )
    }
}

@Composable
private fun SignupTermsAgreementRow(
    checked: Boolean,
    onToggleClick: () -> Unit,
    onPolicyTextClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(role = Role.Checkbox, onClick = onToggleClick),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AgreementCheckMark(checked = checked, shape = RoundedCornerShape(5.dp))
        Spacer(modifier = Modifier.width(10.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "이용약관 및 개인정보처리방침",
                modifier = Modifier.clickable(role = Role.Button, onClick = onPolicyTextClick),
                style = DayTodoTheme.typography.caption1,
                color = DayTodoTheme.colors.brandPrimary,
            )
            Text(
                text = "에 동의합니다.",
                style = DayTodoTheme.typography.caption1,
                color = DayTodoTheme.colors.textPrimary,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SignupPolicyAgreementBottomSheet(
    onDismissRequest: () -> Unit,
    onPolicyViewClick: (Int) -> Unit,
    onAgreeClick: () -> Unit,
) {
    var checkedIndexes by rememberSaveable { mutableStateOf(emptyList<Int>()) }
    val requiredIndexes = SignupPolicyItems
        .mapIndexedNotNull { index, item -> if (item.required) index else null }
    val requiredChecked = requiredIndexes.all { it in checkedIndexes }
    val allChecked = SignupPolicyItems.indices.all { it in checkedIndexes }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp)
                .padding(bottom = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "약관 확인 및 동의",
                style = DayTodoTheme.typography.title2,
                color = DayTodoTheme.colors.textPrimary,
            )
            Spacer(modifier = Modifier.height(24.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .border(1.dp, DayTodoTheme.colors.divider, RoundedCornerShape(24.dp))
                    .padding(horizontal = 20.dp, vertical = 16.dp),
            ) {
                SignupPolicyCheckRow(
                    label = "전체 동의 (선택 동의 포함)",
                    checked = allChecked,
                    onClick = {
                        checkedIndexes = if (allChecked) {
                            emptyList()
                        } else {
                            SignupPolicyItems.indices.toList()
                        }
                    },
                )
                SignupPolicyItems.forEachIndexed { index, item ->
                    SignupPolicyCheckRow(
                        label = item.label,
                        required = item.required,
                        checked = index in checkedIndexes,
                        showView = true,
                        onClick = {
                            checkedIndexes = if (index in checkedIndexes) {
                                checkedIndexes - index
                            } else {
                                checkedIndexes + index
                            }
                        },
                        onViewClick = { onPolicyViewClick(index) },
                    )
                }
            }
            Spacer(modifier = Modifier.height(28.dp))
            DayTodoNextStepButton(
                text = "동의",
                state = if (requiredChecked) {
                    DayTodoNextStepButtonState.Enabled
                } else {
                    DayTodoNextStepButtonState.Disabled
                },
                onClick = onAgreeClick,
            )
        }
    }
}

@Composable
private fun SignupPolicyCheckRow(
    label: String,
    checked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    required: Boolean? = null,
    showView: Boolean = false,
    onViewClick: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(58.dp)
            .clickable(role = Role.Checkbox, onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AgreementCheckMark(checked = checked, shape = CircleShape)
        Spacer(modifier = Modifier.width(12.dp))
        if (required != null) {
            Text(
                text = if (required) "[필수] " else "[선택] ",
                style = DayTodoTheme.typography.label3,
                color = if (required) Color(0xFFE65C5C) else DayTodoTheme.colors.textSecondary,
            )
        }
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            style = DayTodoTheme.typography.label3,
            color = DayTodoTheme.colors.textPrimary,
        )
        if (showView) {
            Text(
                text = "보기",
                modifier = Modifier.clickable(
                    role = Role.Button,
                    onClick = { onViewClick?.invoke() },
                ),
                style = DayTodoTheme.typography.label3,
                color = DayTodoTheme.colors.brandPrimary,
            )
        }
    }
}

@Composable
private fun SignupPolicyDocumentDialog(
    uiState: SignupPolicyDialogUiState,
    onDismissRequest: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = uiState.title,
                style = DayTodoTheme.typography.title2,
                color = DayTodoTheme.colors.textPrimary,
            )
        },
        text = {
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(color = DayTodoTheme.colors.brandPrimary)
                    }
                }
                uiState.isError -> {
                    Text(
                        text = "약관 내용을 불러오지 못했어요. 잠시 후 다시 시도해 주세요.",
                        style = DayTodoTheme.typography.body3,
                        color = DayTodoTheme.colors.textSecondary,
                    )
                }
                else -> {
                    LazyColumn(modifier = Modifier.height(320.dp)) {
                        item {
                            PolicyBodyText(body = uiState.body)
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismissRequest) {
                Text(
                    text = "확인",
                    style = DayTodoTheme.typography.label2,
                    color = DayTodoTheme.colors.brandPrimary,
                )
            }
        },
    )
}

@Composable
private fun AgreementCheckMark(
    checked: Boolean,
    shape: androidx.compose.ui.graphics.Shape,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(24.dp)
            .clip(shape)
            .background(if (checked) DayTodoTheme.colors.brandPrimary else Color.Transparent)
            .border(1.dp, Color(0xFFC7C7D0), shape),
        contentAlignment = Alignment.Center,
    ) {
        if (checked) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = Color.White,
            )
        }
    }
}

private data class SignupPolicyItem(
    val label: String,
    val required: Boolean,
)

private val SignupPolicyItems = listOf(
    SignupPolicyItem(label = "이용약관 동의", required = true),
    SignupPolicyItem(label = "개인정보처리방침 동의", required = true),
    SignupPolicyItem(label = "만 14세 이상 확인", required = true),
    SignupPolicyItem(label = "마케팅 및 이벤트 정보 수신 동의", required = false),
)

@Preview
@Composable
private fun SignupScreenPreview() {
    SignupScreen(
        uiState = SignupUiState(),
        onEmailChange = {},
        onPasswordChange = {},
        onPasswordConfirmChange = {},
        onTermsAgreementChange = {},
        onPolicyViewClick = {},
        onPolicyDialogDismiss = {},
        onPasswordVisibilityClick = {},
        onPasswordConfirmVisibilityClick = {},
        onSignupClick = {},
        onBackClick = {},
    )
}
