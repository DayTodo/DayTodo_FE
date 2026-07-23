package com.team_daytodo.daytodo

import android.net.Uri

internal object DayTodoRoute {
    const val ResetPasswordTokenArg = "verificationToken"

    const val Login = "auth/login"
    const val Signup = "auth/signup"
    const val FindPassword = "auth/find-password"
    const val ResetPassword = "auth/reset-password/{$ResetPasswordTokenArg}"
    const val ProfileSetup = "auth/profile-setup"

    const val Home = "home"
    const val Save = "save"
    const val Calendar = "calendar"
    const val Course = "course"
    const val CourseCreate = "course/create"
    const val CourseJoin = "course/join"
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
}
