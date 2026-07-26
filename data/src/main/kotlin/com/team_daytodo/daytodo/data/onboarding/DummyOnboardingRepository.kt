package com.team_daytodo.daytodo.data.onboarding

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import com.team_daytodo.daytodo.domain.onboarding.model.OnboardingGuide
import com.team_daytodo.daytodo.domain.onboarding.model.OnboardingPage
import com.team_daytodo.daytodo.domain.onboarding.model.OnboardingVisualType
import com.team_daytodo.daytodo.domain.onboarding.repository.OnboardingRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.delay

@Singleton
class DummyOnboardingRepository @Inject constructor(
    @ApplicationContext context: Context,
) : OnboardingRepository {
    private val preferences = context.getSharedPreferences(
        OnboardingPreferencesName,
        Context.MODE_PRIVATE,
    )

    override suspend fun isOnboardingCompleted(): Result<Boolean> =
        runCatching {
            preferences.getBoolean(OnboardingCompletedKey, false)
        }

    override suspend fun getOnboardingPages(): Result<List<OnboardingPage>> =
        runCatching {
            delay(OnboardingRequestDelayMillis)
            DummyOnboardingPages
        }

    override suspend fun completeOnboarding(): Result<Unit> =
        runCatching {
            check(
                preferences.edit()
                .putBoolean(OnboardingCompletedKey, true)
                    .commit(),
            ) {
                "온보딩 완료 상태를 저장하지 못했어요."
            }
            Unit
        }

    private companion object {
        const val OnboardingPreferencesName = "daytodo_onboarding_preferences"
        const val OnboardingCompletedKey = "onboarding_completed"
        const val OnboardingRequestDelayMillis = 120L

        val DummyOnboardingPages = listOf(
            OnboardingPage(
                id = "plan-together",
                headline = "소중한 사람과 함께 계획해요",
                description = "코스방을 만들어 초대하면\n함께 코스를 짜고 의견을 나눌 수 있어요",
                visualType = OnboardingVisualType.PlanTogether,
            ),
            OnboardingPage(
                id = "pick-magazine",
                headline = "코스를 만드는 건\n4단계면 충분해요",
                description = "",
                visualType = OnboardingVisualType.PickMagazine,
                guide = OnboardingGuide(
                    step = 1,
                    title = "버튼 하나로 시작",
                    description = "'새 코스방 만들기'를 눌러 새로운 코스를 만들어보세요.",
                ),
            ),
            OnboardingPage(
                id = "course-name",
                headline = "코스를 만드는 건\n4단계면 충분해요",
                description = "",
                visualType = OnboardingVisualType.CourseName,
                guide = OnboardingGuide(
                    step = 2,
                    title = "코스 정보를 입력하세요",
                    description = "몇가지 정보만 입력하면 코스가 완성돼요.",
                ),
            ),
            OnboardingPage(
                id = "invite-link",
                headline = "코스를 만드는 건\n4단계면 충분해요",
                description = "",
                visualType = OnboardingVisualType.InviteLink,
                guide = OnboardingGuide(
                    step = 3,
                    title = "링크를 공유해 함께 계획하세요",
                    description = "초대 링크를 보내면 일행과 함께 코스를 만들 수 있어요.",
                ),
            ),
            OnboardingPage(
                id = "add-places",
                headline = "코스를 만드는 건\n4단계면 충분해요",
                description = "",
                visualType = OnboardingVisualType.AddPlaces,
                guide = OnboardingGuide(
                    step = 4,
                    title = "코스방에서 원하는 장소를 추가해 보세요",
                    description = "함께 가고 싶은 장소를 AI 추천으로 찾거나\n직접 추가하여 코스를 완성해 보세요.",
                ),
            ),
            OnboardingPage(
                id = "memory-record",
                headline = "함께한 순간을 기록해요",
                description = "코스가 끝나면 그날 찍은 사진과\n서로 남긴 메모로 데이트를 추억으로 남겨보세요",
                visualType = OnboardingVisualType.MemoryRecord,
            ),
            OnboardingPage(
                id = "welcome",
                headline = "함께해요, 데이투두",
                description = "지금 시작하고\n함께할 코스를 만들어보세요",
                visualType = OnboardingVisualType.Welcome,
                canSkip = false,
            ),
        )
    }
}
