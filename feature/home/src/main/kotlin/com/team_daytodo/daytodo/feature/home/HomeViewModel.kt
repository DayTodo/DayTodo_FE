package com.team_daytodo.daytodo.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team_daytodo.daytodo.domain.home.model.HomeDashboard
import com.team_daytodo.daytodo.domain.home.model.HomeMagazine
import com.team_daytodo.daytodo.domain.home.model.HomeTodayCourse
import com.team_daytodo.daytodo.domain.home.model.HomeUpcomingCourse
import com.team_daytodo.daytodo.domain.home.usecase.GetHomeDataUseCase
import com.team_daytodo.daytodo.domain.mypage.model.InterestRegionOption
import com.team_daytodo.daytodo.domain.mypage.usecase.GetInterestRegionOptionsUseCase
import com.team_daytodo.daytodo.domain.mypage.usecase.GetInterestRegionsUseCase
import com.team_daytodo.daytodo.domain.mypage.usecase.GetMypageProfileUseCase
import com.team_daytodo.daytodo.domain.mypage.usecase.UpdateInterestRegionsUseCase
import com.team_daytodo.daytodo.feature.home.model.CourseMember
import com.team_daytodo.daytodo.feature.home.model.CreatedCourse
import com.team_daytodo.daytodo.feature.home.model.HomeInterestRegionGroup
import com.team_daytodo.daytodo.feature.home.model.HomeInterestRegionOption
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
    private val getInterestRegionsUseCase: GetInterestRegionsUseCase,
    private val getInterestRegionOptionsUseCase: GetInterestRegionOptionsUseCase,
    private val updateInterestRegionsUseCase: UpdateInterestRegionsUseCase,
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
            val interestRegionsDeferred = async { getInterestRegionsUseCase() }
            val interestRegionOptionsDeferred = async { getInterestRegionOptionsUseCase() }
            val homeDataResult = homeDataDeferred.await()

            if (homeDataResult.isSuccess) {
                val username = profileDeferred.await()
                    .getOrNull()
                    ?.nickname
                    .orEmpty()
                val interestRegionsResult = interestRegionsDeferred.await()
                val interestRegions = interestRegionsResult.getOrNull().orEmpty()
                val interestRegionOptions = interestRegionOptionsDeferred.await().getOrNull().orEmpty()
                val interestRegionGroups = interestRegionOptions.toHomeInterestRegionGroups()

                val selectedInterestRegionIds = interestRegions.map { it.regionId }.toSet()

                _uiState.value = homeDataResult.getOrThrow().toUiState(
                    username = username,
                    interestRegionName = interestRegions.firstOrNull()?.regionName,
                    shouldRequestInterestRegionSetup = interestRegionsResult.isSuccess && interestRegions.isEmpty(),
                    interestRegionGroups = interestRegionGroups,
                    selectedInterestRegionIds = selectedInterestRegionIds,
                )
            } else {
                profileDeferred.cancel()
                interestRegionsDeferred.cancel()
                interestRegionOptionsDeferred.cancel()
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

    fun showInterestRegionDialog() {
        _uiState.update {
            it.copy(
                showInterestRegionDialog = true,
                selectedInterestRegionGroupName = it.selectedInterestRegionGroupName
                    .ifBlank { it.interestRegionGroups.firstOrNull()?.parentName.orEmpty() },
                interestRegionErrorMessage = null,
            )
        }
    }

    fun dismissInterestRegionDialog() {
        _uiState.update { it.copy(showInterestRegionDialog = false, interestRegionErrorMessage = null) }
    }

    fun selectInterestRegionGroup(parentName: String) {
        _uiState.update { current ->
            val nextGroup = current.interestRegionGroups.firstOrNull { it.parentName == parentName }
            current.copy(
                selectedInterestRegionGroupName = parentName,
                selectedInterestRegionOptionKey = nextGroup?.options?.firstOrNull()?.key,
                selectedInterestRegionIds = nextGroup?.options
                    ?.firstOrNull()
                    ?.regionId
                    ?.let(::setOf)
                    .orEmpty(),
            )
        }
    }

    fun selectInterestRegionOption(optionKey: String) {
        _uiState.update { current ->
            val selectedRegionId = current.interestRegionGroups
                .flatMap { it.options }
                .firstOrNull { it.key == optionKey }
                ?.regionId
            current.copy(
                selectedInterestRegionOptionKey = optionKey,
                selectedInterestRegionIds = selectedRegionId?.let(::setOf).orEmpty(),
            )
        }
    }

    fun saveInterestRegions() {
        val currentState = _uiState.value
        if (!currentState.canSaveInterestRegions) return
        val selectedRegionId = currentState.selectedInterestRegionOption?.regionId ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isInterestRegionSaving = true, interestRegionErrorMessage = null) }
            updateInterestRegionsUseCase(listOf(selectedRegionId))
                .onSuccess { savedRegions ->
                    val savedRegionIds = savedRegions.map { it.regionId }.toSet()
                    val selectedOption = _uiState.value.interestRegionGroups
                        .flatMap { it.options }
                        .firstOrNull { it.regionId in savedRegionIds }
                    _uiState.update {
                        it.copy(
                            interestLocation = savedRegions.firstOrNull()?.regionName
                                ?: it.selectedInterestRegionName()
                                ?: it.interestLocation,
                            selectedInterestRegionIds = savedRegionIds.ifEmpty { it.selectedInterestRegionIds },
                            selectedInterestRegionOptionKey = selectedOption?.key
                                ?: it.selectedInterestRegionOptionKey,
                            isInterestRegionSaving = false,
                            showInterestRegionDialog = false,
                            shouldRequestInterestRegionSetup = false,
                        )
                    }
                }
                .onFailure { cause ->
                    _uiState.update {
                        it.copy(
                            isInterestRegionSaving = false,
                            interestRegionErrorMessage = cause.message
                                ?: "관심 지역을 저장하지 못했어요. 잠시 후 다시 시도해 주세요.",
                        )
                    }
                }
        }
    }
}

