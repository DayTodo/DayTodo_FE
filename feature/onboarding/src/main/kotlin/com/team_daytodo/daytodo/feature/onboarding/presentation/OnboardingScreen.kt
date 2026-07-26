package com.team_daytodo.daytodo.feature.onboarding.presentation

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team_daytodo.daytodo.domain.onboarding.model.OnboardingGuide
import com.team_daytodo.daytodo.domain.onboarding.model.OnboardingPage
import com.team_daytodo.daytodo.domain.onboarding.model.OnboardingVisualType
import com.team_daytodo.daytodo.feature.onboarding.model.OnboardingUiState
import com.team_daytodo.daytodo.feature.onboarding.presentation.component.OnboardingFeaturePreview
import com.team_daytodo.daytodo.feature.onboarding.presentation.component.OnboardingMascotPairIllustration
import com.team_daytodo.daytodo.feature.onboarding.presentation.component.OnboardingSingleMascotIllustration
import com.team_daytodo.daytodo.feature.onboarding.presentation.component.OnboardingWelcomePinIllustration
import com.team_daytodo.daytodo.uikit.component.DayTodoCircularIconButton
import com.team_daytodo.daytodo.uikit.component.DayTodoNextStepButton
import com.team_daytodo.daytodo.uikit.component.DayTodoNextStepButtonState
import com.team_daytodo.daytodo.uikit.component.DayTodoPageIndicator
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

@Composable
fun OnboardingScreen(
    uiState: OnboardingUiState,
    onNextClick: () -> Unit,
    onSkipClick: () -> Unit,
    onStartClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val currentPage = uiState.currentPage

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DayTodoTheme.colors.backgroundDefault)
            .statusBarsPadding()
            .navigationBarsPadding(),
    ) {
        when {
            currentPage == null -> OnboardingLoadingContent(
                message = uiState.errorMessage ?: "온보딩을 준비하고 있어요",
                modifier = Modifier.align(Alignment.Center),
            )

            currentPage.visualType == OnboardingVisualType.PlanTogether -> IntroPageContent(
                page = currentPage,
                modifier = Modifier.fillMaxSize(),
            )

            currentPage.visualType == OnboardingVisualType.MemoryRecord -> MemoryRecordPageContent(
                page = currentPage,
                modifier = Modifier.fillMaxSize(),
            )

            currentPage.visualType == OnboardingVisualType.Welcome -> WelcomePageContent(
                page = currentPage,
                modifier = Modifier.fillMaxSize(),
            )

            else -> InstructionPageContent(
                page = currentPage,
                modifier = Modifier.fillMaxSize(),
            )
        }

        if (currentPage?.canSkip == true) {
            SkipButton(
                enabled = !uiState.isCompleting,
                onClick = onSkipClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 40.dp, end = 22.dp),
            )
        }

        if (currentPage != null && !uiState.isLastPage) {
            OnboardingBottomControls(
                pageCount = uiState.pageCount,
                currentPage = uiState.currentPageIndex,
                enabled = !uiState.isCompleting,
                onNextClick = onNextClick,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = ScreenHorizontalPadding)
                    .padding(bottom = 40.dp),
            )
        }

        if (currentPage != null && uiState.isLastPage) {
            DayTodoNextStepButton(
                text = "시작하기",
                state = if (uiState.isCompleting) {
                    DayTodoNextStepButtonState.Loading
                } else {
                    DayTodoNextStepButtonState.Enabled
                },
                onClick = onStartClick,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = ScreenHorizontalPadding)
                    .padding(bottom = 40.dp),
            )
        }
    }
}

@Composable
private fun IntroPageContent(
    page: OnboardingPage,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(horizontal = ScreenHorizontalPadding)
            .padding(top = 112.dp, bottom = 136.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        OnboardingMascotPairIllustration(
            modifier = Modifier
                .fillMaxWidth(0.82f)
                .height(176.dp),
        )
        Spacer(modifier = Modifier.height(46.dp))
        OnboardingTextBlock(
            headline = page.headline,
            description = page.description,
        )
    }
}

