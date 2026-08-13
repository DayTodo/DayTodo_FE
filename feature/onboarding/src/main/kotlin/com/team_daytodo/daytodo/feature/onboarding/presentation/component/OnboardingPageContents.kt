package com.team_daytodo.daytodo.feature.onboarding.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.feature.onboarding.model.OnboardingPageUiModel
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

@Composable
fun OnboardingIntroPageContent(
    page: OnboardingPageUiModel,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 133.dp, bottom = 136.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OnboardingSingleIllustrationImage(
            illustration = page.illustration,
            modifier = Modifier
                .fillMaxWidth(0.82f)
                .height(205.dp),
        )
        Spacer(modifier = Modifier.fillMaxHeight(0.17f))
        OnboardingTextBlock(
            headline = page.headline,
            description = page.description,
        )
    }
}

@Composable
fun OnboardingFeature1PageContent(
    page: OnboardingPageUiModel,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(horizontal = ScreenHorizontalPadding)
            .padding(top = 87.dp),
    ) {
        Text(
            text = page.headline,
            style = DayTodoTheme.typography.headlineLarge,
            color = DayTodoTheme.colors.textPrimary,
        )
        Spacer(modifier = Modifier.height(60.dp))
        page.guide?.let { guide ->
            OnboardingGuideRow(guide = guide)
            Spacer(modifier = Modifier.height(47.5.dp))
        }
        OnboardingSingleIllustrationImage(
            illustration = page.illustration,
            modifier = Modifier
                .fillMaxWidth()
                .height(267.dp),
        )
    }
}

@Composable
fun OnboardingFeature2PageContent(
    page: OnboardingPageUiModel,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = ScreenHorizontalPadding),
    ) {
        val topPadding = maxHeight * Feature2TopPaddingRatio
        val titleGuideGap = maxHeight * Feature2TitleGuideGapRatio
        val guideIllustrationGap = maxHeight * Feature2GuideIllustrationGapRatio

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = topPadding),
        ) {
            Text(
                text = page.headline,
                style = DayTodoTheme.typography.headlineLarge,
                color = DayTodoTheme.colors.textPrimary,
            )
            Spacer(modifier = Modifier.height(titleGuideGap))
            page.guide?.let { guide ->
                OnboardingGuideRow(guide = guide)
                Spacer(modifier = Modifier.height(guideIllustrationGap))
            }
            OnboardingPairedIllustrationImage(
                illustration = page.illustration,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
fun OnboardingFeature3PageContent(
    page: OnboardingPageUiModel,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = ScreenHorizontalPadding)
    ) {
        val compact = maxHeight < 640.dp
        val topPadding = maxHeight * if (compact) 0.09f else 0.13f
        val firstImageMaxHeight = maxHeight * if (compact) 0.16f else 0.19f
        val secondImageMaxHeight = maxHeight * if (compact) 0.17f else 0.21f
        val sectionGap = maxHeight * if (compact) 0.015f else 0.035f
        val textImageGap = if (compact) 12.dp else 24.dp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = topPadding),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.76f)
                    .padding(11.dp)
                    .align(Alignment.Start),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = page.headline,
                    style = DayTodoTheme.typography.headlineLarge,
                    color = DayTodoTheme.colors.textPrimary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = page.description,
                    style = DayTodoTheme.typography.body2,
                    color = DayTodoTheme.colors.textSecondary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(textImageGap))
                Image(
                    painter = painterResource(id = page.illustration.primaryImageRes),
                    contentDescription = page.description,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = firstImageMaxHeight),
                    contentScale = ContentScale.Fit,
                )
            }
            Spacer(modifier = Modifier.height(sectionGap))
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .align(Alignment.End),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (page.illustration.secondaryImageRes == null) {
                    OnboardingSingleIllustrationImage(
                        illustration = page.illustration,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = secondImageMaxHeight),
                    )
                } else {
                    Image(
                        painter = painterResource(id = page.illustration.secondaryImageRes),
                        contentDescription = page.description,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = secondImageMaxHeight),
                        contentScale = ContentScale.Fit,
                    )
                }
                Spacer(modifier = Modifier.height(textImageGap))
                page.guide?.let { guide ->
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = guide.title,
                        style = DayTodoTheme.typography.headlineLarge,
                        color = DayTodoTheme.colors.textPrimary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = guide.description,
                        style = DayTodoTheme.typography.body2,
                        color = DayTodoTheme.colors.textSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun OnboardingCompletionPageContent(
    page: OnboardingPageUiModel,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(horizontal = ScreenHorizontalPadding)
            .padding(top = 159.dp)
            .fillMaxHeight(0.41f),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OnboardingSingleIllustrationImage(
            illustration = page.illustration,
            modifier = Modifier
                .fillMaxWidth(0.57f)
                .fillMaxHeight(0.35f),
            colorFilter = ColorFilter.tint(DayTodoTheme.colors.brandPrimary)
        )
        Spacer(modifier = Modifier.fillMaxHeight(0.15f))
        OnboardingTextBlock(
            headline = page.headline,
            description = page.description,
        )
    }
}

private val ScreenHorizontalPadding = 20.dp
private const val Feature2TopPaddingRatio = 0.10f
private const val Feature2TitleGuideGapRatio = 0.07f
private const val Feature2GuideIllustrationGapRatio = 0.03f
