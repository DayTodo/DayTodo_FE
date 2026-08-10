package com.team_daytodo.daytodo.data.dto.mypage

import com.team_daytodo.daytodo.domain.mypage.model.Policies
import kotlinx.serialization.Serializable

@Serializable
data class PoliciesResponse(
    val termsOfService: String,
    val privacyPolicy: String,
)

fun PoliciesResponse.toDomain(): Policies = Policies(
    termsOfService = termsOfService,
    privacyPolicy = privacyPolicy,
)
