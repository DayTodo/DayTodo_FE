package com.team_daytodo.daytodo.feature.onboarding.model

import com.team_daytodo.daytodo.feature.onboarding.R

internal data class OnboardingPresentationSpec(
    val template: OnboardingPageTemplate,
    val illustration: OnboardingIllustration,
)

internal fun String.toOnboardingPresentationSpec(): OnboardingPresentationSpec =
    when (this) {
        PageIdPlanTogether -> OnboardingPresentationSpec(
            template = OnboardingPageTemplate.Intro,
            illustration = OnboardingIllustration(
                primaryImageRes = R.drawable.img_onboarding_main,
                contentDescription = "함께 코스를 계획하는 데이투두 캐릭터",
            ),
        )

        PageIdPickMagazine -> OnboardingPresentationSpec(
            template = OnboardingPageTemplate.Feature1,
            illustration = OnboardingIllustration(
                primaryImageRes = R.drawable.img_onboarding_magazine,
                contentDescription = "오늘의 픽 매거진 화면 예시",
            ),
        )

        PageIdCourseName -> OnboardingPresentationSpec(
            template = OnboardingPageTemplate.Feature1,
            illustration = OnboardingIllustration(
                primaryImageRes = R.drawable.img_onboarding_course,
                contentDescription = "코스 방 이름 입력 화면 예시",
            ),
        )

        PageIdInviteLink -> OnboardingPresentationSpec(
            template = OnboardingPageTemplate.Feature1,
            illustration = OnboardingIllustration(
                primaryImageRes = R.drawable.img_onboarding_group,
                contentDescription = "초대 링크 공유 화면 예시",
            ),
        )

        PageIdAddPlaces -> OnboardingPresentationSpec(
            template = OnboardingPageTemplate.Feature2,
            illustration = OnboardingIllustration(
                primaryImageRes = R.drawable.img_onboarding_home,
                secondaryImageRes = R.drawable.img_onboarding_recommend,
                contentDescription = "장소 추천으로 코스를 채우는 화면 예시",
            ),
        )

        PageIdMemoryRecord -> OnboardingPresentationSpec(
            template = OnboardingPageTemplate.Feature3,
            illustration = OnboardingIllustration(
                primaryImageRes = R.drawable.img_onboarding_search,
                secondaryImageRes = R.drawable.img_onboarding_record,
                contentDescription = "함께한 순간을 기록하는 화면 예시",
            ),
        )

        PageIdCompletion -> OnboardingPresentationSpec(
            template = OnboardingPageTemplate.Completion,
            illustration = OnboardingIllustration(
                primaryImageRes = com.team_daytodo.daytodo.uikit.R.drawable.ic_symbol,
                contentDescription = "온보딩 완료",
            ),
        )

        else -> OnboardingPresentationSpec(
            template = OnboardingPageTemplate.Intro,
            illustration = OnboardingIllustration(
                primaryImageRes = R.drawable.img_onboarding_main,
                contentDescription = null,
            ),
        )
    }

private const val PageIdPlanTogether = "plan-together"
private const val PageIdPickMagazine = "pick-magazine"
private const val PageIdCourseName = "course-name"
private const val PageIdInviteLink = "invite-link"
private const val PageIdAddPlaces = "add-places"
private const val PageIdMemoryRecord = "memory-record"
private const val PageIdCompletion = "completion"
