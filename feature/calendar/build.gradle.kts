plugins {
    alias(libs.plugins.daytodo.android.feature)
    alias(libs.plugins.daytodo.hilt)
}

android {
    namespace = "com.team_daytodo.daytodo.feature.calendar"
}

dependencies {
    implementation(libs.kizitonwose.calendar.compose)
}