private fun HomeDashboard.toUiState(
    username: String,
    interestRegionName: String?,
    shouldRequestInterestRegionSetup: Boolean,
    interestRegionGroups: List<HomeInterestRegionGroup>,
    selectedInterestRegionIds: Set<Long>,
): HomeUiState {
    val nearestUpcomingCourse = courses.upcomingCourses.firstOrNull()
    val magazineUiModels = magazines.map { it.toUiModel() }

    return HomeUiState(
        username = username.ifBlank { courses.banner.nickname.orEmpty() },
        bannerMessage = courses.banner.message,
        interestLocation = interestRegionName
            ?: magazineUiModels.firstOrNull()?.location
            ?: DefaultInterestLocation,
        todayCourse = courses.todayCourse?.toUiModel(),
        upcomingCourse = nearestUpcomingCourse?.toUpcomingCourseUiModel(),
        createdCourses = courses.upcomingCourses.map { it.toCreatedCourseUiModel() },
        todayPickMagazines = magazineUiModels,
        isLoading = false,
        errorMessage = null,
        shouldRequestInterestRegionSetup = shouldRequestInterestRegionSetup,
        interestRegionGroups = interestRegionGroups,
        selectedInterestRegionGroupName = interestRegionGroups
            .firstOrNull { group -> group.options.any { it.regionId in selectedInterestRegionIds } }
            ?.parentName
            ?: interestRegionGroups.firstOrNull()?.parentName.orEmpty(),
        selectedInterestRegionOptionKey = interestRegionGroups
            .flatMap { it.options }
            .firstOrNull { it.regionId in selectedInterestRegionIds }
            ?.key,
        selectedInterestRegionIds = selectedInterestRegionIds,
    )
}

private fun List<InterestRegionOption>.toHomeInterestRegionGroups(): List<HomeInterestRegionGroup> =
    HomeRegionGroupSpecs.map { spec ->
        val children = if (spec.parentName == "서울") {
            val seoulChildren = filter { it.parentRegionName.toRegionParentDisplayNameOrNull() == "서울" }
                .map { it.regionName }
            listOf("서울 전체") + seoulChildren
        } else {
            spec.children
        }

        HomeInterestRegionGroup(
            parentName = spec.parentName,
            displayName = spec.parentName,
            options = children.map { childName ->
                HomeInterestRegionOption(
                    key = "${spec.parentName}::$childName",
                    displayName = childName,
                    regionId = findRegionIdForDisplayName(
                        displayName = childName,
                        parentName = spec.parentName,
                    ),
                )
            },
        )
    }

private fun HomeUiState.selectedInterestRegionName(): String? =
    selectedInterestRegionOption?.displayName

private fun List<InterestRegionOption>.findRegionIdForDisplayName(
    displayName: String,
    parentName: String,
): Long? {
    val names = buildSet {
        add(displayName)
        if (displayName.endsWith(" 전체")) {
            add(parentName)
            parentName.toSidoFullName()?.let(::add)
        }
    }

    return firstOrNull { option ->
        option.regionName in names ||
            option.regionName.toRegionParentDisplayName() in names
    }?.regionId
}

private fun String?.toRegionParentDisplayNameOrNull(): String? =
    this?.toRegionParentDisplayName()

