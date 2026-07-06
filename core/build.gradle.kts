plugins {
    alias(libs.plugins.daytodo.android.library)
}

android {
    namespace = "com.team_daytodo.daytodo.core"
}

dependencies {
    api(libs.timber)
    api(libs.kotlinx.coroutines.core)

    testImplementation(libs.junit)
}
