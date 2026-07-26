plugins {
    alias(libs.plugins.daytodo.kotlin.library)
}

dependencies {
    api(libs.kotlinx.coroutines.core)

    testImplementation(libs.junit)
}