private fun String.toRegionParentDisplayName(): String = when (this) {
    "서울특별시" -> "서울"
    "부산광역시" -> "부산"
    "대구광역시" -> "대구"
    "인천광역시" -> "인천"
    "광주광역시" -> "광주"
    "대전광역시" -> "대전"
    "울산광역시" -> "울산"
    "세종특별자치시" -> "세종"
    "제주특별자치도" -> "제주"
    "강원특별자치도", "강원도" -> "강원"
    "경기도" -> "경기"
    "충청북도", "충청남도" -> "충청"
    "전북특별자치도", "전라북도", "전라남도" -> "전라"
    "경상북도", "경상남도" -> "경상"
    else -> this
}

private fun String.toSidoFullName(): String? = when (this) {
    "서울" -> "서울특별시"
    "부산" -> "부산광역시"
    "제주" -> "제주특별자치도"
    "인천" -> "인천광역시"
    "강원" -> "강원특별자치도"
    "경기" -> "경기도"
    "경상" -> "경상북도"
    "전라" -> "전라북도"
    else -> null
}

private data class HomeRegionGroupSpec(
    val parentName: String,
    val children: List<String>,
)

private val HomeRegionGroupSpecs = listOf(
    HomeRegionGroupSpec("전국", listOf("전국")),
    HomeRegionGroupSpec("서울", listOf("서울 전체")),
    HomeRegionGroupSpec(
        "부산",
        listOf(
            "부산 전체",
            "해운대/센텀시티/재송",
            "광안리/수영/남천",
            "서면/전포/부전",
            "남포동/자갈치/광복동/영도",
            "부산역/초량/동구",
            "동래/온천장/명륜",
            "연산/거제/시청",
            "기장/송정/일광",
            "사상/덕천/북구",
            "명지/강서",
            "하단/다대포/사하",
        ),
    ),
    HomeRegionGroupSpec(
        "제주",
        listOf(
            "제주 전체",
            "제주시/제주공항/용담",
            "노형/연동",
            "애월/한림/협재",
            "조천/함덕/김녕",
            "구좌/월정리/세화",
            "성산/우도",
            "표선/남원",
            "서귀포/천지연/정방",
            "중문/색달",
            "안덕/산방산",
            "대정/모슬포",
            "한라산/중산간",
        ),
    ),
    HomeRegionGroupSpec(
        "인천",
        listOf(
            "인천 전체",
            "송도/연수",
            "구월동/인천터미널/남동",
            "부평/십정",
            "청라/서구",
            "검단/마전",
            "계양/작전",
            "인천역/차이나타운/신포동",
            "월미도/동구",
            "영종도/운서/인천공항",
            "강화도/석모도",
            "영흥도/덕적도/옹진",
        ),
    ),
    HomeRegionGroupSpec(
        "강원",
        listOf(
            "강원 전체",
            "춘천",
            "원주",
            "강릉",
            "속초/고성",
            "양양/낙산",
            "동해/삼척",
            "평창/대관령",
            "정선/태백",
            "홍천/횡성",
            "영월",
            "철원/화천",
            "양구/인제",
        ),
    ),
    HomeRegionGroupSpec(
        "경기",
        listOf(
            "경기 전체",
            "수원/광교",
            "성남/분당/판교",
            "용인/수지/기흥",
            "고양/일산",
            "김포/파주",
            "부천/광명",
            "안양/과천",
            "군포/의왕",
            "하남/구리",
            "남양주/가평",
            "의정부/양주/동두천",
            "포천/연천",
            "화성/동탄/오산",
            "평택/안성",
            "안산/시흥",
            "광주/이천/여주",
            "양평",
        ),
    ),
    HomeRegionGroupSpec(
        "경상",
        listOf(
            "경상 전체",
            "대구 동성로/반월당/중구",
            "대구 동대구/수성구",
            "대구 달서구/성서/앞산",
            "경산/영천/청도",
            "울산 삼산/남구",
            "울산 중구/동구/북구",
            "경주/보문/불국사",
            "포항/영덕/울진",
            "안동/영주/예천",
            "문경/상주/김천",
            "구미/칠곡",
            "창원/마산/진해",
            "김해/양산/밀양",
            "진주/사천",
            "거제/통영/고성",
            "남해/하동",
            "거창/함양/산청/합천",
        ),
    ),
    HomeRegionGroupSpec(
        "전라",
        listOf(
            "전라 전체",
            "광주 충장로/동명동/양림동",
            "광주 상무지구/서구",
            "광주 첨단/수완/광산구",
            "전주/완주",
            "군산/익산",
            "정읍/고창/부안",
            "남원/임실/순창",
            "무주/진안/장수",
            "여수",
            "순천/광양",
            "목포/무안",
            "신안/영암",
            "나주/함평/영광",
            "담양/곡성/구례",
            "보성/화순",
            "장흥/강진",
            "해남/완도/진도",
            "고흥",
        ),
    ),
)

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
