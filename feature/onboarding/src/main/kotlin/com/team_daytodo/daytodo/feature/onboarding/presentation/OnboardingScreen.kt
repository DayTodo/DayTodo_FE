package com.team_daytodo.daytodo.feature.onboarding.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team_daytodo.daytodo.feature.onboarding.R
import com.team_daytodo.daytodo.feature.onboarding.model.OnboardingGuideUiModel
import com.team_daytodo.daytodo.feature.onboarding.model.OnboardingIllustration
import com.team_daytodo.daytodo.feature.onboarding.model.OnboardingPageTemplate
import com.team_daytodo.daytodo.feature.onboarding.model.OnboardingPageUiModel
import com.team_daytodo.daytodo.feature.onboarding.model.OnboardingUiState
import com.team_daytodo.daytodo.feature.onboarding.presentation.component.OnboardingCompletionPageContent
import com.team_daytodo.daytodo.feature.onboarding.presentation.component.OnboardingFeature1PageContent
import com.team_daytodo.daytodo.feature.onboarding.presentation.component.OnboardingFeature2PageContent
import com.team_daytodo.daytodo.feature.onboarding.presentation.component.OnboardingFeature3PageContent
import com.team_daytodo.daytodo.feature.onboarding.presentation.component.OnboardingIntroPageContent
import com.team_daytodo.daytodo.feature.onboarding.presentation.component.SkipButton
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
        if (currentPage == null) {
            OnboardingLoadingContent(
                message = uiState.errorMessage ?: "온보딩을 준비하고 있어요",
                modifier = Modifier.align(Alignment.Center),
            )
        } else {
            OnboardingPageContent(
                page = currentPage,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        bottom = if (uiState.isLastPage) {
                            LastPageBottomContentPadding
                        } else {
                            BottomControlsContentPadding
                        },
                    ),
            )
        }

        if (currentPage?.canSkip == true) {
            SkipButton(
                enabled = !uiState.isCompleting,
                onClick = onSkipClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 35.dp, end = ScreenHorizontalPadding),
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
                    .padding(bottom = 74.dp),
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
private fun OnboardingPageContent(
    page: OnboardingPageUiModel,
    modifier: Modifier = Modifier,
) {
    when (page.template) {
        OnboardingPageTemplate.Intro -> OnboardingIntroPageContent(
            page = page,
            modifier = modifier,
        )

        OnboardingPageTemplate.Feature1 -> OnboardingFeature1PageContent(
            page = page,
            modifier = modifier,
        )

        OnboardingPageTemplate.Feature2 -> OnboardingFeature2PageContent(
            page = page,
            modifier = modifier,
        )

        OnboardingPageTemplate.Feature3 -> OnboardingFeature3PageContent(
            page = page,
            modifier = modifier,
        )

        OnboardingPageTemplate.Completion -> OnboardingCompletionPageContent(
            page = page,
            modifier = modifier,
        )
    }
}

@Composable
private fun OnboardingBottomControls(
    pageCount: Int,
    currentPage: Int,
    enabled: Boolean,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            DayTodoPageIndicator(
                pageCount = pageCount,
                currentPage = currentPage,
                modifier = Modifier.align(Alignment.Center),
            )
        }
        Spacer(modifier = Modifier.height(31.dp))
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            DayTodoCircularIconButton(
                enabled = enabled,
                onClick = onNextClick,
                contentDescription = "다음",
                modifier = Modifier.align(Alignment.CenterEnd),
            )
        }
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

private val ScreenHorizontalPadding = 20.dp
private val BottomControlsContentPadding = 178.dp
private val LastPageBottomContentPadding = 132.dp

@Preview(showBackground = true)
@Composable
private fun OnboardingScreenPreview() {
    OnboardingScreen(
        uiState = OnboardingUiState(
            pages = listOf(
//                OnboardingPageUiModel(
//                    id = "memory-record",
//                    headline = "오늘의 Pick 메거진",
//                    description = "AI가 추천한 장소와 코스를\n매일 새로운 메거진으로 만나보세요",
//                    illustration = OnboardingIllustration(
//                        primaryImageRes = R.drawable.img_onboarding_search,
//                        secondaryImageRes = R.drawable.img_onboarding_record,
//                    ),
//                    template = OnboardingPageTemplate.Feature3,
//                    guide = OnboardingGuideUiModel(
//                        step = 1,
//                        title = "함께한 순간을 기록해요",
//                        description = "코스가 끝나면 그날 찍은 사진과\n서로 남긴 메모로 데이트를 추억으로 남겨보세요"
//                    )
//                ),
                OnboardingPageUiModel(
                    id = "add-places",
                    headline = "함께해요, 데이투두",
                    description = "지금 시작하고\n함께할 코스를 만들어보세요",
                    illustration = OnboardingIllustration(
                        primaryImageRes = R.drawable.img_onboarding_home,
                        secondaryImageRes = R.drawable.img_onboarding_recommend
                    ),
                    guide = OnboardingGuideUiModel(
                        step = 1,
                        title = "함께한 순간을 기록해요",
                        description = "코스가 끝나면 그날 찍은 사진과\n서로 남긴 메모로 데이트를 추억으로 남겨보세요"
                    ),
                    template = OnboardingPageTemplate.Feature2,
                ),
            ),
            isLoading = false,
        ),
        onNextClick = {},
        onSkipClick = {},
        onStartClick = {},
    )
}
