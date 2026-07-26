package com.team_daytodo.daytodo

import android.net.Uri

internal object DayTodoRoute {
    const val ResetPasswordTokenArg = "verificationToken"
    const val CourseIdArg = "courseId"
    const val PlaceIdArg = "placeId"

    const val Login = "auth/login"
    const val Signup = "auth/signup"
    const val FindPassword = "auth/find-password"
    const val ResetPassword = "auth/reset-password/{$ResetPasswordTokenArg}"
    const val ProfileSetup = "auth/profile-setup"

    const val Home = "home"
    const val Save = "save"
    const val MagazineDetail = "magazine/{$PlaceIdArg}"
    const val Calendar = "calendar"
    const val Course = "course"
    const val CourseCreate = "course/create"
    const val CourseJoin = "course/join"
    const val PlaceRecommendation = "course/recommend/{$CourseIdArg}"
    const val CourseEdit = "course/edit/{$CourseIdArg}"
    const val PlaceComment = "course/{$CourseIdArg}/place/{$PlaceIdArg}/comments"
    const val Today = "today"
    const val Record = "record"
    const val Mypage = "mypage"

    val AuthRoutes = setOf(
        Login,
        Signup,
        FindPassword,
        ResetPassword,
        ProfileSetup,
    )

    fun resetPasswordRoute(verificationToken: String): String =
        "auth/reset-password/${Uri.encode(verificationToken)}"

    fun placeRecommendationRoute(courseId: String): String =
        "course/recommend/${Uri.encode(courseId)}"

    fun magazineDetailRoute(placeId: String): String =
        "magazine/${Uri.encode(placeId)}"

    fun courseEditRoute(courseId: String): String =
        "course/edit/${Uri.encode(courseId)}"

    fun placeCommentRoute(
        courseId: String,
        placeId: String,
    ): String =
        "course/${Uri.encode(courseId)}/place/${Uri.encode(placeId)}/comments"
}
