package com.team_daytodo.daytodo.domain.course.model

internal fun CourseCreateRequest.validateForCreateOrNull(): InvalidCourseCreateRequestException? =
    when {
        roomName.isBlank() -> InvalidCourseCreateRequestException("코스 방 이름을 입력해 주세요.")
        region.isBlank() -> InvalidCourseCreateRequestException("코스 지역을 선택해 주세요.")
        !date.isValid() -> InvalidCourseCreateRequestException("올바른 날짜를 선택해 주세요.")
        minBudget < 0 || maxBudget < 0 -> InvalidCourseCreateRequestException("예산은 0원 이상으로 입력해 주세요.")
        minBudget > maxBudget -> InvalidCourseCreateRequestException("최소 예산은 최대 예산보다 클 수 없습니다.")
        else -> null
    }

internal fun CourseUpdateRequest.validateForUpdateOrNull(): InvalidCourseEditRequestException? =
    when {
        courseId.isBlank() -> InvalidCourseEditRequestException("코스 정보를 찾을 수 없어요.")
        name.isBlank() -> InvalidCourseEditRequestException("코스 이름을 입력해 주세요.")
        region.isBlank() -> InvalidCourseEditRequestException("지역을 선택해 주세요.")
        !date.isValid() -> InvalidCourseEditRequestException("올바른 날짜를 선택해 주세요.")
        minBudget < 0 || maxBudget < 0 -> InvalidCourseEditRequestException("예산은 0원 이상으로 입력해 주세요.")
        minBudget > maxBudget -> InvalidCourseEditRequestException("최소 예산은 최대 예산보다 클 수 없어요.")
        else -> null
    }
