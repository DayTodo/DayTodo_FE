package com.team_daytodo.daytodo.domain.auth.usecase

private val EmailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")

fun String.isValidEmail(): Boolean =
    trim().matches(EmailRegex)

fun String.isValidPassword(): Boolean =
    length >= 8 && any(Char::isLetter) && any(Char::isDigit)