@Composable
private fun InstructionPageContent(
    page: OnboardingPage,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(horizontal = ScreenHorizontalPadding)
            .padding(top = 92.dp, bottom = 128.dp),
    ) {
        Text(
            text = page.headline,
            style = DayTodoTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.sp,
            ),
            color = DayTodoTheme.colors.textPrimary,
        )
        Spacer(modifier = Modifier.height(42.dp))
        page.guide?.let { guide ->
            StepGuideRow(guide = guide)
            Spacer(modifier = Modifier.height(26.dp))
        }
        OnboardingFeaturePreview(
            visualType = page.visualType,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun MemoryRecordPageContent(
    page: OnboardingPage,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(horizontal = ScreenHorizontalPadding)
            .padding(top = 112.dp, bottom = 132.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "오늘의 Pick 매거진",
            style = DayTodoTheme.typography.title2.copy(letterSpacing = 0.sp),
            color = DayTodoTheme.colors.textPrimary,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "AI가 추천한 장소의 코스를\n매일 새로운 매거진으로 만나보세요",
            style = DayTodoTheme.typography.body3.copy(letterSpacing = 0.sp),
            color = DayTodoTheme.colors.textTertiary,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(20.dp))
        OnboardingMascotPairIllustration(
            modifier = Modifier
                .width(138.dp)
                .height(92.dp),
        )
        Spacer(modifier = Modifier.weight(1f))
        OnboardingSingleMascotIllustration(
            modifier = Modifier
                .width(92.dp)
                .height(112.dp),
        )
        Spacer(modifier = Modifier.height(32.dp))
        OnboardingTextBlock(
            headline = page.headline,
            description = page.description,
        )
    }
}

@Composable
private fun WelcomePageContent(
    page: OnboardingPage,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(horizontal = ScreenHorizontalPadding)
            .padding(top = 112.dp, bottom = 126.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        OnboardingWelcomePinIllustration(
            modifier = Modifier
                .width(132.dp)
                .height(184.dp),
        )
        Spacer(modifier = Modifier.height(54.dp))
        OnboardingTextBlock(
            headline = page.headline,
            description = page.description,
        )
    }
}

@Composable
private fun StepGuideRow(
    guide: OnboardingGuide,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(StepBadgeColor),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = guide.step.toString(),
                style = DayTodoTheme.typography.caption2.copy(
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.sp,
                ),
                color = Color.White,
                textAlign = TextAlign.Center,
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = guide.title,
                style = DayTodoTheme.typography.caption1.copy(
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.sp,
                ),
                color = DayTodoTheme.colors.textPrimary,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = guide.description,
                style = DayTodoTheme.typography.body3.copy(letterSpacing = 0.sp),
                color = DayTodoTheme.colors.textTertiary,
            )
        }
    }
}

@Composable
private fun OnboardingTextBlock(
    headline: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = headline,
            style = DayTodoTheme.typography.title2.copy(
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.sp,
            ),
            color = DayTodoTheme.colors.textPrimary,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = description,
            style = DayTodoTheme.typography.body3.copy(letterSpacing = 0.sp),
            color = DayTodoTheme.colors.textTertiary,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun SkipButton(
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        text = "건너뛰기",
        modifier = modifier.clickable(
            enabled = enabled,
            role = Role.Button,
            onClick = onClick,
        ),
        style = DayTodoTheme.typography.caption2.copy(letterSpacing = 0.sp),
        color = DayTodoTheme.colors.textTertiary,
    )
}

@Composable
private fun OnboardingBottomControls(
    pageCount: Int,
    currentPage: Int,
    enabled: Boolean,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp),
    ) {
        DayTodoPageIndicator(
            pageCount = pageCount,
            currentPage = currentPage,
            modifier = Modifier.align(Alignment.Center),
        )
        DayTodoCircularIconButton(
            enabled = enabled,
            onClick = onNextClick,
            contentDescription = "다음",
            modifier = Modifier.align(Alignment.CenterEnd),
        )
    }
}

@Composable
private fun OnboardingLoadingContent(
    message: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = message,
        modifier = modifier.padding(horizontal = ScreenHorizontalPadding),
        style = DayTodoTheme.typography.body3.copy(letterSpacing = 0.sp),
        color = DayTodoTheme.colors.textTertiary,
        textAlign = TextAlign.Center,
    )
}

private val ScreenHorizontalPadding = 28.dp
private val StepBadgeColor = Color(0xFFC4C7C5)

@Preview(showBackground = true)
@Composable
private fun OnboardingScreenPreview() {
    OnboardingScreen(
        uiState = OnboardingUiState(
            pages = listOf(
                OnboardingPage(
                    id = "preview",
                    headline = "소중한 사람과 함께 계획해요",
                    description = "코스방을 만들어 초대하면\n함께 코스를 짜고 의견을 나눌 수 있어요",
                    visualType = OnboardingVisualType.PlanTogether,
                ),
            ),
            isLoading = false,
        ),
        onNextClick = {},
        onSkipClick = {},
        onStartClick = {},
    )
}
