plugins {
    alias(libs.plugins.daytodo.android.library)
    alias(libs.plugins.daytodo.android.compose)
}

android {
    namespace = "com.team_daytodo.daytodo.uikit"
}

dependencies {
    testImplementation(libs.junit)
}
