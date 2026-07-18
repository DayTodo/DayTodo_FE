package com.team_daytodo.daytodo.feature.course

enum class CourseScreenMode(
    val title: String,
    val description: String,
) {
    List(
        title = "생성한 코스",
        description = "직접 만든 코스 방을 확인할 수 있어요.",
    ),
    Create(
        title = "새 코스 방 만들기",
        description = "새로운 코스 방을 만들 준비를 해요.",
    ),
    Join(
        title = "초대 코드 입력",
        description = "초대받은 그룹의 코드를 입력할 수 있어요.",
    ),
}
