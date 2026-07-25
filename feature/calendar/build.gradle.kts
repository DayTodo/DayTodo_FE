plugins {
    alias(libs.plugins.daytodo.android.feature)
}

android {
    namespace = "com.team_daytodo.daytodo.feature.calendar"
}

dependencies {
    implementation(libs.kizitonwose.calendar.compose)
}
