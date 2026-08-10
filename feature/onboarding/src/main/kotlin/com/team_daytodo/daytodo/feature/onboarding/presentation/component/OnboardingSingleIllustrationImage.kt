package com.team_daytodo.daytodo.feature.onboarding.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.team_daytodo.daytodo.feature.onboarding.model.OnboardingIllustration

@Composable
fun OnboardingSingleIllustrationImage(
    illustration: OnboardingIllustration,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
    colorFilter: ColorFilter? = null,
) {
    Image(
        painter = painterResource(id = illustration.primaryImageRes),
        contentDescription = illustration.contentDescription,
        modifier = modifier,
        contentScale = contentScale,
        colorFilter = colorFilter,
    )
}