package com.team_daytodo.daytodo.feature.mypage.model

enum class RemotePolicyField {
    TermsOfService,
    PrivacyPolicy,
}

sealed interface PolicyContent {
    data class Static(val body: String) : PolicyContent
    data class Remote(val field: RemotePolicyField) : PolicyContent
}

data class PolicyDocument(
    val title: String,
    val content: PolicyContent,
)

private val AgeVerificationContent = """
개인정보 보호법 제22조의2에 따라 만 14세 미만 아동의 개인정보를 처리하려면 법정대리인의 동의가 필요합니다. 데이투두는 원활한 서비스 제공을 위해 만 14세 이상 이용자를 대상으로 회원가입을 받고 있으며, 만 14세 미만인 경우 법정대리인의 동의 절차 없이는 가입이 제한될 수 있습니다. 허위로 만 14세 이상임을 표시하여 발생하는 불이익에 대해 회사는 책임을 지지 않습니다.
""".trimIndent()

private val MarketingConsentContent = """
● 수집·이용 목적: 이벤트, 프로모션, 신규 기능 등 마케팅 정보를 이메일·앱 푸시 알림으로 안내
● 수집 항목: 이메일 주소, 푸시 알림 토큰
● 보유·이용 기간: 동의 철회 시 또는 회원 탈퇴 시까지
● 동의 거부 권리 및 불이익: 본 동의를 거부할 수 있으며, 거부하더라도 회원가입·코스 생성 등 핵심 서비스 이용에는 제한이 없습니다.
● 동의 철회 방법: 마이페이지 > 알림 설정에서 언제든지 수신 동의를 철회할 수 있습니다.
""".trimIndent()

// 목록 화면에 표시할 문서(고정 순서: 이용약관 → 개인정보처리방침 → 만 14세 이상 확인 → 마케팅 및 이벤트).
// 이용약관/개인정보처리방침은 GET /users/policies 응답(Policies.termsOfService / Policies.privacyPolicy)을
// 그대로 화면에 표시하므로 정적 텍스트를 들고 있지 않는다. 만 14세 이상 확인/마케팅 및 이벤트는
// 대응하는 API 필드가 없어 기존처럼 정적 텍스트를 유지한다.
val PolicyDocuments: List<PolicyDocument> = listOf(
    PolicyDocument(
        title = "이용약관",
        content = PolicyContent.Remote(RemotePolicyField.TermsOfService),
    ),
    PolicyDocument(
        title = "개인정보처리방침",
        content = PolicyContent.Remote(RemotePolicyField.PrivacyPolicy),
    ),
    PolicyDocument(
        title = "만 14세 이상 확인",
        content = PolicyContent.Static(AgeVerificationContent),
    ),
    PolicyDocument(
        title = "마케팅 및 이벤트",
        content = PolicyContent.Static(MarketingConsentContent),
    ),
)

val PolicyListTitles: List<String> = PolicyDocuments.map { it.title }
