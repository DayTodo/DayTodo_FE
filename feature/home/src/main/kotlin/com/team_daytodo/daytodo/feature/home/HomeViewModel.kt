package com.team_daytodo.daytodo.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team_daytodo.daytodo.domain.home.model.HomeDashboard
import com.team_daytodo.daytodo.domain.home.model.HomeMagazine
import com.team_daytodo.daytodo.domain.home.model.HomeTodayCourse
import com.team_daytodo.daytodo.domain.home.model.HomeUpcomingCourse
import com.team_daytodo.daytodo.domain.home.usecase.GetHomeDataUseCase
import com.team_daytodo.daytodo.domain.mypage.usecase.GetMypageProfileUseCase
import com.team_daytodo.daytodo.feature.home.model.CourseMember
import com.team_daytodo.daytodo.feature.home.model.CreatedCourse
import com.team_daytodo.daytodo.feature.home.model.HomeMagazineUiModel
import com.team_daytodo.daytodo.feature.home.model.HomeUiState
import com.team_daytodo.daytodo.feature.home.model.TodayCourse
import com.team_daytodo.daytodo.feature.home.model.UpcomingCourse
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getHomeDataUseCase: GetHomeDataUseCase,
    private val getMypageProfileUseCase: GetMypageProfileUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHome()
    }

    fun loadHome() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val homeDataDeferred = async { getHomeDataUseCase() }
            val profileDeferred = async { getMypageProfileUseCase() }
            val homeDataResult = homeDataDeferred.await()

            if (homeDataResult.isSuccess) {
                val username = profileDeferred.await()
                    .getOrNull()
                    ?.nickname
                    .orEmpty()

                _uiState.value = homeDataResult.getOrThrow().toUiState(username)
            } else {
                profileDeferred.cancel()
                homeDataResult.exceptionOrNull()?.let { cause ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = cause.userFacingMessage(),
                        )
                    }
                }
            }
        }
    }
}

private fun HomeDashboard.toUiState(username: String): HomeUiState {
    val nearestUpcomingCourse = courses.upcomingCourses.firstOrNull()
    val magazineUiModels = magazines.map { it.toUiModel() }

    return HomeUiState(
        username = username.ifBlank { courses.banner.nickname.orEmpty() },
        bannerMessage = courses.banner.message,
        interestLocation = magazineUiModels.firstOrNull()?.location ?: DefaultInterestLocation,
        todayCourse = courses.todayCourse?.toUiModel(),
        upcomingCourse = nearestUpcomingCourse?.toUpcomingCourseUiModel(),
        createdCourses = courses.upcomingCourses.map { it.toCreatedCourseUiModel() },
        todayPickMagazines = magazineUiModels,
        isLoading = false,
        errorMessage = null,
    )
}

private fun HomeTodayCourse.toUiModel(): TodayCourse = TodayCourse(
    courseId = courseId.toString(),
    date = courseDate.toShortDisplayText(),
    title = courseName,
    relationship = relationship,
    members = members.map { member ->
        CourseMember(
            name = member.nickname,
            profileImageUrl = member.profileImageUrl,
        )
    },
)

private fun HomeUpcomingCourse.toUpcomingCourseUiModel(): UpcomingCourse = UpcomingCourse(
    relationship = relationship,
    date = courseDate.toShortDisplayText(),
)

private fun HomeUpcomingCourse.toCreatedCourseUiModel(): CreatedCourse = CreatedCourse(
    courseId = courseId.toString(),
    title = courseName,
    date = courseDate.toLongDisplayText(),
    memberCount = memberCount,
    dDay = dDay.toDDayText(),
    relationship = relationship,
)

private fun HomeMagazine.toUiModel(): HomeMagazineUiModel = HomeMagazineUiModel(
    placeId = magazineId.toString(),
    title = placeName,
    location = regionName,
    description = tagline,
    thumbnailUrl = thumbnailUrl,
)

private fun LocalDate.toShortDisplayText(): String =
    format(ShortDateFormatter)

private fun LocalDate.toLongDisplayText(): String =
    format(LongDateFormatter)

private fun Int.toDDayText(): String =
    if (this <= 0) "D-Day" else "D-$this"

private fun Throwable.userFacingMessage(): String =
    message?.takeIf(String::isNotBlank) ?: "홈 정보를 불러오지 못했어요."

private val ShortDateFormatter = DateTimeFormatter.ofPattern("yyyy.M.d. (E)", Locale.KOREAN)
private val LongDateFormatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일 E요일", Locale.KOREAN)
private const val DefaultInterestLocation = "전국"
